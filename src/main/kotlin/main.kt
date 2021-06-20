import runtime.serverless.Lambda

fun main() {
  Lambda()
    .handler("hello") { event ->
      println("音無小鳥すき")
      println(event)

      ""
    }
}
