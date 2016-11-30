package scalatags

import java.util.Objects

import org.scalajs.dom
import org.scalajs.dom.{MouseEvent, Node}
import org.scalajs.dom.raw._

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.Any
import scalatags.VDom.StringFrag
import scalatags.generic.{Aliases, AttrValue, Namespace, StylePair}
import scalatags.stylesheet.{StyleSheetFrag, StyleTree}
import scalatags.vdom.Builder
import scalatags.vdom.raw.VirtualDom.VTreeChild
import scalatags.vdom.raw._

/**
  * A Scalatags module that generates `VNode`s when the tags are rendered.
  * This provides some additional flexibility over the Text backend, as you
  * can bind structured objects to the attributes of your `VNode` without
  * serializing them first into strings.
  */
object VDom
    extends generic.Bundle[vdom.Builder, VTreeChild, VTreeChild]
    with Aliases[vdom.Builder, VTreeChild, VTreeChild] {

  object attrs extends VDom.Cap with vdom.Attrs

  object tags extends VDom.Cap with vdom.Tags

  object tags2 extends VDom.Cap with vdom.Tags2

  object styles extends VDom.Cap with Styles

  object styles2 extends VDom.Cap with Styles2

  object svgTags extends VDom.Cap with vdom.SvgTags

  object svgAttrs extends VDom.Cap with SvgAttrs

  object implicits extends Aggregate with DataConverters

  object all extends Cap with vdom.Attrs with Styles with vdom.Tags with DataConverters with Aggregate

  object short extends Cap with vdom.Tags with DataConverters with Aggregate with AbstractShort {

    object * extends Cap with vdom.Attrs with Styles

  }

  trait Cap extends Util with vdom.TagFactory { self =>
    type ConcreteHtmlTag[T <: VTreeChild] = TypedTag[T]
    type BaseTagType                      = TypedTag[VTreeChild]

    protected[this] implicit def stringAttrX = new GenericAttr[String]

    protected[this] implicit def stringStyleX = new GenericStyle[String]

    protected[this] implicit def stringPixelStyleX =
      new GenericPixelStyle[String](stringStyleX)

    implicit def UnitFrag(u: Unit): VDom.StringFrag = new VDom.StringFrag("")

    def makeAbstractTypedTag[T <: VTreeChild](tag: String,
                                              void: Boolean,
                                              nameSpaceConfig: Namespace): TypedTag[T] =
      TypedTag(tag, Nil, void)

    implicit class SeqFrag[A](xs: Seq[A])(implicit ev: A => Frag) extends Frag {
      Objects.requireNonNull(xs)

      def applyTo(t: vdom.Builder): Unit = xs.foreach(_.applyTo(t))

      def render: VTreeChild = {
        val builder = new vdom.Builder
        xs.foreach(x => builder.addChild(ev(x)))
        builder.make("DocumentFragment")
      }
    }

  }

  trait Aggregate extends generic.Aggregate[vdom.Builder, VTreeChild, VTreeChild] {
    implicit def ClsModifier(s: stylesheet.Cls): Modifier = new Modifier {
      def applyTo(t: vdom.Builder) = {
        t.addClassName(s.name)
      }
    }

    implicit class StyleFrag(s: generic.StylePair[vdom.Builder, _]) extends StyleSheetFrag {
      def applyTo(c: StyleTree) = {
        val b = new vdom.Builder
        s.applyTo(b)
        val escapedStyles = b.style.mapValues(v => s" $v;")
        c.copy(styles = c.styles ++ escapedStyles)
      }
    }

    def genericAttr[T] = new VDom.GenericAttr[T]

    def genericStyle[T] = new VDom.GenericStyle[T]

    def genericPixelStyle[T](implicit ev: StyleValue[T]): PixelStyleValue[T] =
      new VDom.GenericPixelStyle[T](ev)

    def genericPixelStylePx[T](implicit ev: StyleValue[String]): PixelStyleValue[T] =
      new VDom.GenericPixelStylePx[T](ev)

    implicit def stringFrag(v: String): StringFrag = new VDom.StringFrag(v)

    val RawFrag = VDom.RawFrag
    type RawFrag = VDom.RawFrag

    val StringFrag = VDom.StringFrag
    type StringFrag = VDom.StringFrag

    def raw(s: String) = RawFrag(s)

    // type Tag = VDom.TypedTag[VTreeChild]
    val Tag = VDom.TypedTag
  }

  object RawFrag extends Companion[RawFrag]
  case class RawFrag(v: String) extends vdom.Frag {
    Objects.requireNonNull(v)
    def render: VText = new VText(v)
  }

  object StringFrag extends Companion[StringFrag]
  case class StringFrag(v: String) extends vdom.Frag {
    Objects.requireNonNull(v)
    def render: VText = new VText(v)
  }

  class GenericAttr[T] extends AttrValue[T] {
    def apply(t: vdom.Builder, a: Attr, v: T): Unit = {
      t.updateAttribute(a.name, v.toString)
    }
  }

  class GenericStyle[T] extends StyleValue[T] {
    def apply(t: vdom.Builder, s: Style, v: T): Unit = {
      t.addStyle(s.cssName, v.toString)
    }
  }

  class GenericPixelStyle[T](ev: StyleValue[T]) extends PixelStyleValue[T] {
    def apply(s: Style, v: T) = StylePair(s, v, ev)
  }

  class GenericPixelStylePx[T](ev: StyleValue[String]) extends PixelStyleValue[T] {
    def apply(s: Style, v: T) = StylePair(s, v + "px", ev)
  }

  case class TypedTag[+Output <: VTreeChild](tag: String = "",
                                             modifiers: List[Seq[Modifier]],
                                             void: Boolean = false)
      extends generic.TypedTag[vdom.Builder, Output, VTreeChild]
      with vdom.Frag {

    protected[this] type Self = TypedTag[Output]

    def render: Output = {
      val builder = new vdom.Builder
      this.build(builder)
      builder.make(tag).asInstanceOf[Output]
    }

    def apply(xs: Modifier*): TypedTag[Output] = {
      this.copy(tag = tag, void = void, modifiers = xs :: modifiers)
    }

    override def toString = VirtualDom.create(render).outerHTML
  }

}

