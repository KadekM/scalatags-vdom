package scalatags
package vdom

import scalatags.vdom.raw.VirtualDom.VTreeChild

trait Tags
    extends generic.Tags[Builder, VTreeChild, VTreeChild]
    with TagFactory {
  // Root Element
  lazy val html = nTag("html")
  // Document Metadata
  lazy val head = nTag("head")
  lazy val base = nTag("base", void = true)
  lazy val link = nTag("link", void = true)
  lazy val meta = nTag("meta", void = true)
  // Scripting
  lazy val script = nTag("script")
  // Sections
  lazy val body   = nTag("body")
  lazy val h1     = nTag("h1")
  lazy val h2     = nTag("h2")
  lazy val h3     = nTag("h3")
  lazy val h4     = nTag("h4")
  lazy val h5     = nTag("h5")
  lazy val h6     = nTag("h6")
  lazy val header = nTag("header")
  lazy val footer = nTag("footer")
  // Grouping content
  lazy val p          = nTag("p")
  lazy val hr         = nTag("hr", void = true)
  lazy val pre        = nTag("pre")
  lazy val blockquote = nTag("blockquote")
  lazy val ol         = nTag("ol")
  lazy val ul         = nTag("ul")
  lazy val li         = nTag("li")
  lazy val dl         = nTag("dl")
  lazy val dt         = nTag("dt")
  lazy val dd         = nTag("dd")
  lazy val figure     = nTag("figure")
  lazy val figcaption = nTag("figcaption")
  lazy val div        = nTag("div")
  // Text-level semantics
  lazy val a      = nTag("a")
  lazy val em     = nTag("em")
  lazy val strong = nTag("strong")
  lazy val small  = nTag("small")
  lazy val s      = nTag("s")
  lazy val cite   = nTag("cite")
  lazy val code   = nTag("code")
  lazy val sub    = nTag("sub")
  lazy val sup    = nTag("sup")
  lazy val i      = nTag("i")
  lazy val b      = nTag("b")
  lazy val u      = nTag("u")
  lazy val span   = nTag("span")
  lazy val br     = nTag("br", void = true)
  lazy val wbr    = nTag("wbr", void = true)
  // Edits
  lazy val ins = nTag("ins")
  lazy val del = nTag("del")
  // Embedded content
  lazy val img      = nTag("img", void = true)
  lazy val iframe   = nTag("iframe")
  lazy val embed    = nTag("embed", void = true)
  lazy val `object` = nTag("object")
  lazy val param    = nTag("param", void = true)
  lazy val video    = nTag("video")
  lazy val audio    = nTag("audio")
  lazy val source   = nTag("source", void = true)
  lazy val track    = nTag("track", void = true)
  lazy val canvas   = nTag("canvas")
  lazy val map      = nTag("map")
  lazy val area     = nTag("area", void = true)
  // Tabular data
  lazy val table    = nTag("table")
  lazy val caption  = nTag("caption")
  lazy val colgroup = nTag("colgroup")
  lazy val col      = nTag("col", void = true)
  lazy val tbody    = nTag("tbody")
  lazy val thead    = nTag("thead")
  lazy val tfoot    = nTag("tfoot")
  lazy val tr       = nTag("tr")
  lazy val td       = nTag("td")
  lazy val th       = nTag("th")
  // Forms
  lazy val form     = nTag("form")
  lazy val fieldset = nTag("fieldset")
  lazy val legend   = nTag("legend")
  lazy val label    = nTag("label")
  lazy val input    = nTag("input", void = true)
  lazy val button   = nTag("button")
  lazy val select   = nTag("select")
  lazy val datalist = nTag("datalist")
  lazy val optgroup = nTag("optgroup")
  lazy val option   = nTag("option")
  lazy val textarea = nTag("textarea")
}
