package com.github.ldaniels528.scalascript

import com.github.ldaniels528.scalascript.core.{Injector, Q}
import org.scalajs.dom._

import scala.scalajs.js

/**
 * Represents an Angular Element
 * @author lawrence.daniels@gmail.com
 */
trait AngularElement extends Element {

  def find(element: String): AngularElement = js.native

  def find(element: js.Any): AngularElement = js.native

  def bind[T <: Event](event: String, handler: js.Function1[T, _]): Unit = js.native

  def controller(): js.Any = js.native

  def controller(name: String): js.Any = js.native

  def injector(): Injector = js.native

  def scope(): Scope = js.native

  def isolateScope(): Scope = js.native

  def inheritedData(key: String, value: js.Any): Q = js.native

  def inheritedData(obj: js.Dictionary[Any]): Q = js.native

  def inheritedData(key: String): js.Any = js.native

  def inheritedData(): js.Any = js.native

  def unbind[T <: Event](event: String, handler: js.Function1[T, _]): Unit = js.native

}

