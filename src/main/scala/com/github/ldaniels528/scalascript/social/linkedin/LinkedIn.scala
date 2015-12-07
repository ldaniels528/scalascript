package com.github.ldaniels528.scalascript.social.linkedin

import com.github.ldaniels528.scalascript.Service
import com.github.ldaniels528.scalascript.core.Http

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js

/**
  * LinkedIn REST API Service
  * @author lawrence.daniels@gmail.com
  */
class LinkedIn($http: Http) extends Service {
  private var oauth2AccessToken: js.UndefOr[String] = js.undefined

  /**
    * Retrieves user profile information
    * @return the promise of a [[LinkedInBasicProfile profile]]
    */
  def getBasicProfile = $http.get[LinkedInBasicProfile](lnUrl("people/~")) map errorIntercept

  /**
    * Retrieves user profile information (with additional parameters)
    * @return the promise of a [[LinkedInBasicProfile profile]]
    */
  def getAdditionalProfile = $http.get[LinkedInAdditionalProfile](lnUrl("people/~:(id,num-connections,picture-url)")) map errorIntercept

  /**
    * Performs the authentication call to LinkedIn
    * @return the promise of a [[LinkedInAuthResponse authentication response]]
    */
  def login(client_id: String, redirect_uri: String, state: String, scope: String = "r_basicprofile") = {
    val url = s"https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=$client_id&redirect_uri=$redirect_uri&state=$state&scope=$scope"
    $http.get[LinkedInAuthResponse](url) map errorIntercept
  }

  /**
    * Shares a comment
    * @param comment the given [[LinkedInComment comment]]
    * @return a promise of a [[LinkedInResponse response]]
    */
  def shareComment(comment: LinkedInComment) = $http.post[LinkedInResponse](lnUrl("people/~/shares")) map errorIntercept

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  private def errorIntercept[A <: LinkedInResponse](response: A): A = {
    response.message foreach (message => throw new IllegalStateException(message))
    response
  }

  private def lnUrl(baseUrl: String) = s"https://api.linkedin.com/v1/$baseUrl?oauth2_access_token=$oauth2AccessToken&format=json"

}

/**
  * LinkedIn Auth Response
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInAuthResponse extends LinkedInResponse {
  var userID: js.UndefOr[String]
}

/**
  * LinkedIn Additional Profile Information
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInAdditionalProfile extends LinkedInResponse {
  var id: js.UndefOr[String]
  var numConnections: js.UndefOr[Int]
  var pictureUrl: js.UndefOr[String]
}

/**
  * LinkedIn Basic Profile Information
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInBasicProfile extends LinkedInResponse {
  var id: js.UndefOr[String]
  var firstName: js.UndefOr[String]
  var lastName: js.UndefOr[String]
  var headline: js.UndefOr[String]
  var siteStandardProfileRequest: js.UndefOr[LinkedInRequestInfo]
}

/**
  * LinkedIn Comment Information
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInComment extends LinkedInResponse {
  var comment: js.UndefOr[String]
  var visibility: js.UndefOr[LinkedInVisibility]
}

/**
  * LinkedIn Request Information
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInRequestInfo extends js.Object {
  var url: js.UndefOr[String]
}

/**
  * Generic LinkedIn Response
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInResponse extends js.Object {
  var errorCode: js.UndefOr[Int]
  var message: js.UndefOr[String]
  var requestId: js.UndefOr[String]
  var status: js.UndefOr[Int]
  var timestamp: js.UndefOr[js.Date]
}

/**
  * LinkedIn Visibility
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait LinkedInVisibility extends js.Object {
  var code: js.UndefOr[String]
}