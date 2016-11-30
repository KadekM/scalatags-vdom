package sample

import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent}

import scala.scalajs.js.JSApp
import scala.scalajs.js.Dynamic.global

object Main extends JSApp {
  import scalatags.VDom.all._
  import scalatags.vdom.raw.VirtualDom
  import scalatags.events.MouseEventImplicits._
  import scalatags.events.AllEventsImplicits._

  def main(): Unit = {
    println("Started")
    val appDiv = dom.document.getElementById("app")

    val inputText = input(`type` := "text", value := "something", onvdomload := {(e: dom.Node) => global.console.log(e); ()})
    //val inputText = input(`type` := "text", value := "something")
    val alertButton = input(`type` := "button", value := "alert", onclick := "alert();" )
    val printlnButton = input(`type` := "button", value := "println", onclick := { (e: MouseEvent) => println("mouse event: " + e) })

    val vdom = div(
      inputText,
      alertButton,
      printlnButton
    )

    //println("-- alertButton --")
    //global.console.log(alertButton.render.hooks)
    //global.console.log(alertButton.render.properties)

    //println("-- printlnButton --")
    //global.console.log(printlnButton.render.hooks)
    //global.console.log(printlnButton.render.properties)

    val vnode = vdom.render

    val el = VirtualDom.create(vnode)
    appDiv.appendChild(el)

  }
}



