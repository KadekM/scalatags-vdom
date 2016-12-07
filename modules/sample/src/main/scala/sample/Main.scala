package sample

import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent}

import scala.scalajs.js.JSApp
import scala.scalajs.js.Dynamic.global
import rxscalajs.Observable

import scala.concurrent.duration._
import scala.scalajs.js

object Main extends JSApp {
  import scalatags.VDom.all._
  import scalatags.vdom.raw.VirtualDom
  import scalatags.events.MouseEventImplicits._
  import scalatags.events.AllEventsImplicits._

  def main(): Unit = {
    println("Started")
    val appDiv = dom.document.getElementById("app")

    /*    val inputText = input(`type` := "text", value := "something", onvdomload := { (e: dom.Node) =>
      global.console.log(e); ()
    })*/

    //val inputText = input(`type` := "text", value := "something")
    val alertButton = input(`type` := "button", value := "alert", onclick := "alert();")
    val printlnButton = input(`type` := "button", value := "println", onclick := { (e: MouseEvent) =>
      println("mouse event: " + e)
    })

    def static(counter: Int) = div(
      //inputText,
      alertButton,
      printlnButton,
      input(value := counter.toString, onvdomload := { (e: dom.Node, prop: String, prev: Option[js.Object]) =>
        global.console.log(e); ()
      })
    )

    //println("-- alertButton --")
    //global.console.log(alertButton.render.hooks)
    //global.console.log(alertButton.render.properties)

    //println("-- printlnButton --")
    //global.console.log(printlnButton.render.hooks)
    //global.console.log(printlnButton.render.properties)

    val vnodeR = static(0).render

    val root = VirtualDom.create(vnodeR)
    appDiv.appendChild(root)

    Observable
      .interval(1000.millis)
      .scan((vnodeR, root)) {
        case ((prev, node), i) =>
          val _next  = static(i)
          val nextR  = _next.render
          val patch  = VirtualDom.diff(prev, nextR)
          val nowDiv = VirtualDom.patch(node, patch)

          (nextR, nowDiv)
      }
      .subscribe(x => println("## re-rendered ##"))
  }
}
