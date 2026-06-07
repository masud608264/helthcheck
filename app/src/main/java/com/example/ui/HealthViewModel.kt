package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Formats and localized labels
object Localizer {
    fun get(isBangla: Boolean, key: String): String {
        return if (isBangla) {
            banglaMap[key] ?: key
        } else {
            englishMap[key] ?: key
        }
    }

    private val banglaMap = mapOf(
        "app_name" to "সুস্থতা ট্র্যাকার",
        "home" to "হোম",
        "nutrition" to "পুষ্টি",
        "exercise" to "ব্যায়াম",
        "water" to "পানি",
        "today" to "আজ",
        "yesterday" to "গতকাল",
        "tomorrow" to "আগামীকাল",
        "calories" to "ক্যালরি",
        "calorie_target" to "ক্যালরি লক্ষ্য",
        "calorie_burned" to "ব্যয়কৃত ক্যালরি",
        "water_target" to "পানির লক্ষ্য",
        "target" to "লক্ষ্য",
        "consumed" to "গৃহীত",
        "burned" to "ব্যয়কৃত",
        "remaining" to "অবশিষ্ট",
        "surplus" to "অতিরিক্ত",
        "net_calories" to "নেট ক্যালরি",
        "food_name" to "খাবারের নাম",
        "activity_name" to "ব্যায়ামের নাম",
        "duration" to "সময় (মিনিট)",
        "add_food" to "খাবার যোগ করুন",
        "add_exercise" to "ব্যায়াম যোগ করুন",
        "water_added" to "পানি যোগ করা হয়েছে",
        "add_glass" to "+ ১ গ্লাস (২৫০ মিলি)",
        "remove_glass" to "১ গ্লাস কমান",
        "no_food_logged" to "আজ কোনো খাবার রেকর্ড করা হয়নি",
        "no_exercise_logged" to "আজ কোনো ব্যায়াম রেকর্ড করা হয়নি",
        "presets" to "সহজ তালিকা থেকে যোগ করুন",
        "save" to "সংরক্ষণ করুন",
        "delete" to "মুছুন",
        "custom" to "ম্যানুয়াল যোগ করুন",
        "protein" to "প্রোটিন",
        "carbs" to "কার্বস",
        "fat" to "ফ্যাট",
        "breakfast" to "সকালের খাবার",
        "lunch" to "দুপুরের খাবার",
        "dinner" to "রাতের খাবার",
        "snack" to "নাস্তা",
        "other" to "অন্যান্য",
        "statistics" to "পরিসংখ্যান",
        "daily_summary" to "দৈনিক অগ্রগতি",
        "enter_food_error" to "দয়া করে খাবারের নাম এবং সঠিক ক্যালরি লিখুন!",
        "enter_exercise_error" to "দয়া করে ব্যায়ামের নাম, সঠিক সময় ও ক্যালরি লিখুন!",
        "nutrition_stats" to "খাবার ও পুষ্টি ট্র্যাকার",
        "exercise_stats" to "ব্যায়াম ও ওয়ার্কআউট",
        "target_settings" to "লক্ষ্য পরিবর্তন",
        "save_settings" to "সংরক্ষণ করুন",
        "glass_count" to "গ্লাস",
        "calories_unit" to "কি.ক্যালরি",
        "minutes_unit" to "মিনিট",
        "ml_unit" to "মিলি",
        "lang_toggle" to "English (EN)",
        "lang_toggle_bn" to "বাংলা (BN)",
        "g_unit" to "গ্রাম",
        "prev_date" to "পূর্ববর্তী দিন",
        "next_date" to "পরবর্তী দিন",
        "modify_header" to "নিজের লক্ষ্য ঠিক করুন",
        "calories_field" to "ক্যালরি (কি.ক্যালরি)",
        "close" to "বন্ধ করুন"
    )

