import aws.Lambda

fun main() {
  Lambda()
    .Hander("hello", { event ->
      println("大石泉すき")
      println(event)
    })
    .Hander("world", { event ->
      println("音無小鳥すき")
      println(event)
    })
}
