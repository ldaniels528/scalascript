package com.github.ldaniels528.scalascript.core

import org.scalajs.dom.html.Element

import scala.scalajs.js
import scala.scalajs.js.RegExp

/**
 * <h4>AngularJS Compile Service</h4>
 * Compiles an HTML string or DOM into a template and produces a template function,
 * which can then be used to link scope and the template together.
 * @author lawrence.daniels@gmail.com
 */
trait Compile extends js.Object {

  def apply(element: String): TemplateLinkingFunction = js.native

  def apply(element: String, transclude: TranscludeFunction): TemplateLinkingFunction = js.native

  def apply(element: String, transclude: TranscludeFunction, maxPriority: Int): TemplateLinkingFunction = js.native

  def apply(element: Element): TemplateLinkingFunction = js.native

  def apply(element: Element, transclude: TranscludeFunction): TemplateLinkingFunction = js.native

  def apply(element: Element, transclude: TranscludeFunction, maxPriority: Int): TemplateLinkingFunction = js.native

  def apply(element: JQLite): TemplateLinkingFunction = js.native

  def apply(element: JQLite, transclude: TranscludeFunction): TemplateLinkingFunction = js.native

  def apply(element: JQLite, transclude: TranscludeFunction, maxPriority: Int): TemplateLinkingFunction = js.native

  // These signatures result in a compilation error when JQuery bindings are not in classpath,
  // since the compiler probably checks all signatures, even if we don't use them!
  //def apply(element: JQuery): TemplateLinkingFunction = js.native
  //def apply(element: JQuery, transclude: TranscludeFunction): TemplateLinkingFunction = js.native
  //def apply(element: JQuery, transclude: TranscludeFunction, maxPriority: Int): TemplateLinkingFunction = js.native

}

trait CompileProvider extends js.Object {

  def $get: js.Any = js.native

  def directive(name: String, directiveFactory: js.Function): CompileProvider = js.native

  // Undocumented, but it is there...
  def directive(directivesMap: js.Any): CompileProvider = js.native

  def aHrefSanitizationWhitelist(): RegExp = js.native

  def aHrefSanitizationWhitelist(regexp: RegExp): CompileProvider = js.native

  def imgSrcSanitizationWhitelist(): RegExp = js.native

  def imgSrcSanitizationWhitelist(regexp: RegExp): CompileProvider = js.native

  def debugInfoEnabled(enabled: Boolean = true): js.Any = js.native

}

// This corresponds to the "publicLinkFn" returned by $compile.
trait TemplateLinkingFunction extends js.Function {

  def apply(): JQLite = js.native

  def apply(scope: js.Object): JQLite = js.native

  def apply(scope: js.Object, cloneAttachFn: js.Function): JQLite = js.native

}

// This corresponds to $transclude (and also the transclude function passed to link).
trait TranscludeFunction extends js.Function {

  def apply(scope: js.Object = js.native, cloneAttachFn: js.Function = js.native): JQLite = js.native

}