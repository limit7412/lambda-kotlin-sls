package runtime.serverless

import io.ktor.client.statement.bodyAsText
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.posix.getenv

// Kotlin/Native では System.getenv が使えないため posix の getenv をラップする。
// inline 関数 handler から参照するため @PublishedApi internal で公開する。
@OptIn(ExperimentalForeignApi::class)
@PublishedApi
internal fun env(name: String): String = getenv(name)?.toKString() ?: ""

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
  inline fun <reified T> handler(name: String, crossinline callback: (event: T) -> LambdaResponse): Lambda {
    if (name != env("_HANDLER")) {
      return this
    }

    val api = env("AWS_LAMBDA_RUNTIME_API")
    val json = Json {
      ignoreUnknownKeys = true
    }

    // Runtime API のポーリングループ全体を 1 つの runBlocking で囲み、
    // イテレーション毎にコルーチンイベントループを生成・破棄するオーバーヘッドを避ける。
    // reified T を使った decode も runBlocking のブロックは inline 関数 handler の
    // スコープ内にあるため、ここで直接呼び出せる。
    runBlocking {
      while (true) {
        val response = Http.get("http://$api/2018-06-01/runtime/invocation/next")
        val requestID = response.headers["Lambda-Runtime-Aws-Request-Id"]
        if (requestID == null) {
          // request id が取れない応答は不正なエンドポイントを叩く前にスキップする。
          println("Error: Lambda-Runtime-Aws-Request-Id header is missing")
          continue
        }
        val rawBody = response.bodyAsText()

        try {
          val event = json.decodeFromString<T>(rawBody)
          val resultJson = json.encodeToString(callback(event))
          Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/response", resultJson)
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
          Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/error", errorJson)
        }
      }
    }
  }
}
