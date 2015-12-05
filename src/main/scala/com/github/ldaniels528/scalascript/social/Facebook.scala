package com.github.ldaniels528.scalascript.social

import com.github.ldaniels528.scalascript.Service
import com.github.ldaniels528.scalascript.core.Q
import com.github.ldaniels528.scalascript.util.ScalaJsHelper._
import org.scalajs.dom.console

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => JS}

/**
  * Facebook Service
  * @author lawrence.daniels@gmail.com
  */
class Facebook($q: Q) extends Service {
  type CallbackObject = js.Function1[js.Dynamic, Unit]
  type PaginationCallback[T] = js.Function1[FacebookPagination[T], Unit]

  // define the API version and App ID properties
  var appID: String = _
  var version = "v2.3"

  // define the Facebook state variables
  private lazy val FB = Facebook.SDK
  var facebookID: String = null
  var auth: AuthResponse = null

  def getLoginStatus: Future[LoginStatusResponse] = {
    val deferred = $q.defer[LoginStatusResponse]()
    FB.getLoginStatus((response: LoginStatusResponse) => {
      if (response.status == "connected") {
        if (isDefined(response.authResponse)) {
          auth = response.authResponse
          facebookID = auth.userID
          deferred.resolve(response)
        }
        else deferred.reject("Facebook response was undefined")
      }
      else deferred.reject(s"Facebook is not connected (status: ${response.status})")
    })
    deferred.promise
  }

  def login(): Future[LoginStatusResponse] = {
    val deferred = $q.defer[LoginStatusResponse]()
    FB.login((response: LoginStatusResponse) => {
      if (isDefined(response.authResponse)) {
        auth = response.authResponse
        facebookID = response.authResponse.userID
        deferred.resolve(response)
      } else {
        deferred.reject("Could not login into Facebook")
      }
    })
    deferred.promise
  }

  def createFriendList(friendListId: String) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.api(s"/$version/me/$friendListId/members&access_token=${auth.accessToken}", { (response: js.Dynamic) =>
      if (isDefined(response) && !isDefined(response.error)) deferred.resolve(response)
      else deferred.reject("Failed to create friends list")
    })
    deferred.promise
  }

  def getFriends(callback: CallbackObject) {
    FB.api(s"/$version/me/friends?access_token=${auth.accessToken}", (response: js.Dynamic) => callback(response))
  }

  def getFriendList(listType: js.UndefOr[String]) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.api(s"/$version/me/friendlists?list_type=${listType getOrElse "close_friends"}&access_token=${auth.accessToken}", (response: js.Dynamic) => {
      if (isDefined(response) && !isDefined(response.error)) deferred.resolve(response)
      else deferred.reject(s"Failed to retrieve friends list (type $listType)")
    })
    deferred.promise
  }

  def getFriendListMembers(friendListId: String) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.api(s"/$version/me/$friendListId/members&access_token=${auth.accessToken}", (response: js.Dynamic) => {
      if (isDefined(response) && !isDefined(response.error)) deferred.resolve(response)
      else deferred.reject("Failed to retrieve friend list members")
    })
    deferred.promise
  }

  /**
    * Retrieves all taggable friends for the authenticated user
    * @return the array of [[TaggableFriend taggable friends]]
    */
  def getTaggableFriends: Future[js.Array[TaggableFriend]] = {
    val deferred = $q.defer[js.Array[TaggableFriend]]()
    val friends = emptyArray[TaggableFriend]
    val callback: PaginationCallback[TaggableFriend] = (response: FacebookPagination[TaggableFriend]) => {
      val results = response.data.asArray[TaggableFriend]
      friends.push(results: _*)
      console.log(s"${friends.length} friend(s) loaded")
      ()
    }
    FB.api(s"/$version/me/taggable_friends?access_token=${auth.accessToken}", { (response: TaggleFriendsResponse) =>
      paginatedResults(response, callback)
      deferred.resolve(friends)
    })
    deferred.promise
  }

  private def paginatedResults[T](response: FacebookPagination[T], callback: PaginationCallback[T]) {
    // perform the callback for this response
    callback(response)

    // if there are more results, recursively extract them
    response.paging.next foreach { url =>
      FB.api(url, (response: FacebookPagination[T]) => paginatedResults(response, callback))
    }
  }

  def getUserProfile: Future[FacebookProfile] = {
    val deferred = $q.defer[FacebookProfile]()
    FB.api(s"/$version/me?access_token=${auth.accessToken}", (response: FacebookProfile) =>
      if (isDefined(response.dynamic.error)) deferred.reject(response.dynamic.error) else deferred.resolve(response)
    )
    deferred.promise
  }

  def logout(): Future[LoginStatusResponse] = {
    val deferred = $q.defer[LoginStatusResponse]()
    FB.logout((response: LoginStatusResponse) => {
      if (isDefined(response)) {
        auth = null
        deferred.resolve(response)
      } else deferred.reject("Error logging out of Facebook")
    })
    deferred.promise
  }

  def feed(caption: String, link: String) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.ui(
      JS(app_id = appID, method = "feed", link = link, caption = caption),
      (response: js.Dynamic) => if (isDefined(response.error)) deferred.reject(response.error) else deferred.resolve(response))
    deferred.promise
  }

  def send(message: String, link: String) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.ui(
      JS(app_id = appID, method = "send", link = link),
      (response: js.Dynamic) => if (isDefined(response.error)) deferred.reject(response.error) else deferred.resolve(response))
    deferred.promise
  }

  def share(link: String) = {
    val deferred = $q.defer[js.Dynamic]()
    FB.ui(
      JS(app_id = appID, method = "share", href = link),
      (response: js.Dynamic) => if (isDefined(response.error)) deferred.reject(response.error) else deferred.resolve(response))
    deferred.promise
  }
}

