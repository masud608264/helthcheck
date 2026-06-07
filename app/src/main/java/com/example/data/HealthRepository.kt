package com.example.data

import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val nutritionDao: NutritionDao,
    private val exerciseDao: ExerciseDao,
    private val waterDao: WaterDao
) {
    fun getNutritionLogs(date: String): Flow<List<NutritionLog>> =
        nutritionDao.getNutritionLogsByDate(date)

    fun getAllNutritionLogs(): Flow<List<NutritionLog>> =
        nutritionDao.getAllNutritionLogs()

    suspend fun insertNutrition(log: NutritionLog) {
        nutritionDao.insertNutrition(log)
    }

    suspend fun deleteNutrition(id: Int) {
        nutritionDao.deleteNutritionById(id)
    }

    fun getExerciseLogs(date: String): Flow<List<ExerciseLog>> =
        exerciseDao.getExerciseLogsByDate(date)

    fun getAllExerciseLogs(): Flow<List<ExerciseLog>> =
        exerciseDao.getAllExerciseLogs()

    suspend fun insertExercise(log: ExerciseLog) {
        exerciseDao.insertExercise(log)
    }

    suspend fun deleteExercise(id: Int) {
        exerciseDao.deleteExerciseById(id)
    }

    fun getWaterLogs(date: String): Flow<List<WaterLog>> =
        waterDao.getWaterLogsByDate(date)

    fun getDailyWaterSum(date: String): Flow<Int?> =
        waterDao.getDailyWaterSum(date)

    suspend fun insertWater(log: WaterLog) {
        waterDao.insertWater(log)
    }

    suspend fun deleteWater(id: Int) {
        waterDao.deleteWaterById(id)
    }

    suspend fun deleteLastWater(date: String) {
        waterDao.deleteLastWaterLog(date)
    }
}
