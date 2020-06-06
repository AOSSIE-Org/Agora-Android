package org.aossie.agoraandroid.data.network

import android.util.Log
import org.aossie.agoraandroid.utilities.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class ApiRequest {
  suspend fun<T:Any> apiRequest(call : suspend () -> Response<T>) : T {

    val response = call.invoke()
    if(response.isSuccessful){
      return response.body()!!
    }else{
      val error = response.errorBody().toString()
      val message = StringBuilder()
      error.let {
        try {
          message.append(JSONObject(it).getString("message"))
        }catch (e : JSONException){
        }
        message.append("\n")
      }
      message.append("Error Code is : ${response.code()}")
      throw ApiException(message.toString())
    }
  }
}