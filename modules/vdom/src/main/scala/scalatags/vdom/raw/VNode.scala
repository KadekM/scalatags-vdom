package scalatags.vdom.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scalatags.vdom.raw.VirtualDom.VTreeChild

@JSName("virtualDom.VNode")
@js.native
class VNode(tagName0: String,
            properties0: js.UndefOr[js.Dictionary[js.Any]] = js.undefined,
            children0: js.UndefOr[js.Array[VTreeChild]] = js.undefined)
    extends js.Object
    with VTreeChild {
  def tagName: String                          = js.native
  def key: js.UndefOr[String]                  = js.native
  def namespace: js.UndefOr[String]            = js.native
  def count: Int                               = js.native
  def hasWidgets: Boolean                      = js.native
  def hasThunks: Boolean                       = js.native
  def hooks: js.UndefOr[js.Dictionary[js.Any]] = js.native
  def children: js.Array[VTreeChild]           = js.native
  def properties: js.Dictionary[js.Any]        = js.native
}
