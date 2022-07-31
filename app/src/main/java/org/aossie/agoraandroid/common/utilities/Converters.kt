package org.aossie.agoraandroid.common.utilities

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.aossie.agoraandroid.data.remote.dto.BallotDto
import org.aossie.agoraandroid.data.remote.dto.VotersDto
import org.aossie.agoraandroid.data.remote.dto.WinnerDto

class Converters {
  companion object {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): List<String> {
      val type = Types.newParameterizedType(List::class.java, String::class.java)
      val adapter = Moshi.Builder().build().adapter<List<String>>(type)
      return adapter.fromJson(value ?: "") ?: listOf()
    }

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<String>?): String {
      val type = Types.newParameterizedType(List::class.java, String::class.java)
      val adapter = Moshi.Builder().build().adapter<List<String>>(type)
      return adapter.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun fromBallotString(value: String?): List<BallotDto> {
      val type = Types.newParameterizedType(List::class.java, BallotDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<BallotDto>>(type)
      return adapter.fromJson(value ?: "") ?: listOf()
    }

    @TypeConverter
    @JvmStatic
    fun fromBallotList(list: List<BallotDto>?): String {
      val type = Types.newParameterizedType(List::class.java, BallotDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<BallotDto>>(type)
      return adapter.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromVotersString(value: String?): List<VotersDto> {
      val type = Types.newParameterizedType(List::class.java, VotersDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<VotersDto>>(type)
      return adapter.fromJson(value ?: "") ?: listOf()
    }

    @TypeConverter
    @JvmStatic
    fun fromVotersList(list: List<VotersDto>?): String {
      val type = Types.newParameterizedType(List::class.java, VotersDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<VotersDto>>(type)
      return adapter.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromWinnersString(value: String?): List<WinnerDto> {
      val type = Types.newParameterizedType(List::class.java, WinnerDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<WinnerDto>>(type)
      return adapter.fromJson(value ?: "") ?: listOf()
    }

    @TypeConverter
    @JvmStatic
    fun fromWinnersList(list: List<WinnerDto>?): String {
      val type = Types.newParameterizedType(List::class.java, WinnerDto::class.java)
      val adapter = Moshi.Builder().build().adapter<List<WinnerDto>>(type)
      return adapter.toJson(list)
    }
  }
}
