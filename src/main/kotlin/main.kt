import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import runtime.serverless.APIGatewayRequest
import runtime.serverless.Lambda

data class SampleRequest(val msg: String, val test: Int)

fun main() {
  Lambda
    .handler("hello") {
      val bodyNode = JsonNodeFactory.instance.objectNode()
      bodyNode
        .put("msg", "繋ぐレインボー")
      val responseNode = JsonNodeFactory.instance.objectNode()
      responseNode
        .put("statusCode", 200)
        .put("body", bodyNode.toString())

      responseNode.toString()
    }
    .handler("world") { event ->
      val mapper = jacksonObjectMapper()
      val request = mapper.readValue<APIGatewayRequest>(event)
      val body = mapper.readValue<SampleRequest>(request.body)


      val bodyNode = JsonNodeFactory.instance.objectNode()
      bodyNode
        .put("msg", "津軽レインボー ${mapper.writeValueAsString(body)}")
      val responseNode = JsonNodeFactory.instance.objectNode()
      responseNode
        .put("statusCode", 200)
        .put("body", bodyNode.toString())

      responseNode.toString()
    }
}
