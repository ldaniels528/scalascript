package com.github.ldaniels528.scalascript.social

import com.github.ldaniels528.scalascript._
import com.github.ldaniels528.scalascript.util.ScalaJsHelper._
import org.scalajs.dom.console
import org.scalajs.jquery._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g, literal => JS}
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success}

/**
  * Facebook Injector
  * @author lawrence.daniels@gmail.com
  */
case class FacebookInjector(elemName: String)(getFacebookAppID: String => String) {
  private lazy val FB = Facebook.SDK

  /**
    * Initializes the Facebook SDK
    */
  g.fbAsyncInit = (() => {
    val hostname = g.location.hostname.as[String]
    console.log(s"Facebook - hostname: $hostname")
    val appId = getFacebookAppID(hostname)
    console.log(s"Initializing Facebook SDK (App ID $appId)...")
    FB.init(JS(
      appId = appId,
      status = true,
      xfbml = true
    ))

    // capture the user ID and access token
    val rootElem = jQuery(elemName)
    val injector = angular.element(rootElem).injector()
    injector.get[Facebook]("Facebook") foreach { facebook =>
      facebook.appID = getFacebookAppID(hostname)
      facebook.getLoginStatus onComplete {
        case Success(response) =>
          console.log("Facebook login successful.")

          // react the the login status
          val $scope = angular.element(rootElem).scope()
          if ($scope != null) {
            console.log("Initiating post-login updates...")
            $scope.dynamic.postLoginUpdates(facebook.facebookID, false)
          }
          else {
            console.log(s"Scope for '$elemName' could not be retrieved")
          }
        case Failure(e) =>
          console.log(s"Facebook Service: ${e.displayMessage}")
      }
    }
    ()
  }): js.Function0[Unit]

  /**
    * Inject the Facebook SDK
    */
  @JSExport
  def init: js.Function0[Unit] = () => inject(g.document)

  /**
    * Injects the Facebook SDK
    * @param fbroot the Facebook root element
    */
  private def inject(fbroot: js.Dynamic) {
    // is the element our script?
    val id = "facebook-jssdk"
    if (!isDefined(fbroot.getElementById(id))) {
      // dynamically create the script
      val fbScript = fbroot.createElement("script")
      fbScript.id = id
      fbScript.async = true
      fbScript.src = "http://connect.facebook.net/en_US/all.js"

      // get the script and insert our dynamic script
      val ref = fbroot.getElementsByTagName("script").asArray[js.Dynamic](0)
      ref.parentNode.insertBefore(fbScript, ref)
    }
    ()
  }

}

