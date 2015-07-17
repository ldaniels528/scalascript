ScalaScript
=============
A Type-safe Scala.js Binding for AngularJS 

## Introduction

The goal of ScalaScript is to be more than just a type-safe Scala.js binding for AngularJS 1.4.x. ScalaScript goes to 
great lengths to make all the things you love about writing Scala on the back-end, to the front-end. To that end, various
refinements have been added to make the transition more seamless.

## Defining a Module

```scala
val module = angular.createModule("shocktrade",
  js.Array("ngAnimate", "ngCookies", "ngRoute", "ngSanitize", "nvd3ChartDirectives", "toaster", "ui.bootstrap"))

// add the custom directives
module.directiveOf[AvatarDirective]("avatar")
module.directiveOf[ChangeArrowDirective]("changearrow")

// add the custom filters
module.filter("duration", Filters.duration)
module.filter("yesno", Filters.yesNo)

// add the controllers and services
module.controllerOf[DiscoverController]("DiscoverController")
module.serviceOf[FacebookService]("Facebook")
module.serviceOf[MySession]
module.serviceOf[QuoteService]("QuoteService")

// define the routes
module.config({ ($routeProvider: RouteProvider) =>
  $routeProvider
    .when("/about/us", Route(templateUrl = "/assets/views/about/us.htm"))
    .when("/discover", Route(templateUrl = "/assets/views/discover/discover.htm", controller = "DiscoverController"))
    .when("/discover/:symbol", Route(templateUrl = "/assets/views/discover/discover.htm", controller = "DiscoverController"))
    .otherwise(Route(redirectTo = "/about/us"))
})

// initialize the application
module.run({ (WebSocketService: WebSocketService) =>
  // inject Facebook's JavaScript SDK
  FacebookInjector.init()

  // initialize the web socket service
  WebSocketService.init()
})
```

## ScalaScript Examples

### Filter Example

```scala
val yesNo: js.Function = () => ((state: Boolean) => if (state) "Yes" else "No"): js.Function

module.filter("yesno", yesNo)
```

### Controller Example 1: Js.Dynamic Impelementation

```scala
module.controllerOf[AwardsController]("AwardsCtrl")

class AwardsController($scope: js.Dynamic, $http: Http, @injected("MySession") mySession: MySession) 
    extends Controller {

  $scope.getAwards = () => AvailableAwards

  $scope.getMyAwards = () => getMyAwards

  $scope.getAwardImage = (code: String) => AwardIconsByCode.get(code).orNull

  $scope.setupAwards = () => setupAwards()
    
  private def getMyAwards: js.Array[js.Dynamic] = {
    mySession.getMyAwards() map (code => AwardsByCode.get(code).orNull)
  }

  private def setupAwards() {
    console.log("Setting up awards....")
    AvailableAwards foreach { award =>
      award.owned = mySession.getMyAwards().contains(award.code.as[String])
    }
  }
}
```

In the example above, we're using a Scala.js JSON literal as our scope. While this is very flexible, it's not at all 
type-safe. In addition, most IDE's will be unable to provide any useful type inference information. 

### Controller Example 2: Type-Safe Implementation

```scala
class AwardsController($scope: Scope, $http: Http, @injected("MySession") mySession: MySession)
  extends Controller {

  ///////////////////////////////////////////////////////////////////////////
  //          Public Functions
  ///////////////////////////////////////////////////////////////////////////

  @scoped def getAwards = Award.AvailableAwards map { award =>
    val myAward = award.asInstanceOf[MyAward]
    myAward.owned = mySession.getMyAwards.contains(award.code)
    myAward
  } sortBy (_.owned) reverse

  @scoped def getAwardImage(code: String) = AwardIconsByCode.get(code).orNull

  @scoped def getMyAwards = mySession.getMyAwards map (code => AwardsByCode.get(code).orNull)

}

trait Award extends js.Object {
  var name: String = js.native
  var code: String = js.native
  var icon: String = js.native
  var description: String = js.native
}

trait MyAward extends Award {
  var owned: Boolean = js.native
}
```

In the example above, we using a generic scope object because we don't need to specify any custom scope variables, and 
we're using the `@scope` macro annotation to attach our methods to the `$scope` variable. 

### Modal Dialog &#8212; Controller and Service

```scala
class InvitePlayerDialog($http: Http, $modal: Modal) extends Service {
  def popup(participant: Participant): Future[InvitePlayerDialogResult] = {
    val modalInstance = $modal.open[InvitePlayerDialogResult](ModalOptions(
      templateUrl = "invite_player_dialog.htm",
      controllerClass = classOf[InvitePlayerDialogController]
    ))
    modalInstance.result
  }
}

class InvitePlayerDialogController($scope: InvitePlayerScope, $modalInstance: ModalInstance[InvitePlayerDialogResult],
                                   @injected("MySession") mySession: MySession)
  extends Controller {

  private val myFriends = mySession.fbFriends
  $scope.invites = emptyArray[TaggableFriend]

  @scoped def getFriends = myFriends

  @scoped def getInvitedCount = $scope.invites.count(invitee => isDefined(invitee))

  @scoped def getInvites = $scope.invites

  @scoped def ok() = $modalInstance.close(getSelectedFriends)

  @scoped def cancel() = $modalInstance.dismiss("cancel")

  private def getSelectedFriends = {
    val selectedFriends = emptyArray[TaggableFriend]
    for (n <- 0 to $scope.invites.length) {
      if (isDefined($scope.invites(n))) selectedFriends.push(myFriends(n))
    }
    selectedFriends
  }
}

trait InvitePlayerScope extends Scope {
  var invites: js.Array[TaggableFriend] = js.native
}

object InvitePlayerDialogController {
  type InvitePlayerDialogResult = js.Array[TaggableFriend]
}
```

