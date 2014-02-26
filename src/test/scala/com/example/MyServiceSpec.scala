package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MyServiceSpec
  extends Specification
  with Specs2RouteTest
  with ServiceHello
  with ServiceGoodBye {
  def actorRefFactory = system
  
  "MyService" should {

    "return a greeting for GET requests to the hello path" in {
      Get("/hello") ~> helloRoute ~> check {
        val responseAsString = responseAs[String]
        responseAsString must contain("Say hello")
      }
    }

    "return a greeting for GET requests to the bye path" in {
      Get("/bye") ~> goodByeRoute ~> check {
        val responseAsString = responseAs[String]
        responseAsString must contain("Say goodbye")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> helloRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the hello path" in {
      Put("/hello") ~> sealRoute(helloRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
