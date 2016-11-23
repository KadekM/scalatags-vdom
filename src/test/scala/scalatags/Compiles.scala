package scalatags

import scalatags.VDom.all._

object Compiles {

  val labelElem = label("Default").render

  val inputElem = input(
  `type`:="text",
   onfocus := { () => println("test") }
  //onfocus := { () => labelElem.textContent = ""}
  ).render

  val box = div(
  inputElem,
  labelElem
  ).render

/*  assert(labelElem.textContent == "Default")
  inputElem.onfocus(null)
  assert(labelElem.textContent == "")*/

}
