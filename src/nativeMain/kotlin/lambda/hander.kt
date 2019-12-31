package lambda

fun hander(name: String, callback: (event: String) -> String) {
  callback(name)
}
