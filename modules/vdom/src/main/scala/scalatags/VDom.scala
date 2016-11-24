package scalatags

import java.util.Objects

import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.raw._

import scala.language.implicitConversions
import scala.scalajs.js
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

  object attrs extends VDom.Cap with Attrs

  object tags extends VDom.Cap with vdom.Tags

  object tags2 extends VDom.Cap with vdom.Tags2

  object styles extends VDom.Cap with Styles

  object styles2 extends VDom.Cap with Styles2

  object svgTags extends VDom.Cap with vdom.SvgTags

  object svgAttrs extends VDom.Cap with SvgAttrs

  object implicits extends Aggregate with DataConverters

  object all extends Cap with Attrs with Styles with vdom.Tags with DataConverters with Aggregate

  object short extends Cap with vdom.Tags with DataConverters with Aggregate with AbstractShort {

    object * extends Cap with Attrs with Styles

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
          case "onclick"     => SpecificElementSet[HTMLDocument](_.onclick = (e: MouseEvent) => v(e))
          case "onmousedown" => SpecificElementSet[HTMLElement](_.onmousedown = (e: MouseEvent) => v(e))
          case "onmouseup"   => SpecificElementSet[HTMLElement](_.onmouseup = (e: MouseEvent) => v(e))
          case "onmousemove" => SpecificElementSet[HTMLElement](_.onmousemove = (e: MouseEvent) => v(e))
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
          case "onchange" => SpecificElementSet[HTMLDocument](_.onchange = (e: Event) => v(e))
          case "onsubmit" => SpecificElementSet[HTMLDocument](_.onsubmit = (e: Event) => v(e))
          case "onload"   => SpecificElementSet[HTMLDocument](_.onload = (e: Event) => v(e))
          case "oninput"  => SpecificElementSet[HTMLDocument](_.oninput = (e: Event) => v(e))
        }

        t.updateProperty("hook-" + a.name, hook)
      }
    }
}

object events {

  object MouseEventImplicits extends MouseEventImplicits

  object EventImplicits extends EventImplicits

}
