import aws.Lambda

fun main() {
  println("大石泉すき")

  val lambda = Lambda()
  lambda.Hander("hello", { event ->
    println("音無小鳥すき")
    println(event)
  })
}
