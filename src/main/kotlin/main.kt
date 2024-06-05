package org.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import runtime.serverless.Lambda
import runtime.serverless.LambdaResponse

@Serializable
data class HelloResponse(
  val msg: String,
)

@Serializable
data class WorldRequest(
  val body: String,
)

@Serializable
data class WorldResponse(
  val msg: String,
  val body: String,
)

fun main() {
  Lambda
    .handler("hello") {
      Json.encodeToString(
        LambdaResponse(
          statusCode=200,
          body=Json.encodeToString(
            HelloResponse(
              msg="繋ぐレインボー"
            )
          )
        )
      )
    }
    .handler("world") { event ->
      val json = Json {
        ignoreUnknownKeys = true
      }
      val request = json.decodeFromString<WorldRequest>(event)

      json.encodeToString(
        LambdaResponse(
          statusCode=200,
          body=json.encodeToString(
            WorldResponse(
              msg="津軽レインボー",
              body=request.body
            )
          )
        )
      )
    }
}