// todo: hacky way via strings of the attributes
// todo: add remaining stuff
// todo: define order of implicits

sealed trait MouseEventImplicits {
  /*  implicit object bindJsAny extends generic.AttrValue[VNode, js.Any] {
    def apply(t: VNode, a: generic.Attr, v: js.Any): Unit = {
      t.asInstanceOf[js.Dynamic].updateDynamic(a.name)(v)
    }
  }*/
  //implicit def bindJsFunc[A](implicit ev: A =:= Function1[dom.Event, Unit]): AttrValue[Builder, A] = ???

  implicit def bindMouseEvent[T <: dom.MouseEvent => Unit]: AttrValue[Builder, T] =
    new generic.AttrValue[Builder, T] {
      def apply(t: Builder, a: generic.Attr, v: T): Unit = {
        val hook = a.name match {
          case "onclick"     => SpecificElementSet[HTMLDocument](_.onclick = v)
          case "onmousedown" => SpecificElementSet[HTMLElement](_.onmousedown = v)
          case "onmouseup"   => SpecificElementSet[HTMLElement](_.onmouseup = v)
          case "onmousemove" => SpecificElementSet[HTMLElement](_.onmousemove = v)
        }

        t.updateProperty("hook-" + a.name, hook)
      }
    }

  /*
 implicit class bindNode(e: dom.Node) extends generic.Frag[VNode, dom.Node] {
    def applyTo(t: VNode) = t.children.push(t)
    def render            = e
  }*/
}

sealed trait EventImplicits {
  implicit def bindEvent[T <: dom.Event => Unit]: AttrValue[Builder, T] =
    new generic.AttrValue[Builder, T] {
      def apply(t: Builder, a: generic.Attr, v: T): Unit = {
        val hook = a.name match {
          case "onchange" => SpecificElementSet[HTMLDocument](_.onchange = v)
          case "onsubmit" => SpecificElementSet[HTMLDocument](_.onsubmit = v)
          case "onload"   => SpecificElementSet[HTMLDocument](_.onload = v)
          case "oninput"  => SpecificElementSet[HTMLDocument](_.oninput = v)
        }

        t.updateProperty("hook-" + a.name, hook)
      }
    }
}

// Catches anything with Event, so by importing this user gets only most general type
sealed trait AllEventsImplicits {
  implicit def bindVDom[T <: dom.Node => Unit]: AttrValue[Builder, T] =
    new generic.AttrValue[Builder, T] {
      def apply(t: Builder, a: generic.Attr, v: T): Unit = {
        t.updateProperty("hook-" + a.name, new OnNodeHooked(v))
      }
    }

