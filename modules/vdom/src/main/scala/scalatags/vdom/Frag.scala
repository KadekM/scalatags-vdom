package scalatags.vdom

import org.scalajs.dom.raw.HTMLElement

import scalatags.generic
import scalatags.vdom.raw.VirtualDom.{Patch, VTreeChild}
import scalatags.vdom.raw.{VNode, VirtualDom}

trait Frag extends generic.Frag[Builder, VTreeChild] {
  def render: VTreeChild
  def diff(vnode: VNode): Patch = VirtualDom.diff(this.render, vnode)
  def create(): HTMLElement     = VirtualDom.create(this.render)

  override def applyTo(t: Builder): Unit = t.addChild(this)
}
