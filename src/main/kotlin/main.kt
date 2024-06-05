package org.example

//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
import runtime.serverless.Lambda

//@Serializable
//data class HelloResponse(
//  val msg: String,
//)
//
//@Serializable
//data class WorldRequest(
//  val body: String,
//)
//
//@Serializable
//data class WorldResponse(
//  val msg: String,
//  val body: String,
//)


fun main() {
  Lambda
    .handler("hello") {
        "{\"statusCode\":200, \"body\": \"test hello\"}"
//      LambdaResponse(
//        statusCode=200,
//        body=HelloResponse(
//          msg="繋ぐレインボー"
//        ).toString()
//      ).toString()
    }
    .handler("world") { event ->
//      val request = Json.decodeFromString<WorldRequest>(event)
//
//      LambdaResponse(
//        statusCode=200,
//        body=WorldResponse(
//          msg="津軽レインボー",
//          body=request.body
//        ).toString()
//      ).toString()
      "{\"statusCode\":200, \"body\": \"test world\"}"
    }
}
