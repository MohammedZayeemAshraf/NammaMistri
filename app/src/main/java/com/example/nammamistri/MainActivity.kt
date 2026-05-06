package com.example.nammamistri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.nammamistri.data.AppDatabase
import com.example.nammamistri.repository.NammaMistriRepository
import com.example.nammamistri.ui.*
import com.example.nammamistri.ui.theme.NAMMAMISTRITheme
import com.example.nammamistri.viewmodel.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: NammaMistriRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "namma_mistri_db"
        ).build()

        repository = NammaMistriRepository(
            database.siteDao(),
            database.workerDao(),
            database.laborEntryDao(),
            database.materialRateDao(),
            database.photoDao()
        )

        // Seed default site if none exists
        kotlinx.coroutines.GlobalScope.launch {
            val sites = repository.getAllSites().first()
            if (sites.isEmpty()) {
                repository.insertSite(com.example.nammamistri.data.Site(name = "Default Site", location = "Local"))
            }
            // Seed default rates
            val rates = repository.getAllMaterialRates().first()
            if (rates.isEmpty()) {
                repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Brick", unit = "piece", rate = 10.0))
                repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Cement", unit = "bag", rate = 400.0))
                repository.insertMaterialRate(com.example.nammamistri.data.MaterialRate(materialName = "Sand", unit = "cubic meter", rate = 1500.0))
            }
        }

        setContent {
            NAMMAMISTRITheme {
                MainScreen(repository)
            }
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