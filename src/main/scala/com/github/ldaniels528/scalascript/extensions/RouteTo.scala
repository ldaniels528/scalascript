package com.github.ldaniels528.scalascript.extensions

import scala.scalajs.js

/**
 * Type-Safe AngularJS Route
 */
trait RouteTo extends js.Object {

  /**
   * Controller function that should be associated with the newly created scope or the name of a registered controller.
   */
  var controller: js.Any = js.native

  /**
   * A controller alias name. If present the controller will be published to the scope under this name.
   */
  var controllerAs: String = js.native

  /**
   * Value to update $$location path with and trigger route redirection.
   */
  var redirectTo: js.Any = js.native

  var reloadOnSearch: Boolean = js.native

  /**
   * An optional Map of dependencies which should be injected into the controller.
   */
  var resolve: js.Dictionary[js.Any] = js.native

  /**
   * HTML template as a string or function.
   * If it is a function, it will be called with an array containing the parameters from the current route.
   */
  var template: js.Any = js.native

  /**
   * Path, or function that returns a path to an html template that should be used by ngView.
   * If it is a function, it will be called with an array containing the parameters from the current route.
   */
  var templateUrl: js.Any = js.native

}

/**
 * RouteTo Singleton
 */
object RouteTo {

  def apply(controller: js.Any = null,
            controllerAs: String = null,
            redirectTo: String = null,
            reloadOnSearch: Boolean = true,
            resolve: js.Dictionary[js.Any] = null,
            template: String = null,
            templateUrl: String = null) = {
    val route = new js.Object().asInstanceOf[RouteTo]
    Option(controller).foreach(route.controller = _)
    Option(controllerAs).foreach(route.controllerAs = _)
    Option(resolve).foreach(route.resolve = _)
    Option(redirectTo).foreach(route.redirectTo = _)
    Option(reloadOnSearch).foreach(route.reloadOnSearch = _)
    Option(template).foreach(route.template = _)
    Option(templateUrl).foreach(route.templateUrl = _)
    route
  }

}
