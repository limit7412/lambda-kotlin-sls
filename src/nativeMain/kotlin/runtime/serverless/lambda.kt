package runtime.serverless

import io.ktor.client.call.*
import kotlinx.cinterop.toKString
import kotlinx.serialization.Serializable
import platform.posix.getenv

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
  suspend inline fun handler(name: String, callback: (event: String) -> String): Lambda {
    if (name != getenv("_HANDLER")?.toKString()) {
      return this
    }

    val api = getenv("AWS_LAMBDA_RUNTIME_API")?.toKString()

    while (true) {
      val response = Http.get("http://$api/2018-06-01/runtime/invocation/next")
      val requestID = response.headers["Lambda-Runtime-Aws-Request-Id"]

      try {
        val result = callback(response.body())
        Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/response", result)
      } catch (e: Exception) {
        println(e)

        Http.post(
          "http://$api/2018-06-01/runtime/invocation/$requestID/error",
          LambdaResponse(
            statusCode=500,
            body=ErrorResponse(
              msg="Internal Lambda Error",
              error=e.message ?: "no error message"
            ).toString()
          ).toString()
        )
      }
    }
  }
}
