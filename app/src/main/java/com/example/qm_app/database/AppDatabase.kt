package com.example.qm_app.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "crash_log")
data class CrashLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stackTrace: String,
    val deviceInfo: String,
    var uploaded: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
)

@Dao
interface CrashDao {
    @Insert
    fun insert(crashLog: CrashLog)

    @Update
    fun update(crashLog: CrashLog)

    @Delete
    fun delete(crashLog: CrashLog)

    @Query("select * from crash_log where uploaded = 0")
    fun query(): List<CrashLog>
}

@Database(entities = [CrashLog::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun crashDao(): CrashDao

    companion object {
        private lateinit var instance: AppDatabase

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qm_app_crash_log.db"
                ).build()
            }

            return instance
        }
    }
}