package com.github.ldaniels528.scalascript.core

import scala.scalajs.js

/**
 * AngularJS Provider
 */
trait Provider[T <: js.Object] extends js.Object {

  def $get(): T = js.native

}
