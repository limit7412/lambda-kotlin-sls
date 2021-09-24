package runtime.serverless

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//@Serializable
data class APIGatewayRequest(
  val resource: String,
  val path: String,
  val httpMethod: String,
  val headers: Map<String, String>,
  val body: String
)

@Serializable
data class Response(val statusCode: Int = 0, val body: String)

@Serializable
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
        val response = ErrorResponse(
          "Internal Lambda Error",
          e.message ?: "no error message"
        )
        val body = Response(
          500,
          Json.encodeToString(response)
        )
        Http.Post("http://$api/2018-06-01/runtime/invocation/$requestID/error", Json.encodeToString(body))
      }
    }
  }
}
