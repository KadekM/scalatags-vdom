package sample

import org.scalajs.dom

import scala.scalajs.js.JSApp
import scalatags.VDom
import scalatags.VDom.all._
import scalatags.vdom.raw.VirtualDom

object Main extends JSApp {
  def main(): Unit = {
    println("Started")
    val appDiv = dom.document.getElementById("app")

    val vdom = div(
      input(`type` := "text", value := "something"),
      input(`type` := "button", value := "click me")
    )


    val vnode = vdom.render

    val el = VirtualDom.create(vnode)
    appDiv.appendChild(el)

  }
}