### Service Example

```scala
module.serviceOf[MarketStatusService]("MarketStatusSvc")

class MarketStatusService($http: Http) extends Service {

  def getMarketStatus(implicit ec: ExecutionContext): Future[MarketStatus] = {
    $http.get[MarketStatus]("/api/tradingClock/status")
  }
  
}

trait MarketStatus extends js.Object {
  var stateChanged: Boolean = js.native
  var active: Boolean = js.native
  var sysTime: Double = js.native
  var delay: Double = js.native
  var start: Double = js.native
  var end: Double = js.native
}
```

### Directive Example

```scala
module.directiveOf[ChangeArrowDirective]("changearrow")

class ChangeArrowDirective extends Directive[ChangeArrowDirectiveScope] {
  override val restrict = "E"
  override val scope = JS(value = "@value")
  override val transclude = true
  override val replace = false
  override val template = """<i ng-class="icon"></i>"""

  override def link(scope: ChangeArrowDirectiveScope, element: JQLite, attrs: Attributes) = {
    scope.$watch("value", { (newValue: js.UndefOr[Any], oldValue: js.UndefOr[Any]) =>
      scope.icon = newValue.toOption flatMap getNumericValue map {
        case v if v > 0 => "fa fa-arrow-up positive"
        case v if v < 0 => "fa fa-arrow-down negative"
        case _ => "fa fa-minus null"
      } orNull
    })
  }

  private def getNumericValue(newValue: Any): Option[Double] = newValue match {
    case n: Number => Some(n.doubleValue)
    case s: String if s.nonEmpty => Try(s.toDouble).toOption
    case _ => None
  }
}

trait ChangeArrowDirectiveScope extends Scope {
  var value: js.UndefOr[Any] = js.native
  var icon: String = js.native
}

object ChangeArrowDirectiveScope {
  def apply(): ChangeArrowDirectiveScope = {
    val scope = new js.Object().asInstanceOf[ChangeArrowDirectiveScope]
    scope.icon = null
    scope
  }
}
```

## The Refinements

### Durations

ScalaScript provides implicit conversions so that you may use `scala.concurrent.duration.FiniteDuration`s with `$timeout`,
`$interval`, and any other services that use time in milliseconds.

```scala
import com.ldaniels528.scalascript.core.TimerConversions._

$timeout(() => doSomething(), 5.minutes)
```

### JSON data as case-classes

ScalaScript allows you to utilize dynamic JavaScript objects or type-safe Scala objects using traits. 
Consider the following example:

```scala
$http.get[js.Dynamic]("/api/tradingClock/status") onComplete {
    case Success(obj) => console.log(angular.toJson(obj))
    case Failure(e) => ...
}
```

Above we retrieve a JSON object (see below) in much the same fashion as you would using native JavaScript. Sometimes
one may want to retrieve the data as a `js.Dynamic` because of the flexibility it offers.

```json
{"stateChanged":false,"active":false,"sysTime":1392092448795,"delay":-49848795,"start":1392042600000,"end":1392066000000}
```

However, sometimes we instead want to retrieve the data as a type-safe Scala object. ScalaScript makes this as simple as:

```scala
$http.get[MarketStatus]("/api/tradingClock/status") onComplete {
    case Success(status) => ...
    case Failure(e) => ...
}

trait MarketStatus extends js.Object {
    var stateChanged: Boolean = js.native
    var active: Boolean = js.native
    var sysTime: Double = js.native
    var delay: Double = js.native
    var start: Double = js.native
    var end: Double = js.native
}
```

Above, we retrieve a JSON object via the given API and return a Market Status object. *NOTE:* The fact 
that `MarketStatus` extends `js.Object` is significant.

### For Comprehensions

ScalaScript provides implicit conversions that convert a `HttpResponse[T]` into a `Future[T]`, we can also use `for`
comprehensions when we need to combine data from multiple API calls.

```scala
val outcome = for {
   symbols <- contestService.getHeldSecurities(playerId)
   quotes <- quoteService.getStockQuoteList(symbols)
 } yield quotes
```

The above code uses Scala's for comprehension to first retrieve a collection of symbols (via REST) and then use the 
symbols to then retrieve a collection of quotes (also via REST). 

To do the same in JavaScript would've required creating 
multiple promises (via $q) or ugly callbacks, and then there's the results gathering/error handling... which would also 
be more painful in JavaScript. 

Here's how it's handled via Scala.js:

```scala
outcome onComplete {
  case Success(updatedQuotes) =>
    obj.quotes = updatedQuotes
  case Failure(e) =>
    toaster.error(s"Failed to load Held Securities")
    console.error(s"Failed to load Held Securities: ${e.getMessage}")
}
```