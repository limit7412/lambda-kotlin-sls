package org.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import runtime.serverless.Lambda
import runtime.serverless.LambdaAPIGatewayRequest
import runtime.serverless.LambdaResponse

@Serializable
data class HelloResponse(
  val msg: String,
)

@Serializable
data class WorldResponse(
  val msg: String,
  val body: String,
)

fun main() {
  Lambda
    .handler<LambdaAPIGatewayRequest>("hello") {
        LambdaResponse(
          statusCode=200,
          body=Json.encodeToString(
            HelloResponse(
              msg="繋ぐレインボー"
            )
          )
        )
    }
    .handler<LambdaAPIGatewayRequest>("world") { event ->
      LambdaResponse(
        statusCode=200,
        body=Json.encodeToString(
          WorldResponse(
            msg="津軽レインボー",
            body=event.body.toString()
          )
        )
      )
    }
}
