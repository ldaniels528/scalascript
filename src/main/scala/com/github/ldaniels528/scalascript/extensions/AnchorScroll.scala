package com.github.ldaniels528.scalascript.extensions

import com.github.ldaniels528.scalascript.core.Provider

import scala.scalajs.js

/**
 * AnchorScroll Service
 * @see [[https://docs.angularjs.org/api/ng/service/\$anchorScroll]]
 */
trait AnchorScroll extends js.Object {

  def apply(hash: String = js.native): js.Any = js.native

  var yOffset: js.Any = js.native

}

/**
 * AnchorScroll Provider
 * @see [[https://docs.angularjs.org/api/ng/provider/\$anchorScrollProvider]]
 */
trait AnchorScrollProvider extends Provider[AnchorScroll] {

  def disableAutoScrolling(): js.Any = js.native

}