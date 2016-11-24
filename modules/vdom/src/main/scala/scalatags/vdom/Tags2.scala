package scalatags
package vdom

import scalatags.vdom.raw.VNode
import scalatags.vdom.raw.VirtualDom.VTreeChild

trait Tags2
    extends generic.Tags2[Builder, VTreeChild, VTreeChild]
    with TagFactory {
  // Document Metadata
  lazy val title = nTag("title")
  lazy val style = nTag("style")
  // Scripting
  lazy val noscript = nTag("noscript")
  // Sections
  lazy val section = nTag("section")
  lazy val nav     = nTag("nav")
  lazy val article = nTag("article")
  lazy val aside   = nTag("aside")
  lazy val address = nTag("address")
  lazy val main    = nTag("main")
  // Text level semantics
  lazy val q     = nTag("q")
  lazy val dfn   = nTag("dfn")
  lazy val abbr  = nTag("abbr")
  lazy val data  = nTag("data")
  lazy val time  = nTag("time")
  lazy val `var` = nTag("var")
  lazy val samp  = nTag("samp")
  lazy val kbd   = nTag("kbd")
  lazy val math  = nTag("math")
  lazy val mark  = nTag("mark")
  lazy val ruby  = nTag("ruby")
  lazy val rt    = nTag("rt")
  lazy val rp    = nTag("rp")
  lazy val bdi   = nTag("bdi")
  lazy val bdo   = nTag("bdo")
  // Forms
  lazy val keygen   = nTag("keygen", void = true)
  lazy val output   = nTag("output")
  lazy val progress = nTag("progress")
  lazy val meter    = nTag("meter")
  // Interactive elements
  lazy val details = nTag("details")
  lazy val summary = nTag("summary")
  lazy val command = nTag("command", void = true)
  lazy val menu    = nTag("menu")
}