    private val englishMap = mapOf(
        "app_name" to "Susthota Tracker",
        "home" to "Home",
        "nutrition" to "Nutrition",
        "exercise" to "Exercise",
        "water" to "Water",
        "today" to "Today",
        "yesterday" to "Yesterday",
        "tomorrow" to "Tomorrow",
        "calories" to "Calories",
        "calorie_target" to "Calorie Target",
        "calorie_burned" to "Calories Burned",
        "water_target" to "Water Target",
        "target" to "Target",
        "consumed" to "Consumed",
        "burned" to "Burned",
        "remaining" to "Remaining",
        "surplus" to "Excess",
        "net_calories" to "Net Calories",
        "food_name" to "Food Name",
        "activity_name" to "Activity Name",
        "duration" to "Duration (min)",
        "add_food" to "Add Food Log",
        "add_exercise" to "Add Exercise Log",
        "water_added" to "Water Logged",
        "add_glass" to "+ 1 Glass (250 ml)",
        "remove_glass" to "Decrease 1 glass",
        "no_food_logged" to "No food items logged today",
        "no_exercise_logged" to "No exercises logged today",
        "presets" to "Add from Quick Presets",
        "save" to "Save Log",
        "delete" to "Delete",
        "custom" to "Add Custom Entry",
        "protein" to "Protein",
        "carbs" to "Carbs",
        "fat" to "Fat",
        "breakfast" to "Breakfast",
        "lunch" to "Lunch",
        "dinner" to "Dinner",
        "snack" to "Snack",
        "other" to "Other",
        "statistics" to "Insights",
        "daily_summary" to "Daily Progress",
        "enter_food_error" to "Please enter food name & valid calories!",
        "enter_exercise_error" to "Please enter activity name, valid duration & calories!",
        "nutrition_stats" to "Food & Nutrition Tracker",
        "exercise_stats" to "Exercise & Workouts",
        "target_settings" to "Settings",
        "save_settings" to "Save Targets",
        "glass_count" to "Glasses",
        "calories_unit" to "kcal",
        "minutes_unit" to "mins",
        "ml_unit" to "ml",
        "lang_toggle" to "English (EN)",
        "lang_toggle_bn" to "বাংলা (BN)",
        "g_unit" to "g",
        "prev_date" to "Previous Day",
        "next_date" to "Next Day",
        "modify_header" to "Set Daily Targets",
        "calories_field" to "Calories (kcal)",
        "close" to "Close"
    )

    private val bnMonths = mapOf(
        "January" to "জানুয়ারি", "February" to "ফেব্রুয়ারি", "March" to "মার্চ", "April" to "এপ্রিল",
        "May" to "মে", "June" to "জুন", "July" to "জুলাই", "August" to "আগস্ট",
        "September" to "সেপ্টেম্বর", "October" to "অক্টোবর", "November" to "নভেম্বর", "December" to "ডিসেম্বর"
    )

    // Formatter to convert numbers to Bangla digits when Bangla is the active language
    fun formatNumber(number: Int, isBangla: Boolean): String {
        if (!isBangla) return number.toString()
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        return number.toString().map { char ->
            if (char in '0'..'9') banglaDigits[char - '0'] else char
        }.joinToString("")
    }

    fun formatNumber(number: Double, isBangla: Boolean): String {
        val str = String.format(Locale.US, "%.1f", number)
        if (!isBangla) return str
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        return str.map { char ->
            when (char) {
                in '0'..'9' -> banglaDigits[char - '0']
                '.' -> '.'
                else -> char
            }
        }.joinToString("")
    }

    fun getDisplayDate(dateStr: String, isBangla: Boolean, dateFormat: SimpleDateFormat): String {
        val todayStr = dateFormat.format(Date())
        
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dateFormat.format(cal.time)
        
        cal.add(Calendar.DAY_OF_YEAR, 2)
        val tomorrowStr = dateFormat.format(cal.time)

        return when (dateStr) {
            todayStr -> get(isBangla, "today")
            yesterdayStr -> get(isBangla, "yesterday")
            tomorrowStr -> get(isBangla, "tomorrow")
            else -> {
                try {
                    val parsedDate = dateFormat.parse(dateStr)
                    if (parsedDate != null) {
                        val outFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.US)
                        val formatted = outFormat.format(parsedDate) // e.g. "28 May, 2026"
                        if (isBangla) {
                            var bnDate = formatted
                            // Translate month names
                            bnMonths.forEach { (enMonth, bnMonth) ->
                                bnDate = bnDate.replace(enMonth, bnMonth)
                            }
                            // Translate digits
                            val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
                            bnDate.map { char ->
                                if (char in '0'..'9') banglaDigits[char - '0'] else char
                            }.joinToString("")
                        } else {
                            formatted
                        }
                    } else {
                        dateStr
                    }
                } catch (e: Exception) {
                    dateStr
                }
            }
        }
    }
}

