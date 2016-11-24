package scalatags.vdom

import scala.scalajs.js
import scalatags.vdom.raw.VNode
import scalatags.vdom.raw.VirtualDom.VTreeChild

class Builder() {
  import scalatags.generic.Frag

  val attributes = js.Dictionary.empty[js.Any]
  val properties = js.Dictionary.empty[js.Any]
  val style      = js.Dictionary.empty[String]
  val classNames = js.Array[String]()
  val children   = js.Array[Frag[_, VTreeChild]]()

  def addChild(f: Frag[_, VTreeChild]): Unit = children.push(f)

  def addClassName(name: String): Unit = classNames.push(name)

  def makeClassNameString: Option[String] = {
    val namesOpt =
      if (classNames.isEmpty) None
      else Some(classNames.mkString(" "))

    attributes
      .get("class")
      .asInstanceOf[Option[String]]
      .flatMap { x =>
        namesOpt.map(y => s"$x $y")
      }
      .orElse(namesOpt)
  }

  def updateProperty(key: String, value: js.Any): Unit =
    properties.update(key, value)

  def updateAttribute(key: String, value: js.Any): Unit =
    attributes.update(key, value)

  def addStyle(key: String, value: String): Unit =
    style.update(key, value)

  def make(tag: String): VNode = {
    import js.JSConverters._

    val renderedChildren = Option(children.map(_.render)).orUndefined

    makeClassNameString.foreach(this.updateAttribute("class", _))

    val props =
      js.Dictionary[js.Any](
        "attributes" -> attributes,
        "style" -> style)

    properties.foreach { case (k, v) => props.update(k, v) }

    new VNode(tag, Option(props).orUndefined, renderedChildren)
  }

}
