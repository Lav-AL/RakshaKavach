package com.rakshakavach.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rakshakavach.app.data.model.IncidentLog

@Database(entities = [IncidentLog::class], version = 1, exportSchema = false)
abstract class RakshaKavachDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao

    companion object {
        @Volatile
        private var INSTANCE: RakshaKavachDatabase? = null

        fun getDatabase(context: Context): RakshaKavachDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RakshaKavachDatabase::class.java,
                    "raksha_kavach_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
