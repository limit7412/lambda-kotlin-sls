package runtime.serverless

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


data class APIGatewayRequest(
  val resource: String,
  val path: String,
  val httpMethod: String,
  val headers: Map<String, String>,
  val body: String
)

data class Response(val statusCode: Int = 0, val body: String)

data class ErrorResponse(val msg: String, val error: String)

object Lambda {
  fun handler(name: String, callback: (event: String) -> String): Lambda {
    if (name != System.getenv("_HANDLER")) {
      return this
    }

    val api = System.getenv("AWS_LAMBDA_RUNTIME_API")

    while (true) {
      val response = Http.Get("http://$api/2018-06-01/runtime/invocation/next")
      val requestID =
        response.headers().firstValue("Lambda-Runtime-Aws-Request-Id").get()

      try {
        val result = callback(response.body())
        Http.Post("http://$api/2018-06-01/runtime/invocation/$requestID/response", result)
      } catch (e: Exception) {
        var mapper = jacksonObjectMapper()
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        val responseBody = ErrorResponse(
          "Internal Lambda Error",
          e.message ?: "no error message"
        )
        val errResponse = Response(
          500,
          mapper.writeValueAsString(responseBody)
        )
        Http.Post(
          "http://$api/2018-06-01/runtime/invocation/$requestID/error",
          mapper.writeValueAsString(errResponse)
        )
      }
    }
  }
}
