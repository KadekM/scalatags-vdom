package scalatags.vdom.raw

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("virtualDom")
@js.native
object VirtualDom extends js.Object {
  def create(node: VTreeChild,
             opts: js.UndefOr[js.Object] = js.undefined): dom.raw.HTMLElement =
    js.native

  def diff(lhs: VTreeChild, rhs: VTreeChild): Patch = js.native

  def patch(rootNode: dom.raw.Node, patches: Patch): dom.raw.HTMLElement =
    js.native

  @js.native
  trait VTreeChild extends js.Any
  @js.native
  trait Patch extends js.Object
}
