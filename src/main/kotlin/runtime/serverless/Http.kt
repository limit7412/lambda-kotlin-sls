package runtime.serverless

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object Http {
  fun get(url: String): HttpResponse<String> {
    val request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .GET()
      .build()
    val response = HttpClient
      .newHttpClient()
      .send(request, HttpResponse.BodyHandlers.ofString())

    return response
  }

  fun post(url: String, body: String): HttpResponse<String> {
    val request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .headers("Content-Type", "application/json;charset=UTF-8")
      .POST(HttpRequest.BodyPublishers.ofString(body))
      .build()
    val response = HttpClient
      .newHttpClient()
      .send(request, HttpResponse.BodyHandlers.ofString())

    return response
  }
}