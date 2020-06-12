package org.aossie.agoraandroid.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.data.db.model.Winner
import java.lang.reflect.Type

class Converters {
  companion object {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): ArrayList<String> {
      val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
      return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromArrayList(list: ArrayList<String?>?): String {
      val gson = Gson()
      return gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    fun fromBallotString(value: String?): ArrayList<Ballot> {
      val listType: Type = object : TypeToken<ArrayList<Ballot?>?>() {}.type
      return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromBallotArrayList(list: ArrayList<Ballot?>?): String {
      val gson = Gson()
      return gson.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromVotersString(value: String?): ArrayList<VoterList> {
      val listType: Type = object : TypeToken<ArrayList<VoterList?>?>() {}.type
      return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromVotersArrayList(list: ArrayList<VoterList?>?): String {
      val gson = Gson()
      return gson.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromWinnersString(value: String?): ArrayList<Winner> {
      val listType: Type = object : TypeToken<ArrayList<Winner?>?>() {}.type
      return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromWinnersArrayList(list: ArrayList<Winner?>?): String {
      val gson = Gson()
      return gson.toJson(list)
    }
  }
}