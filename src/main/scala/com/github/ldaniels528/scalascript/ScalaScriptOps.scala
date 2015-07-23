package com.github.ldaniels528.scalascript

import scala.scalajs.js

/**
 * ScalaScript Operations
 */
object ScalaScriptOps {

  /**
   * UndefOr Extensions
   * @param op the given [[js.UndefOr undefined or otherwise value]]
   */
  implicit class UndefOrExtensions[T](val op: js.UndefOr[T]) extends AnyVal {

    def contains(value: T): Boolean = op.exists(_ == value)

  }

}