  implicit def bindEvent[T <: dom.Event => Unit]: AttrValue[Builder, T] =
    new generic.AttrValue[Builder, T] {
      def apply(t: Builder, a: generic.Attr, v: T): Unit = {
        val hook = a.name match {
          case "onbeforeactivate"   => SpecificElementSet[HTMLDocument](_.onbeforeactivate = v)
          case "onactivate"         => SpecificElementSet[HTMLDocument](_.onactivate = v)
          case "onbeforedeactivate" => SpecificElementSet[HTMLDocument](_.onbeforedeactivate = v)
          case "ondeactivate"       => SpecificElementSet[HTMLDocument](_.ondeactivate = v)
          case "onload"             => SpecificElementSet[HTMLDocument](_.onload = v)
          case "onchange"           => SpecificElementSet[HTMLDocument](_.onchange = v)
          case "onreadystatechange" => SpecificElementSet[HTMLDocument](_.onreadystatechange = v)

          case "onsubmit"          => SpecificElementSet[HTMLDocument](_.onsubmit = v)
          case "onfocus"           => SpecificElementSet[HTMLDocument](_.onfocus = v)
          case "onblur"            => SpecificElementSet[HTMLDocument](_.onblur = v)
          case "onfocusin"         => SpecificElementSet[HTMLDocument](_.onfocusin = v)
          case "onfocusout"        => SpecificElementSet[HTMLDocument](_.onfocusout = v)
          case "onselect"          => SpecificElementSet[HTMLDocument](_.onselect = v)
          case "onselectstart"     => SpecificElementSet[HTMLDocument](_.onselectstart = v)
          case "onselectionchange" => SpecificElementSet[HTMLDocument](_.onselectionchange = v)
          case "oninput"           => SpecificElementSet[HTMLDocument](_.oninput = v)
          case "onkeydown"         => SpecificElementSet[HTMLDocument](_.onkeydown = v)
          case "onkeyup"           => SpecificElementSet[HTMLDocument](_.onkeyup = v)
          case "onkeypress"        => SpecificElementSet[HTMLDocument](_.onkeypress = v)

          case "onclick"          => SpecificElementSet[HTMLDocument](_.onclick = v)
          case "ondblclick"       => SpecificElementSet[HTMLDocument](_.ondblclick = v)
          case "onmouseup"        => SpecificElementSet[HTMLElement](_.onmouseup = v)
          case "onmouseover"      => SpecificElementSet[HTMLElement](_.onmouseover = v)
          case "onmousedown"      => SpecificElementSet[HTMLElement](_.onmousedown = v)
          case "onmousemove"      => SpecificElementSet[HTMLElement](_.onmousemove = v)
          case "onmouseout"       => SpecificElementSet[HTMLElement](_.onmouseout = v)
          case "onmousewheel"     => SpecificElementSet[HTMLElement](_.onmousewheel = v)
          case "onscroll"         => SpecificElementSet[HTMLElement](_.onscroll = v)
          case "ondrag"           => SpecificElementSet[HTMLElement](_.ondrag = v)
          case "ondragenter"      => SpecificElementSet[HTMLElement](_.ondragenter = v)
          case "ondragleave"      => SpecificElementSet[HTMLElement](_.ondragleave = v)
          case "ondragover"       => SpecificElementSet[HTMLElement](_.ondragover = v)
          case "ondragstart"      => SpecificElementSet[HTMLElement](_.ondragstart = v)
          case "ondragend"        => SpecificElementSet[HTMLElement](_.ondragend = v)
          case "ondrop"           => SpecificElementSet[HTMLElement](_.ondrop = v)
          case "onreset"          => SpecificElementSet[HTMLElement](_.onreset = v)
          case "onhelp"           => SpecificElementSet[HTMLElement](_.onhelp = v)
          case "onseeked"         => SpecificElementSet[HTMLElement](_.onseeked = v)
          case "onemptied"        => SpecificElementSet[HTMLElement](_.onemptied = v)
          case "onseeking"        => SpecificElementSet[HTMLElement](_.onseeking = v)
          case "oncanplay"        => SpecificElementSet[HTMLElement](_.oncanplay = v)
          case "ontimeupdate"     => SpecificElementSet[HTMLElement](_.ontimeupdate = v)
          case "onended"          => SpecificElementSet[HTMLElement](_.onended = v)
          case "onsuspend"        => SpecificElementSet[HTMLElement](_.onsuspend = v)
          case "onpause"          => SpecificElementSet[HTMLElement](_.onpause = v)
          case "onwaiting"        => SpecificElementSet[HTMLElement](_.onwaiting = v)
          case "onstop"           => SpecificElementSet[HTMLDocument](_.onstop = v)
          case "onstalled"        => SpecificElementSet[HTMLElement](_.onstalled = v)
          case "onratechange"     => SpecificElementSet[HTMLElement](_.onratechange = v)
          case "onvolumechange"   => SpecificElementSet[HTMLElement](_.onvolumechange= v)
          //case "onprogress"       => SpecificElementSet[HTMLElement](_.onprogress = v)
          case "oncontextmenu"    => SpecificElementSet[HTMLElement](_.oncontextmenu = v)
          case "ondurationchange" => SpecificElementSet[HTMLElement](_.ondurationchange = v)
          case "onloadeddata"     => SpecificElementSet[HTMLElement](_.onloadeddata = v)
          case "onloadedmetadata" => SpecificElementSet[HTMLElement](_.onloadedmetadata = v)
          case "onerror"          => SpecificElementSet[HTMLFrameSetElement](_.onerror = v)
          case "onplay"           => SpecificElementSet[HTMLElement](_.onplay = v)
          case "onplaying"        => SpecificElementSet[HTMLElement](_.onplaying = v)
          case "onabort"          => SpecificElementSet[HTMLElement](_.onabort = v)
          case "oncanplaythrough" => SpecificElementSet[HTMLElement](_.oncanplaythrough = v)
          case "onstoragecommit"  => SpecificElementSet[HTMLDocument](_.onstoragecommit = v)

        }

        t.updateProperty("hook-" + a.name, hook)
      }
    }
}

object events {

  object MouseEventImplicits extends MouseEventImplicits

  object EventImplicits extends EventImplicits

  object AllEventsImplicits extends AllEventsImplicits
}
