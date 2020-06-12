package org.aossie.agoraandroid.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.aossie.agoraandroid.data.db.entities.Election

@Dao
interface ElectionsDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveElections( elections : List<Election>)

  @Query("SELECT * FROM Election")
  fun getElections() : LiveData<List<Election>>

  @Query("SELECT * FROM Election WHERE :currentDate < start")
  fun getPendingElections(currentDate: String) : List<Election>

  @Query("SELECT * FROM Election WHERE :currentDate > [end]")
  fun getFinishedElections(currentDate: String) : List<Election>

  @Query("SELECT * FROM Election WHERE :currentDate BETWEEN start AND [end]")
  fun getActiveElections(currentDate: String) : List<Election>
}