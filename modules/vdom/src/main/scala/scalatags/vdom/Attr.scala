package scalatags.vdom

import scalatags.generic
import scalatags.generic._
import scalatags.vdom.raw.VirtualDom.VTreeChild

trait Attrs extends generic.Attrs[Builder, VTreeChild, VTreeChild] {

  lazy val onvdomload = attr("onvdomload")

}
