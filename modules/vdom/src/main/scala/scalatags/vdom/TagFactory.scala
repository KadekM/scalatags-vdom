package scalatags.vdom

import scalatags.Escaping
import scalatags.generic.Namespace
import scalatags.vdom.raw.VNode
import scalatags.vdom.raw.VirtualDom.VTreeChild

trait TagFactory
    extends scalatags.generic.Util[Builder, VTreeChild, VTreeChild] {
  def nTag(s: String, void: Boolean = false): ConcreteHtmlTag[VNode] = {
    if (!Escaping.validTag(s))
      throw new IllegalArgumentException(
        s"Illegal tag name: $s is not a valid XML tag name"
      )
    makeAbstractTypedTag[VNode](s, void, Namespace.htmlNamespaceConfig)
  }

  def tag(s: String, void: Boolean = false): ConcreteHtmlTag[VTreeChild] = {
    if (!Escaping.validTag(s))
      throw new IllegalArgumentException(
        s"Illegal tag name: $s is not a valid XML tag name"
      )
    makeAbstractTypedTag[VTreeChild](s, void, Namespace.htmlNamespaceConfig)
  }
}
