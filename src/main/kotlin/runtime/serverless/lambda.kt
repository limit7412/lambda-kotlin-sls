package runtime.serverless

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LambdaAPIGatewayRequest(
  val body: String? = null
)

@Serializable
data class LambdaResponse(
  val statusCode: Int,
  val body: String,
)

@Serializable
data class ErrorResponse(
  val msg: String,
  val error: String,
)

object Lambda {
  inline fun <reified T> handler(name: String, callback: (event: T) -> LambdaResponse): Lambda {
    if (name != System.getenv("_HANDLER").toString()) {
      return this
    }

    val api = System.getenv("AWS_LAMBDA_RUNTIME_API").toString()

    while (true) {
      val response = Http.get("http://$api/2018-06-01/runtime/invocation/next")
      val requestID = response.headers().firstValue("Lambda-Runtime-Aws-Request-Id").get()

      val json = Json {
        ignoreUnknownKeys = true
      }
      try {
        val body = json.decodeFromString<T>(response.body())
        val result = callback(body)
        Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/response", json.encodeToString(result))
      } catch (e: Exception) {
        println(e)

        Http.post(
          "http://$api/2018-06-01/runtime/invocation/$requestID/error",
          json.encodeToString(
            LambdaResponse(
              statusCode=500,
              body= json.encodeToString(
                ErrorResponse(
                  msg="Internal Lambda Error",
                  error=e.message ?: "no error message"
                )
              )
            )
          )
        )
      }
    }
  }
}
