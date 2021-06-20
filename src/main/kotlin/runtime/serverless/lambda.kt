package runtime.serverless

class Lambda {
    fun handler(name: String, callback: (event: String) -> String): Lambda {
//    if (name == getenv("_HANDLER").toString()) {
//      return this
//    }

//    val api = getenv("AWS_LAMBDA_RUNTIME_API").toString()
//    val client = HttpClient(Curl) {
//      install(JsonFeature) {
//        serializer = KotlinxSerializer()
//      }
//    }

//    while (true) {
//      val response = client.get<HttpResponse>("http://$api/2018-06-01/runtime/invocation/next")
//      val eventData = response.toString()
//      val requestID = response.headers["lambda-runtime-aws-request-id"]
//
//      try {
        callback(name)
//        val result = callback(eventData)
//        client.post("http://$api/2018-06-01/runtime/invocation/$requestID/response")
//      } catch (e: Exception) {
//        client.post("http://$api/2018-06-01/runtime/invocation/$requestID/error")
//      }
//    }

      return this
    }
}
