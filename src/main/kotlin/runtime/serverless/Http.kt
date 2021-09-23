package runtime.serverless

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object Http {
  fun Get(url: String): HttpResponse<String> {
    var request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .GET()
      .build()
    var response = HttpClient
      .newHttpClient()
      .send(request, HttpResponse.BodyHandlers.ofString())

    return response
  }

  fun Post(url: String, body: String): HttpResponse<String> {
    var request = HttpRequest
      .newBuilder()
      .uri(URI.create(url))
      .headers("Content-Type", "application/json;charset=UTF-8")
      .POST(HttpRequest.BodyPublishers.ofString(body))
      .build()
    var response = HttpClient
      .newHttpClient()
      .send(request, HttpResponse.BodyHandlers.ofString())

    return response
  }
}