/**
  * Facebook Singleton
  * @author lawrence.daniels@gmail.com
  */
object Facebook {
  lazy val SDK = js.Dynamic.global.FB.asInstanceOf[js.UndefOr[FacebookSDK]]
    .getOrElse(throw new IllegalStateException("Facebook SDK is not loaded"))

}

/**
  * Facebook SDK
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookSDK extends js.Object {

  def api(url: String, callback: js.Function): Unit

  def getLoginStatus(callback: js.Function1[LoginStatusResponse, Any]): Unit

  def init(config: js.Object): Unit

  def login(callback: js.Function1[LoginStatusResponse, Any]): Unit

  def logout(callback: js.Function1[LoginStatusResponse, Any]): Unit

  def ui(command: js.Dynamic, callback: js.Function): Unit

}

/*
 * Facebook Login Response
 * @author lawrence.daniels@gmail.com
 */
@js.native
trait LoginStatusResponse extends js.Object {
  var authResponse: AuthResponse
  var status: String
}

/*
 * Facebook Login: Auth Response
 * @author lawrence.daniels@gmail.com
 */
@js.native
trait AuthResponse extends js.Object {
  var accessToken: String
  var signedRequest: String
  var userID: String
  var expiresIn: Int
}

/**
  * Represents a Facebook Profile
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookProfile extends js.Object {
  var id: String
  var first_name: String
  var last_name: String
  var name: String
  var gender: String
  var link: String
  var locale: String
  var updated_time: js.Date
  var timezone: Int
  var verified: Boolean
}

/**
  * Facebook Friend Picture
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookPicture extends js.Object {
  var data: FacebookPictureData
}

/**
  * Facebook Friend Picture Data
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookPictureData extends js.Object {
  var is_silhouette: Boolean
  var url: String
}

/**
  * Facebook Taggable Friend
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait TaggableFriend extends js.Object {
  var id: String
  var name: String
  var picture: FacebookPicture
}

/**
  * Facebook Taggable Friends Response
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait TaggleFriendsResponse extends FacebookPagination[TaggableFriend]

/**
  * Facebook Pagination Trait
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookPagination[T] extends js.Object {
  var data: js.Array[T]
  var paging: FacebookPaging
}

/**
  * Facebook Paging Trait
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookPaging extends js.Object {
  var cursors: FacebookCursor
  var previous: js.UndefOr[String]
  var next: js.UndefOr[String]
}

/**
  * Facebook Cursor Trait
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait FacebookCursor extends js.Object {
  var before: js.UndefOr[String]
  var after: js.UndefOr[String]
}


