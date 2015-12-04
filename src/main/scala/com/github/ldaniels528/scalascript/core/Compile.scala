package com.github.ldaniels528.scalascript.core

import scala.scalajs.js

/**
  * AngularJS $compile Service
  * @author lawrence.daniels@gmail.com
  */
trait Compile extends js.Object {

  def apply(element: Any): Any

  // TODO add other methods here

}
