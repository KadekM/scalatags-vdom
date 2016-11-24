package scalatags.vdom.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scalatags.vdom.raw.VirtualDom.VTreeChild

@JSName("virtualDom.VText")
@js.native
class VText(str: String) extends js.Object with VTreeChild

