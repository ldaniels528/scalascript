package com.github.ldaniels528.scalascript.core

import scala.scalajs.js

/**
 * Parse Service - Converts Angular expression into a function.
 * @see [[https://docs.angularjs.org/api/ng/service/\$parse]]
 */
trait Parse extends js.Object {

  def apply(expression: String): js.Function2[js.Object, js.Object, js.Function] = js.native

}

/**
 * Parse Provider - can be used for configuring the default behavior of the \$parse service.
 * @see [[https://docs.angularjs.org/api/ng/provider/\$parseProvider]]
 */
trait ParseProvider extends Provider[Parse]