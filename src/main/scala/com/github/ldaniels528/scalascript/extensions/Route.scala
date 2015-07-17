package com.github.ldaniels528.scalascript.extensions

import scala.scalajs.js

/**
 * Route Service
 */
trait Route extends js.Object {

  /**
   * Controller function that should be associated with the newly created scope or the name of a registered controller.
   */
  var controller: js.Object = js.native

  /**
   * A controller alias name. If present the controller will be published to the scope under this name.
   */
  var controllerAs: String = js.native

  /**
   * Value to update $$location path with and trigger route redirection.
   */
  var redirectTo: js.Object = js.native

  /**
   * An optional Map of dependencies which should be injected into the controller.
   */
  var resolve: js.Dictionary[js.Object] = js.native

  /**
   * HTML template as a string or function.
   * If it is a function, it will be called with an array containing the parameters from the current route.
   */
  var template: js.Object = js.native

  /**
   * Path, or function that returns a path to an html template that should be used by ngView.
   * If it is a function, it will be called with an array containing the parameters from the current route.
   */
  var templateUrl: js.Object = js.native

}

/**
 * Route Service Singleton
 */
object Route {

  def apply(controller: String = null,
            controllerFn: js.Function = null,
            controllerAs: String = null,
            resolve: js.Dictionary[js.Object] = null,
            redirectTo: String = null,
            redirectToFn: js.Function3[js.Array[Any], String, js.Object, Unit] = null,
            reloadOnSearch: Boolean = true,
            template: String = null,
            templateFn: js.Function1[js.Array[Any], String] = null,
            templateUrl: String = null,
            templateUrlFn: js.Function1[js.Array[Any], String] = null): Route = {
    val r = js.Dictionary[Any]()
    if (controller != null) r("controller") = controller
    if (controllerFn != null) r("controller") = controllerFn
    if (controllerAs != null) r("controllerAs") = controllerAs
    if (resolve != null) r("resolve") = resolve
    if (redirectTo != null) r("redirectTo") = redirectTo
    if (redirectToFn != null) r("redirectTo") = redirectToFn
    r("reloadOnSearch") = reloadOnSearch
    if (template != null) r("template") = template
    if (templateFn != null) r("template") = templateFn
    if (templateUrl != null) r("templateUrl") = templateUrl
    if (templateUrlFn != null) r("templateUrl") = templateUrlFn
    r.asInstanceOf[Route]
  }
}

/**
 * Route Provider
 */
trait RouteProvider extends js.Object {

  def when(path: String, route: Route): RouteProvider = js.native

  def otherwise(params: Route): RouteProvider = js.native

}