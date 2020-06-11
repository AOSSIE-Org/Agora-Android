package org.aossie.agoraandroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.aossie.agoraandroid.data.db.dao.UserDao
import org.aossie.agoraandroid.data.db.entities.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  companion object {
    @Volatile
    private var appDatabase: AppDatabase? = null
    private var LOCK = Any()

    operator fun invoke(context: Context) = appDatabase ?: synchronized(LOCK) {
      appDatabase ?: buildDatabase(context).also {
        appDatabase = it
      }
    }

    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
          context,
          AppDatabase::class.java,
          "AgoraAppDatabase.db"
      )
          .fallbackToDestructiveMigration()
          .build()
    }
  }

  abstract fun getUserDao(): UserDao

}