import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import runtime.serverless.APIGatewayRequest
import runtime.serverless.Lambda
import runtime.serverless.Response

@Serializable
data class SampleRequest(val msg: String, val test: Int)

@Serializable
data class SampleResponse(val msg: String)


fun main() {
  Lambda
    .handler("hello") {
      Json.encodeToString(
        Response(
          200,
          Json.encodeToString(
            SampleResponse(
              "さよなら透明だった僕たち"
            )
          )
        )
      )
    }
    .handler("world") { event ->
      val request = Json.decodeFromString<APIGatewayRequest>(event)
      val body = Json.decodeFromString<SampleRequest>(request.body)

      Json.encodeToString(
        Response(
          200,
          Json.encodeToString(
            SampleResponse(
              "でしょうねミスター・サーバーレス ${Json.encodeToString(body)}"
            )
          )
        )
      )
    }
}
