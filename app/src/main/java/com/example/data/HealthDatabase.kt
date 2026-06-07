package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NutritionLog::class, ExerciseLog::class, WaterLog::class],
    version = 1,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun nutritionDao(): NutritionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_tracker_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
