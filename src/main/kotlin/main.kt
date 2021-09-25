import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import runtime.serverless.APIGatewayRequest
import runtime.serverless.Lambda
import runtime.serverless.Response

data class SampleRequest(val msg: String, val test: Int)

data class SampleResponse(val msg: String)

fun main() {
  Lambda
    .handler("hello") {
      val mapper = jacksonObjectMapper()
      val responseBody = SampleResponse(
        "繋ぐレインボー"
      )
      val response = Response(
        200,
        mapper.writeValueAsString(responseBody)
      )

      mapper.writeValueAsString(response)
    }
    .handler("world") { event ->
      val mapper = jacksonObjectMapper()
      val request = mapper.readValue<APIGatewayRequest>(event)
      val body = mapper.readValue<SampleRequest>(request.body)

      val responseBody = SampleResponse(
        "津軽レインボー ${mapper.writeValueAsString(body)}"
      )
      val response = Response(
        200,
        mapper.writeValueAsString(responseBody)
      )

      mapper.writeValueAsString(response)
    }
}