// Preset Models
data class FoodPreset(
    val nameBn: String,
    val nameEn: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealTypeBn: String,
    val mealTypeEn: String
)

data class ExercisePreset(
    val nameBn: String,
    val nameEn: String,
    val caloriesBurned: Int,
    val durationMinutes: Int
)

class HealthViewModel(private val repository: HealthRepository) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Language setting (true = Bangla, false = English)
    val isBangla = MutableStateFlow(true)

    // Calendar selected date
    val selectedDate = MutableStateFlow(dateFormat.format(Date()))

    // Setting Targets state
    val calorieIntakeTarget = MutableStateFlow(2000)
    val calorieBurnedTarget = MutableStateFlow(400)
    val waterIntakeTargetMl = MutableStateFlow(2000) // 2000 ml ~ 8 glasses

    // Selected tab: 0 = summary, 1 = nutrition, 2 = exercise, 3 = settings
    val currentTab = MutableStateFlow(0)

    // Preset data for Bangla & English toggle
    val foodPresets = listOf(
        FoodPreset("সাদা ভাত (১ বাটি)", "White Rice (1 Bowl)", 200, 4.0, 44.0, 0.4, "দুপুরের খাবার", "Lunch"),
        FoodPreset("মসুর ডাল (১ বাটি)", "Lentil Soup (1 Bowl)", 120, 8.0, 20.0, 1.0, "দুপুরের খাবার", "Lunch"),
        FoodPreset("মুরগির মাংস (১ টুকরা)", "Chicken Curry (1 Piece)", 180, 22.0, 3.0, 9.0, "দুপুরের খাবার", "Lunch"),
        FoodPreset("ডিম ভাজি (১টি)", "Fried Egg (1 pc)", 120, 6.0, 1.0, 10.0, "সকালের খাবার", "Breakfast"),
        FoodPreset("সেদ্ধ ডিম (১টি)", "Boiled Egg (1 pc)", 75, 6.0, 0.6, 5.0, "সকালের খাবার", "Breakfast"),
        FoodPreset("লাল আটার রুটি (১টি)", "Wheat Roti (1 pc)", 85, 3.0, 18.0, 0.5, "সকালের খাবার", "Breakfast"),
        FoodPreset("রুই মাছের ঝোল (১ টুকরা)", "Rui Fish Curry (1 pc)", 150, 18.0, 2.0, 8.0, "দুপুরের খাবার", "Lunch"),
        FoodPreset("পাকা কলা (১টি)", "Ripe Banana (1 pc)", 105, 1.3, 27.0, 0.3, "নাস্তা", "Snack"),
        FoodPreset("লাল আপেল (১টি)", "Red Apple (1 pc)", 95, 0.5, 25.0, 0.3, "নাস্তা", "Snack"),
        FoodPreset("গরুর খাঁটি দুধ (১ গ্লাস)", "Cow's Milk (1 Glass)", 150, 8.0, 12.0, 8.0, "নাস্তা", "Snack")
    )

    val exercisePresets = listOf(
        ExercisePreset("সাধারণ হাঁটা", "Normal Walking", 120, 30),
        ExercisePreset("দ্রুত জুতা পায়ে হাঁটা", "Brisk Walk", 180, 30),
        ExercisePreset("দৌড়ানো", "Running", 220, 20),
        ExercisePreset("ফ্রি-হ্যান্ড ব্যায়াম", "Calisthenics", 150, 30),
        ExercisePreset("সাইকেল চালানো", "Bicycle Riding", 200, 30),
        ExercisePreset("যোগব্যায়াম / ইয়োগা", "Yoga Session", 90, 30),
        ExercisePreset("দড়ি লাফ", "Rope Skipping", 180, 15),
        ExercisePreset("জিম ওয়ার্কআউট", "Gym Workout", 250, 45)
    )

    // Observe active logs for the selected date
    @OptIn(ExperimentalCoroutinesApi::class)
    val nutritionLogs: StateFlow<List<NutritionLog>> = selectedDate
        .flatMapLatest { date -> repository.getNutritionLogs(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val exerciseLogs: StateFlow<List<ExerciseLog>> = selectedDate
        .flatMapLatest { date -> repository.getExerciseLogs(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalWaterMl: StateFlow<Int> = selectedDate
        .flatMapLatest { date -> repository.getDailyWaterSum(date) }
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Computed Daily summaries
    val totalCaloriesConsumed: StateFlow<Int> = nutritionLogs
        .map { logs -> logs.sumOf { it.calories } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCaloriesBurned: StateFlow<Int> = exerciseLogs
        .map { logs -> logs.sumOf { it.caloriesBurned } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalProtein: StateFlow<Double> = nutritionLogs
        .map { logs -> logs.sumOf { it.protein } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalCarbs: StateFlow<Double> = nutritionLogs
        .map { logs -> logs.sumOf { it.carbs } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalFat: StateFlow<Double> = nutritionLogs
        .map { logs -> logs.sumOf { it.fat } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Actions
    fun changeDate(offsetDays: Int) {
        viewModelScope.launch {
            try {
                val current = dateFormat.parse(selectedDate.value) ?: Date()
                val cal = Calendar.getInstance()
                cal.time = current
                cal.add(Calendar.DAY_OF_YEAR, offsetDays)
                selectedDate.value = dateFormat.format(cal.time)
            } catch (e: Exception) {
                // fall back
            }
        }
    }

    fun toggleLanguage() {
        isBangla.value = !isBangla.value
    }

    fun selectTab(tabIndex: Int) {
        currentTab.value = tabIndex
    }

    fun addNutritionLog(foodName: String, calories: Int, protein: Double = 0.0, carbs: Double = 0.0, fat: Double = 0.0, mealType: String = "Breakfast") {
        viewModelScope.launch {
            repository.insertNutrition(
                NutritionLog(
                    date = selectedDate.value,
                    foodName = foodName,
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fat = fat,
                    mealType = mealType
                )
            )
        }
    }

    fun addNutritionFromPreset(preset: FoodPreset) {
        val mealType = if (isBangla.value) preset.mealTypeBn else preset.mealTypeEn
        val name = if (isBangla.value) preset.nameBn else preset.nameEn
        addNutritionLog(
            foodName = name,
            calories = preset.calories,
            protein = preset.protein,
            carbs = preset.carbs,
            fat = preset.fat,
            mealType = mealType
        )
    }

    fun deleteNutritionLog(id: Int) {
        viewModelScope.launch {
            repository.deleteNutrition(id)
        }
    }

    fun addExerciseLog(activityName: String, durationMinutes: Int, caloriesBurned: Int) {
        viewModelScope.launch {
            repository.insertExercise(
                ExerciseLog(
                    date = selectedDate.value,
                    activityName = activityName,
                    durationMinutes = durationMinutes,
                    caloriesBurned = caloriesBurned
                )
            )
        }
    }

    fun addExerciseFromPreset(preset: ExercisePreset) {
        val name = if (isBangla.value) preset.nameBn else preset.nameEn
        addExerciseLog(
            activityName = name,
            durationMinutes = preset.durationMinutes,
            caloriesBurned = preset.caloriesBurned
        )
    }

    fun deleteExerciseLog(id: Int) {
        viewModelScope.launch {
            repository.deleteExercise(id)
        }
    }

    fun addWaterLog(amountMl: Int = 250) {
        viewModelScope.launch {
            repository.insertWater(
                WaterLog(
                    date = selectedDate.value,
                    amountMl = amountMl
                )
            )
        }
    }

    fun removeLastWaterLog() {
        viewModelScope.launch {
            repository.deleteLastWater(selectedDate.value)
        }
    }

    fun setDailyIntakeTarget(calories: Int) {
        calorieIntakeTarget.value = calories.coerceAtLeast(100)
    }

    fun setDailyBurnedTarget(calories: Int) {
        calorieBurnedTarget.value = calories.coerceAtLeast(10)
    }

    fun setDailyWaterTarget(waterMl: Int) {
        waterIntakeTargetMl.value = waterMl.coerceAtLeast(250)
    }

    fun getDisplayDateLabel(): String {
        return Localizer.getDisplayDate(selectedDate.value, isBangla.value, dateFormat)
    }
}

class HealthViewModelFactory(private val repository: HealthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
