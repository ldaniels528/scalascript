package com.github.ldaniels528.scalascript.extensions

import scala.scalajs.js

/**
 * Cookies Service (requires: ngCookies) - Provides read/write access to browser's cookies.
 * @author lawrence.daniels@gmail.com
 * @see [[https://docs.angularjs.org/api/ngCookies/service/$cookies]]
 */
trait Cookies extends js.Object {

  /**
   * Returns the value of given cookie key
   * @param key the given cookie key
   * @tparam T the cookie value's type
   * @return the value of given cookie key
   */
  def get[T](key: String): js.UndefOr[T] = js.native

  def getAll(): js.Any = js.native

  def getObject[T](key: String): js.UndefOr[T] = js.native

  def put[T](key: String, value: T): Unit = js.native

  def putObject[T](key: String, value: T, options: js.Any = null): Unit = js.native

  def remove(key: String, options: js.Any = null): js.Any = js.native

}

/**
 * Cookies Service Singleton
 * @author lawrence.daniels@gmail.com
 */
object Cookies {

  /**
   * Cookie Extensions
   * @param cookies the given cookies instance
   */
  implicit class CookieExtensions(val cookies: Cookies) extends AnyVal {

    @inline
    def getOrElse[T](key: String, defaultValue: T): T = (cookies.get(key) getOrElse defaultValue).asInstanceOf[T]

    @inline
    def getObjectOrElse[T](key: String, defaultValue: T): T = (cookies.getObject(key) getOrElse defaultValue).asInstanceOf[T]

  }

}
