package runtime.serverless

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object Http {
  suspend fun get(url: String): HttpResponse {
    val client = HttpClient(Curl) {
      engine {
        sslVerify = false
      }
    }

    return client.request(url) {
      method = HttpMethod.Get
    }
  }

  suspend fun post(url: String, body: String): HttpResponse{
    val client = HttpClient(Curl) {
      engine {
        sslVerify = false
      }
    }

    return client.request(url) {
      method = HttpMethod.Post
      headers {
        append(HttpHeaders.ContentType, "application/json;charset=UTF-8")
      }
      setBody(body)
    }
  }
}
