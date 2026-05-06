package com.example.nammamistri

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.nammamistri.data.AppDatabase
import com.example.nammamistri.repository.NammaMistriRepository
import com.example.nammamistri.ui.*
import com.example.nammamistri.ui.theme.NAMMAMISTRITheme
import com.example.nammamistri.viewmodel.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: NammaMistriRepository
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        Log.d(TAG, "onCreate: Starting MainActivity initialization")

        try {
            Log.d(TAG, "onCreate: Building Room database on background thread")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    database = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "namma_mistri_db"
                    ).build()
                    Log.d(TAG, "onCreate: Database built successfully")

                    repository = NammaMistriRepository(
                        database.siteDao(),
                        database.workerDao(),
                        database.laborEntryDao(),
                        database.materialRateDao(),
                        database.photoDao()
                    )
                    Log.d(TAG, "onCreate: Repository created successfully")

                    // Seed default site if none exists
                    try {
                        Log.d(TAG, "onCreate: Checking and seeding default data")
                        val sites = repository.getAllSites().first()
                        if (sites.isEmpty()) {
                            Log.d(TAG, "onCreate: Inserting default site")
                            repository.insertSite(com.example.nammamistri.data.Site(name = "Default Site", location = "Local"))
                        }
                        // Seed default rates
                        val rates = repository.getAllMaterialRates().first()
                        if (rates.isEmpty()) {
                            Log.d(TAG, "onCreate: Inserting default rates")
                            repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Brick", unit = "piece", rate = 10.0))
                            repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Cement", unit = "bag", rate = 400.0))
                            repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Sand", unit = "cubic meter", rate = 1500.0))
                        }
                        Log.d(TAG, "onCreate: Data seeding completed successfully")
                    } catch (e: Exception) {
                        Log.e(TAG, "onCreate: Error seeding data", e)
                    }

                    // Set content after database is ready
                    setContent {
                        NAMMAMISTRITheme {
                            MainScreen(repository)
                        }
                    }
                    Log.d(TAG, "onCreate: UI content set successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "onCreate: Error during database initialization", e)
                    e.printStackTrace()
                    // Show error UI
                    setContent {
                        NAMMAMISTRITheme {
                            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                                Text("Error initializing database: ${e.message}", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: Unexpected error", e)
            e.printStackTrace()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(repository: NammaMistriRepository) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Calculator", "Team", "Photos", "Rates")

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> CalculatorScreen(viewModel(factory = CalculatorViewModelFactory(repository)))
            1 -> LaborScreen(viewModel(factory = LaborViewModelFactory(repository)))
            2 -> PhotoScreen(viewModel(factory = PhotoViewModelFactory(repository)))
            3 -> RatesScreen(viewModel(factory = RatesViewModelFactory(repository)))
        }
    }
}

// ViewModel Factories
class CalculatorViewModelFactory(private val repository: NammaMistriRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CalculatorViewModel(repository) as T
    }
}

class LaborViewModelFactory(private val repository: NammaMistriRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return LaborViewModel(repository) as T
    }
}

class PhotoViewModelFactory(private val repository: NammaMistriRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(repository) as T
    }
}

class RatesViewModelFactory(private val repository: NammaMistriRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return RatesViewModel(repository) as T
    }
}