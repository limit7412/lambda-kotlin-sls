package runtime.serverless

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

object Http {
  private val client = HttpClient(CIO)

  suspend fun get(url: String): HttpResponse = client.get(url)

  suspend fun post(url: String, body: String): HttpResponse =
    client.post(url) {
      contentType(ContentType.parse("application/json;charset=UTF-8"))
      setBody(body)
    }
}
