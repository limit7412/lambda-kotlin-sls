import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import runtime.serverless.Lambda

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
      val reference = object : TypeReference<Map<String, String>>() {}

      val request = mapper.readValue(event, reference)
      val body = mapper.readValue(request["body"], reference)

      val bodyNode = JsonNodeFactory.instance.objectNode()
      bodyNode
        .put("msg", "津軽レインボー $body")
      val responseNode = JsonNodeFactory.instance.objectNode()
      responseNode
        .put("statusCode", 200)
        .put("body", bodyNode.toString())

      responseNode.toString()
    }
}
