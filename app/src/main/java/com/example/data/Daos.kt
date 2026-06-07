package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionDao {
    @Query("SELECT * FROM nutrition_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getNutritionLogsByDate(date: String): Flow<List<NutritionLog>>

    @Query("SELECT * FROM nutrition_logs ORDER BY timestamp DESC")
    fun getAllNutritionLogs(): Flow<List<NutritionLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(log: NutritionLog)

    @Query("DELETE FROM nutrition_logs WHERE id = :id")
    suspend fun deleteNutritionById(id: Int)
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getExerciseLogsByDate(date: String): Flow<List<ExerciseLog>>

    @Query("SELECT * FROM exercise_logs ORDER BY timestamp DESC")
    fun getAllExerciseLogs(): Flow<List<ExerciseLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(log: ExerciseLog)

    @Query("DELETE FROM exercise_logs WHERE id = :id")
    suspend fun deleteExerciseById(id: Int)
}

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getWaterLogsByDate(date: String): Flow<List<WaterLog>>

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE date = :date")
    fun getDailyWaterSum(date: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(log: WaterLog)

    @Query("DELETE FROM water_logs WHERE id = :id")
    suspend fun deleteWaterById(id: Int)

    @Query("DELETE FROM water_logs WHERE id = (SELECT id FROM water_logs WHERE date = :date ORDER BY timestamp DESC LIMIT 1)")
    suspend fun deleteLastWaterLog(date: String)
}
