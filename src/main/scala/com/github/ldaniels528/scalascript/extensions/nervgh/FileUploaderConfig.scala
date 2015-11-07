package com.github.ldaniels528.scalascript.extensions.nervgh

import com.github.ldaniels528.scalascript.util.ScalaJsHelper._

import scala.scalajs.js

/**
  * File Uploader Config
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FileUploaderConfig extends js.Object {
  var url: js.UndefOr[String] = js.native
  var filters: js.Array[FileFilter] = js.native
}

/**
  * File Uploader Config Companion
  * @author lawrence.daniels@gmail.com
  */
object FileUploaderConfig {

  def apply(url: js.UndefOr[String] = js.undefined, filters: js.Array[FileFilter] = emptyArray) = {
    val config = makeNew[FileUploaderConfig]
    config.url = url
    config.filters = filters
    config
  }

}