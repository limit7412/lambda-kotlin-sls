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
      val responseBody = SampleResponse(
        "繋ぐレインボー"
      )
      val response = Response(
        200,
        "test"
      )

      "{\"test\": \"get\"}"
    }
    .handler("world") { event ->
//      val request = Json.decodeFromString<APIGatewayRequest>(event)
//      val body = Json.decodeFromString<SampleRequest>(request.body)

      val responseBody = SampleResponse(
//        "津軽レインボー ${Json.encodeToString(body)}"
        "test"
      )
      val response = Response(
        200,
        "test"
      )

      "{\"test\": \"post\"}"
    }
}
