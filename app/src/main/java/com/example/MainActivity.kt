package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.data.HealthDatabase
import com.example.data.HealthRepository
import com.example.ui.HealthTrackerScreen
import com.example.ui.HealthViewModel
import com.example.ui.HealthViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Room database singleton and clean repository-to-ViewModel lifecycle binding
        val database = HealthDatabase.getDatabase(this)
        val repository = HealthRepository(
            database.nutritionDao(),
            database.exerciseDao(),
            database.waterDao()
        )
        val factory = HealthViewModelFactory(repository)
        val viewModel by viewModels<HealthViewModel> { factory }

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HealthTrackerScreen(
                        viewModel = viewModel,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
