package aws

class Lambda {
  fun Hander(name: String, callback: (event: String) -> Unit) {
    callback(name)
  }
}
