import lambda.*

fun main() {
  hander("test", { event ->
    println("音無小鳥すき")
    event
  })
}
