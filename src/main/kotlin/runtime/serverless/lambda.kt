package runtime.serverless

import com.fasterxml.jackson.databind.node.JsonNodeFactory

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
        println(e)
        val bodyNode = JsonNodeFactory.instance.objectNode()
        bodyNode
          .put("msg", "Internal Lambda Error")
          .put("error", e.message ?: "no error message")
        val responseNode = JsonNodeFactory.instance.objectNode()
        responseNode
          .put("statusCode", 500)
          .put("body", bodyNode.toString())

        Http.Post(
          "http://$api/2018-06-01/runtime/invocation/$requestID/error",
          responseNode.toString()
        )
      }
    }
  }
}
