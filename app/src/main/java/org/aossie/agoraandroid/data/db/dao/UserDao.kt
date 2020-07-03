package org.aossie.agoraandroid.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.aossie.agoraandroid.data.db.entities.CURRENT_USER_ID
import org.aossie.agoraandroid.data.db.entities.User

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(user : User) : Long

  @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
  fun getUser(): LiveData<User>

  @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
  fun getUserInfo(): User

  @Query("DELETE FROM user")
  suspend fun removeUser()

  @Transaction
  suspend fun replace(user: User){
    removeUser()
    insert(user)
  }
}