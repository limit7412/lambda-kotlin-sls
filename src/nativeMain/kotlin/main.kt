import aws.Lambda

fun main() {
  Lambda()
    .Hander("hello", { event ->
      println("音無小鳥すき")
      println(event)
    })
}
