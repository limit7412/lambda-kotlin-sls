package runtime.serverless

//import kotlinx.serialization.Serializable
//
//@Serializable
//data class LambdaResponse(
//  val statusCode: Int,
//  val body: String,
//)
//
//@Serializable
//data class ErrorResponse(
//  val msg: String,
//  val error: String,
//)

object Lambda {
  inline fun handler(name: String, callback: (event: String) -> String): Lambda {
    if (name != System.getenv("_HANDLER").toString()) {
      return this
    }

    val api = System.getenv("AWS_LAMBDA_RUNTIME_API").toString()

    while (true) {
      val response = Http.get("http://$api/2018-06-01/runtime/invocation/next")
      val requestID = response.headers().firstValue("Lambda-Runtime-Aws-Request-Id").get()

      try {
        val result = callback(response.body())
        Http.post("http://$api/2018-06-01/runtime/invocation/$requestID/response", result)
      } catch (e: Exception) {
        println(e)

        Http.post(
          "http://$api/2018-06-01/runtime/invocation/$requestID/error",
          "{\"statusCode\":500, \"body\": \"test err\"}"
//          LambdaResponse(
//            statusCode=500,
//            body=ErrorResponse(
//              msg="Internal Lambda Error",
//              error=e.message ?: "no error message"
//            ).toString()
//          ).toString()
        )
      }
    }
  }
}
