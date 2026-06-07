package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_logs")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: YYYY-MM-DD
    val foodName: String,
    val calories: Int,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val mealType: String = "अन्य", // breakfast, lunch, dinner, snack, other in Bangla/English
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: YYYY-MM-DD
    val activityName: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: YYYY-MM-DD
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)
