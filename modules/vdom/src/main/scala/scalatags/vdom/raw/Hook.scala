package scalatags.vdom.raw

import org.scalajs.dom
import org.scalajs.dom.DragEvent
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.{Any, UndefOr}
import scala.scalajs.js.annotation._
import scala.scalajs.js.Dynamic.{global => g}

@ScalaJSDefined
trait Hook extends js.Object {
  def hook(node: dom.Node, propertyName: String, previousValue: UndefOr[js.Object]): Unit
}

@ScalaJSDefined
class LogHook extends Hook {
  override def hook(node: dom.Node, propertyName: String, previousValue: UndefOr[js.Object]): Unit = {

    g.console.log("loghook node:" + node)
    g.console.log("loghook propertyName:" + propertyName)
    g.console.log("loghook previousValue:" + previousValue)

  }
}

@ScalaJSDefined
class SpecificElementSet[A <: dom.Node](f: A => Unit) extends Hook {
  override def hook(node: dom.Node, propertyName: String, previousValue: UndefOr[js.Object]): Unit = {
    f(node.asInstanceOf[A])
  }
}

object SpecificElementSet {
  def apply[A <: dom.Node](f: A => Unit): SpecificElementSet[A] = new SpecificElementSet[A](f)
}

@ScalaJSDefined
class OnNodeHooked(f: (dom.Node, String, Option[js.Object]) => Unit) extends Hook {
  override def hook(node: dom.Node, propertyName: String, previousValue: UndefOr[js.Object]): Unit = {
/*    g.console.log(node)
    g.console.log(propertyName)
    previousValue.toOption.foreach(x => g.console.log(g.JSON.stringify(x)))*/

    f(node, propertyName, previousValue.toOption)
  }
}
