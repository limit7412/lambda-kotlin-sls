package runtime.serverless

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
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
    val json = Json {
      ignoreUnknownKeys = true
    }

    while (true) {
      // Runtime API のポーリングは Ktor client(suspend) を runBlocking で同期実行する。
      // reified T を使った decode はインライン関数本体で行う必要があるため、
      // suspend なネットワーク呼び出しと JSON 変換のスコープを分離している。
      val (requestID, rawBody) = runBlocking {
        val response = Http.get("http://$api/2018-06-01/runtime/invocation/next")
        val id = response.headers["Lambda-Runtime-Aws-Request-Id"]
        id to response.bodyAsText()
      }

      try {
        val event = json.decodeFromString<T>(rawBody)
        val resultJson = json.encodeToString(callback(event))
        runBlocking {
          Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/response", resultJson)
        }
      } catch (e: Exception) {
        println(e)

        val errorJson = json.encodeToString(
          LambdaResponse(
            statusCode = 500,
            body = json.encodeToString(
              ErrorResponse(
                msg = "Internal Lambda Error",
                error = e.message ?: "no error message"
              )
            )
          )
        )
        runBlocking {
          Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/error", errorJson)
        }
      }
    }
  }
}
