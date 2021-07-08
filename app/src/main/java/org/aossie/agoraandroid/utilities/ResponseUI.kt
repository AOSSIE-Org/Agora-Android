package org.aossie.agoraandroid.utilities

class ResponseUI<T> {

  enum class Status {
    ERROR, LOADING, SUCCESS
  }

  var status: Status? = null

  var message: String? = null

  var data: T? = null

  var dataList: List<T>? = null

  constructor(status: Status, data: Any?, message: String?) {
    this.status = status
    this.data = data as T?
    this.message = message
    this.dataList = null
  }

  constructor(status: Status, dataList: List<T>?, message: String?, data: Any?) {
    this.status = status
    this.dataList = dataList
    this.message = message
    this.data = data as T?
  }

  constructor(status: Status) {
    this.status = status
    this.data = null
    this.message = null
    this.dataList = null
  }

  companion object {
    fun <T> success(): ResponseUI<T> {
      return ResponseUI<T>(Status.SUCCESS, null, null)
    }

    fun <T> success(data: T?): ResponseUI<T> {
      return ResponseUI<T>(Status.SUCCESS, data, null)
    }

    fun <T> success(dataList: List<T>?): ResponseUI<T> {
      return ResponseUI<T>(Status.SUCCESS, dataList, null, null)
    }

    fun <T> error(msg: String): ResponseUI<T> {
      return ResponseUI<T>(Status.ERROR, null, msg)
    }

    fun <T> loading(): ResponseUI<T> {
      return ResponseUI(Status.LOADING)
    }

    fun <T> success(msg: String): ResponseUI<T> {
      return ResponseUI<T>(Status.SUCCESS, null, msg)
    }
  }
}
