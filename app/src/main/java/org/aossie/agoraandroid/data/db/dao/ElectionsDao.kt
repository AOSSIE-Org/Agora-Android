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

  @Query("SELECT COUNT(*) FROM Election")
  fun getTotalElectionsCount() : LiveData<Int>

  @Query("SELECT COUNT(*) FROM Election WHERE :currentDate < start")
  fun getPendingElectionsCount(currentDate: String) : LiveData<Int>

  @Query("SELECT COUNT(*) FROM Election WHERE :currentDate > [end]")
  fun getFinishedElectionsCount(currentDate: String) : LiveData<Int>

  @Query("SELECT COUNT(*) FROM Election WHERE :currentDate BETWEEN start AND [end]")
  fun getActiveElectionsCount(currentDate: String) : LiveData<Int>
}