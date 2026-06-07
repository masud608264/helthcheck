package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ExerciseLog
import com.example.data.NutritionLog
import com.example.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HealthTrackerScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBn by viewModel.isBangla.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val displayDateLabel = viewModel.getDisplayDateLabel()

    // Database states
    val nutritionLogs by viewModel.nutritionLogs.collectAsStateWithLifecycle()
    val exerciseLogs by viewModel.exerciseLogs.collectAsStateWithLifecycle()
    val waterSum by viewModel.totalWaterMl.collectAsStateWithLifecycle()

    // Targets
    val targetIntake by viewModel.calorieIntakeTarget.collectAsStateWithLifecycle()
    val targetBurn by viewModel.calorieBurnedTarget.collectAsStateWithLifecycle()
    val targetWater by viewModel.waterIntakeTargetMl.collectAsStateWithLifecycle()

    // Log Calculations
    val totalConsumed by viewModel.totalCaloriesConsumed.collectAsStateWithLifecycle()
    val totalBurned by viewModel.totalCaloriesBurned.collectAsStateWithLifecycle()
    val totalProtein by viewModel.totalProtein.collectAsStateWithLifecycle()
    val totalCarbs by viewModel.totalCarbs.collectAsStateWithLifecycle()
    val totalFat by viewModel.totalFat.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                // Sleek custom App Bar with local language toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Localizer.get(isBn, "app_name"),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = if (isBn) "আপনার দৈনন্দিন সুস্থতা সহচর" else "Your Daily Wellness Companion",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        )
                    }

                    // Tactile Language Toggle Chip
                    Button(
                        onClick = { viewModel.toggleLanguage() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("language_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "English" else "বাংলা",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Date Picker Widget allowing users to move through time
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.changeDate(-1) },
                            modifier = Modifier.testTag("prev_date_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = Localizer.get(isBn, "prev_date"),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        AnimatedContent(
                            targetState = displayDateLabel,
                            transitionSpec = {
                                slideInHorizontally { width -> if (targetState > initialState) width else -width } + fadeIn() with
                                        slideOutHorizontally { width -> if (targetState > initialState) -width else width } + fadeOut()
                            },
                            label = "dateTransition"
                        ) { date ->
                            Text(
                                text = date,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        IconButton(
                            onClick = { viewModel.changeDate(1) },
                            modifier = Modifier.testTag("next_date_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = Localizer.get(isBn, "next_date"),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple(0, Icons.Default.GridView, Localizer.get(isBn, "home")),
                    Triple(1, Icons.Default.Restaurant, Localizer.get(isBn, "nutrition")),
                    Triple(2, Icons.Default.FitnessCenter, Localizer.get(isBn, "exercise")),
                    Triple(3, Icons.Default.Settings, Localizer.get(isBn, "target_settings"))
                )

                tabs.forEach { (index, icon, label) ->
                    NavigationBarItem(
                        selected = currentTab == index,
                        onClick = { viewModel.selectTab(index) },
                        icon = { Icon(imageVector = icon, contentDescription = label) },
                        label = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        modifier = Modifier.testTag("nav_tab_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            when (currentTab) {
                0 -> SummaryTab(
                    isBn = isBn,
                    targetIntake = targetIntake,
                    totalConsumed = totalConsumed,
                    targetBurn = targetBurn,
                    totalBurned = totalBurned,
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFat = totalFat,
                    waterSum = waterSum,
                    targetWater = targetWater,
                    onAddWater = { viewModel.addWaterLog(250) },
                    onRemoveWater = { viewModel.removeLastWaterLog() }
                )
                1 -> NutritionTab(
                    isBn = isBn,
                    logs = nutritionLogs,
                    presets = viewModel.foodPresets,
                    onAddCustom = { name, cal, p, c, f, meal ->
                        viewModel.addNutritionLog(name, cal, p, c, f, meal)
                        Toast.makeText(context, if (isBn) "খাবারটি যোগ করা হয়েছে!" else "Food Added!", Toast.LENGTH_SHORT).show()
                    },
                    onAddPreset = { viewModel.addNutritionFromPreset(it) },
                    onDelete = { viewModel.deleteNutritionLog(it) }
                )
                2 -> ExerciseTab(
                    isBn = isBn,
                    logs = exerciseLogs,
                    presets = viewModel.exercisePresets,
                    onAddCustom = { name, dur, cal ->
                        viewModel.addExerciseLog(name, dur, cal)
                        Toast.makeText(context, if (isBn) "ব্যায়ামটি রেকর্ড করা হয়েছে!" else "Exercise Logged!", Toast.LENGTH_SHORT).show()
                    },
                    onAddPreset = { viewModel.addExerciseFromPreset(it) },
                    onDelete = { viewModel.deleteExerciseLog(it) }
                )
                3 -> SettingsTab(
                    isBn = isBn,
                    targetIntake = targetIntake,
                    targetBurn = targetBurn,
                    targetWater = targetWater,
                    onSave = { intake, burn, water ->
                        viewModel.setDailyIntakeTarget(intake)
                        viewModel.setDailyBurnedTarget(burn)
                        viewModel.setDailyWaterTarget(water)
                        Toast.makeText(context, if (isBn) "নতুন লক্ষ্যসমূহ সংরক্ষিত হয়েছে!" else "Targets Saved!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

// ------------------------------------------------------------------------
// TAB 0: SUMMARY DASHBOARD
// ------------------------------------------------------------------------
@Composable
fun SummaryTab(
    isBn: Boolean,
    targetIntake: Int,
    totalConsumed: Int,
    targetBurn: Int,
    totalBurned: Int,
    totalProtein: Double,
    totalCarbs: Double,
    totalFat: Double,
    waterSum: Int,
    targetWater: Int,
    onAddWater: () -> Unit,
    onRemoveWater: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High fidelity Health Calorie balance Meter Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Localizer.get(isBn, "daily_summary"),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Beautiful dual ring calorie dial
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(175.dp)
                ) {
                    val intakeProgress = if (targetIntake > 0) totalConsumed.toFloat() / targetIntake.toFloat() else 0f
                    val burnProgress = if (targetBurn > 0) totalBurned.toFloat() / targetBurn.toFloat() else 0f

                    // Canvas Circular Progress Arcs
                    val trackColor = MaterialTheme.colorScheme.surfaceVariant
                    val primaryColor = GreenSecondary
                    val accentColor = AmberAlert

                    Canvas(modifier = Modifier.size(160.dp)) {
                        // Consumed Calorie ring (Outer)
                        drawArc(
                            color = trackColor,
                            startAngle = -220f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = primaryColor,
                            startAngle = -220f,
                            sweepAngle = (260f * intakeProgress.coerceIn(0f, 1f)),
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Burned Calorie ring (Inner)
                        drawArc(
                            color = trackColor.copy(alpha = 0.5f),
                            startAngle = -220f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = accentColor,
                            startAngle = -220f,
                            sweepAngle = (260f * burnProgress.coerceIn(0f, 1f)),
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Inside texts showing Net calorie calculation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val netCal = totalConsumed - totalBurned
                        val caloriesUnit = Localizer.get(isBn, "calories_unit")

                        Text(
                            text = Localizer.formatNumber(netCal, isBn),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = caloriesUnit,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Localizer.get(isBn, "net_calories"),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Balance Details row
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(GreenSecondary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Localizer.get(isBn, "consumed"),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${Localizer.formatNumber(totalConsumed, isBn)} / ${Localizer.formatNumber(targetIntake, isBn)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(AmberAlert)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Localizer.get(isBn, "burned"),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${Localizer.formatNumber(totalBurned, isBn)} / ${Localizer.formatNumber(targetBurn, isBn)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Tactile Water Hydration Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Water",
                            tint = TealWater,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localizer.get(isBn, "water"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    val waterUnit = Localizer.get(isBn, "ml_unit")
                    Text(
                        text = "${Localizer.formatNumber(waterSum, isBn)} / ${Localizer.formatNumber(targetWater, isBn)} $waterUnit",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TealWater
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sparkling Blue Water Glass Indicator rows
                val glassCount = (waterSum / 250).coerceAtLeast(0)
                val targetGlasses = (targetWater / 250).coerceAtLeast(1)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(targetGlasses) { idx ->
                        val isFilled = idx < glassCount
                        Icon(
                            imageVector = if (isFilled) Icons.Default.LocalDrink else Icons.Default.LocalDrink,
                            contentDescription = "Glass",
                            tint = if (isFilled) TealWater else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(horizontal = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Reduce water button
                    OutlinedButton(
                        onClick = onRemoveWater,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("remove_water_button"),
                        border = BorderStroke(1.dp, TealWater),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TealWater),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove water glass",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = Localizer.get(isBn, "remove_glass"), style = MaterialTheme.typography.labelSmall)
                    }

                    // Add water glass button
                    Button(
                        onClick = onAddWater,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("add_water_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = TealWater),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = "Add water glass"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Localizer.get(isBn, "add_glass"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Daily Macro-Nutrient balance Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = Localizer.get(isBn, "nutrition_stats"),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val massUnit = Localizer.get(isBn, "g_unit")

                // Macro Progress Row helper
                MacroProgressRow(
                    label = Localizer.get(isBn, "protein"),
                    amount = totalProtein,
                    target = 65.0, // standard healthy default
                    unit = massUnit,
                    isBn = isBn,
                    color = GreenSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                MacroProgressRow(
                    label = Localizer.get(isBn, "carbs"),
                    amount = totalCarbs,
                    target = 250.0,
                    unit = massUnit,
                    isBn = isBn,
                    color = AmberAlert
                )

                Spacer(modifier = Modifier.height(12.dp))

                MacroProgressRow(
                    label = Localizer.get(isBn, "fat"),
                    amount = totalFat,
                    target = 60.0,
                    unit = massUnit,
                    isBn = isBn,
                    color = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
fun MacroProgressRow(
    label: String,
    amount: Double,
    target: Double,
    unit: String,
    isBn: Boolean,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${Localizer.formatNumber(amount, isBn)} / ${Localizer.formatNumber(target, isBn)} $unit",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        val ratio = (amount / target).toFloat().coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = ratio,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

// ------------------------------------------------------------------------
// TAB 1: NUTRITION LOGS
// ------------------------------------------------------------------------
@Composable
fun NutritionTab(
    isBn: Boolean,
    logs: List<NutritionLog>,
    presets: List<FoodPreset>,
    onAddCustom: (String, Int, Double, Double, Double, String) -> Unit,
    onAddPreset: (FoodPreset) -> Unit,
    onDelete: (Int) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Breakfast") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form to add custom food
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = Localizer.get(isBn, "custom"),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text(Localizer.get(isBn, "food_name")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("food_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text(Localizer.get(isBn, "calories_field")) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("food_calories_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("${Localizer.get(isBn, "protein")} (${Localizer.get(isBn, "g_unit")})") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("food_protein_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("${Localizer.get(isBn, "carbs")} (${Localizer.get(isBn, "g_unit")})") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("food_carbs_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("${Localizer.get(isBn, "fat")} (${Localizer.get(isBn, "g_unit")})") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("food_fat_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Meal category pills selector
                val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mealTypes.forEach { meal ->
                        val translatedMeal = when (meal) {
                            "Breakfast" -> Localizer.get(isBn, "breakfast")
                            "Lunch" -> Localizer.get(isBn, "lunch")
                            "Dinner" -> Localizer.get(isBn, "dinner")
                            else -> Localizer.get(isBn, "snack")
                        }
                        FilterChip(
                            selected = selectedMealType == meal,
                            onClick = { selectedMealType = meal },
                            label = { Text(translatedMeal) },
                            modifier = Modifier.testTag("chip_meal_$meal")
                        )
                    }
                }

                Button(
                    onClick = {
                        val caloriesVal = calories.toIntOrNull()
                        if (foodName.isNotBlank() && caloriesVal != null) {
                            val pVal = protein.toDoubleOrNull() ?: 0.0
                            val cVal = carbs.toDoubleOrNull() ?: 0.0
                            val fVal = fat.toDoubleOrNull() ?: 0.0
                            onAddCustom(foodName, caloriesVal, pVal, cVal, fVal, selectedMealType)
                            // Clear form
                            foodName = ""
                            calories = ""
                            protein = ""
                            carbs = ""
                            fat = ""
                        } else {
                            Toast.makeText(context, Localizer.get(isBn, "enter_food_error"), Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_food_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Localizer.get(isBn, "save"),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Quick Presets Horizon list
        Text(
            text = Localizer.get(isBn, "presets"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(presets) { preset ->
                val displayName = if (isBn) preset.nameBn else preset.nameEn
                val subText = "${Localizer.formatNumber(preset.calories, isBn)} ${Localizer.get(isBn, "calories_unit")}"

                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .clickable { onAddPreset(preset) }
                        .testTag("preset_${preset.nameEn}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "",
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = subText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }

        // List of logged foods
        Text(
            text = Localizer.get(isBn, "nutrition_stats"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 12.dp)
        )

        if (logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Localizer.get(isBn, "no_food_logged"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                logs.forEach { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("food_log_item_${log.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = log.foodName,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                val categoryStr = when (log.mealType) {
                                    "Breakfast" -> Localizer.get(isBn, "breakfast")
                                    "Lunch" -> Localizer.get(isBn, "lunch")
                                    "Dinner" -> Localizer.get(isBn, "dinner")
                                    else -> Localizer.get(isBn, "snack")
                                }
                                Text(
                                    text = categoryStr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                                if (log.protein > 0 || log.carbs > 0 || log.fat > 0) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "P: ${Localizer.formatNumber(log.protein, isBn)}g | C: ${Localizer.formatNumber(log.carbs, isBn)}g | F: ${Localizer.formatNumber(log.fat, isBn)}g",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${Localizer.formatNumber(log.calories, isBn)} ${Localizer.get(isBn, "calories_unit")}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                IconButton(
                                    onClick = { onDelete(log.id) },
                                    modifier = Modifier.testTag("delete_food_${log.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = Localizer.get(isBn, "delete"),
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------
// TAB 2: EXERCISE LOGS
// ------------------------------------------------------------------------
@Composable
fun ExerciseTab(
    isBn: Boolean,
    logs: List<ExerciseLog>,
    presets: List<ExercisePreset>,
    onAddCustom: (String, Int, Int) -> Unit,
    onAddPreset: (ExercisePreset) -> Unit,
    onDelete: (Int) -> Unit
) {
    var activityName by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Custom exercise submission form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = Localizer.get(isBn, "custom"),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )

                OutlinedTextField(
                    value = activityName,
                    onValueChange = { activityName = it },
                    label = { Text(Localizer.get(isBn, "activity_name")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("exercise_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = { durationMinutes = it },
                        label = { Text(Localizer.get(isBn, "duration")) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("exercise_duration_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = caloriesBurned,
                        onValueChange = { caloriesBurned = it },
                        label = { Text(Localizer.get(isBn, "calories_field")) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("exercise_calories_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Button(
                    onClick = {
                        val duration = durationMinutes.toIntOrNull()
                        val calories = caloriesBurned.toIntOrNull()
                        if (activityName.isNotBlank() && duration != null && calories != null) {
                            onAddCustom(activityName, duration, calories)
                            activityName = ""
                            durationMinutes = ""
                            caloriesBurned = ""
                        } else {
                            Toast.makeText(context, Localizer.get(isBn, "enter_exercise_error"), Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_exercise_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Localizer.get(isBn, "save"),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Quick Workouts Presets row
        Text(
            text = Localizer.get(isBn, "presets"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(presets) { preset ->
                val displayName = if (isBn) preset.nameBn else preset.nameEn
                val minUnit = Localizer.get(isBn, "minutes_unit")
                val calUnit = Localizer.get(isBn, "calories_unit")
                val subText = "${Localizer.formatNumber(preset.durationMinutes, isBn)} $minUnit | ${Localizer.formatNumber(preset.caloriesBurned, isBn)} $calUnit"

                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .clickable { onAddPreset(preset) }
                        .testTag("preset_${preset.nameEn}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsRun,
                                contentDescription = "",
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = subText,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }

        // List of logged exercises
        Text(
            text = Localizer.get(isBn, "exercise_stats"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 12.dp)
        )

        if (logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Localizer.get(isBn, "no_exercise_logged"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                logs.forEach { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exercise_log_item_${log.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = log.activityName,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${Localizer.formatNumber(log.durationMinutes, isBn)} ${Localizer.get(isBn, "minutes_unit")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "-${Localizer.formatNumber(log.caloriesBurned, isBn)} ${Localizer.get(isBn, "calories_unit")}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                IconButton(
                                    onClick = { onDelete(log.id) },
                                    modifier = Modifier.testTag("delete_exercise_${log.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = Localizer.get(isBn, "delete"),
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------
// TAB 3: SETTINGS TARGETS
// ------------------------------------------------------------------------
@Composable
fun SettingsTab(
    isBn: Boolean,
    targetIntake: Int,
    targetBurn: Int,
    targetWater: Int,
    onSave: (Int, Int, Int) -> Unit
) {
    var intakeInput by remember(targetIntake) { mutableStateOf(targetIntake.toString()) }
    var burnInput by remember(targetBurn) { mutableStateOf(targetBurn.toString()) }
    var waterInput by remember(targetWater) { mutableStateOf(targetWater.toString()) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = Localizer.get(isBn, "modify_header"),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                OutlinedTextField(
                    value = intakeInput,
                    onValueChange = { intakeInput = it },
                    label = { Text(Localizer.get(isBn, "calorie_target")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_intake_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = burnInput,
                    onValueChange = { burnInput = it },
                    label = { Text(Localizer.get(isBn, "calorie_burned")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_burn_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = waterInput,
                    onValueChange = { waterInput = it },
                    label = { Text("${Localizer.get(isBn, "water_target")} (${Localizer.get(isBn, "ml_unit")})") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_water_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val caloriesIntake = intakeInput.toIntOrNull()
                        val caloriesBurn = burnInput.toIntOrNull()
                        val waterMl = waterInput.toIntOrNull()

                        if (caloriesIntake != null && caloriesBurn != null && waterMl != null) {
                            onSave(caloriesIntake, caloriesBurn, waterMl)
                        } else {
                            Toast.makeText(context, if (isBn) "সবগুলো ঘর সঠিকভাবে পূরণ করুন!" else "Please fill all fields with correct numbers!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_settings_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localizer.get(isBn, "save_settings"),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}
