package com.example.nammamistri

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.nammamistri.data.AppDatabase
import com.example.nammamistri.repository.NammaMistriRepository
import com.example.nammamistri.ui.*
import com.example.nammamistri.ui.theme.NAMMAMISTRITheme
import com.example.nammamistri.viewmodel.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private val TAG = "MainActivity"
    private val repositoryState = mutableStateOf<NammaMistriRepository?>(null)
    private val initError = mutableStateOf<String?>(null)
    private val splashFinished = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d(TAG, "onCreate: Starting MainActivity initialization")

        setContent {
            NAMMAMISTRITheme {
                when {
                    initError.value != null && splashFinished.value -> ErrorScreen(initError.value!!)
                    !splashFinished.value || repositoryState.value == null -> SplashScreen(onSplashFinished = { splashFinished.value = true })
                    else -> MainScreen(repositoryState.value!!)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                database = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "namma_mistri_db"
                ).build()
                Log.d(TAG, "onCreate: Database built successfully")

                val repository = NammaMistriRepository(
                    database.siteDao(),
                    database.workerDao(),
                    database.laborEntryDao(),
                    database.materialRateDao(),
                    database.photoDao()
                )
                Log.d(TAG, "onCreate: Repository created successfully")

                try {
                    Log.d(TAG, "onCreate: Checking and seeding default data")
                    val sites = repository.getAllSites().first()
                    if (sites.isEmpty()) {
                        Log.d(TAG, "onCreate: Inserting default site")
                        repository.insertSite(com.example.nammamistri.data.Site(name = "Default Site", location = "Local"))
                    }
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

                withContext(Dispatchers.Main) {
                    repositoryState.value = repository
                }
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: Error during database initialization", e)
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    initError.value = e.message ?: "Unknown initialization error"
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(durationMillis = 900)
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 900)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "App logo",
                modifier = Modifier
                    .size(180.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                        alpha = logoAlpha
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NAMMAMISTRI",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ErrorScreen(errorMessage: String) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Error initializing app: $errorMessage",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
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
            Column {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> CalculatorScreen(viewModel(factory = CalculatorViewModelFactory(repository)))
                1 -> LaborScreen(viewModel(factory = LaborViewModelFactory(repository)))
                2 -> PhotoScreen(viewModel(factory = PhotoViewModelFactory(repository)))
                3 -> RatesScreen(viewModel(factory = RatesViewModelFactory(repository)))
            }
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