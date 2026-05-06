package com.example.nammamistri.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nammamistri.data.MaterialRate
import com.example.nammamistri.viewmodel.RatesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(viewModel: RatesViewModel = viewModel()) {
    val rates by viewModel.materialRates.collectAsState(initial = emptyList())
    var showAddRateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddRateDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Material Rates", style = MaterialTheme.typography.headlineMedium)
            }

            items(rates) { rate ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(rate.materialName, style = MaterialTheme.typography.titleMedium)
                            Text("Unit: ${rate.unit}")
                        }
                        Text("₹${rate.rate}")
                    }
                }
            }
        }
    }

    if (showAddRateDialog) {
        AddRateDialog(
            onDismiss = { showAddRateDialog = false },
            onAdd = { name, unit, rate ->
                viewModel.addMaterialRate(name, unit, rate)
                showAddRateDialog = false
            }
        )
    }
}

@Composable
fun AddRateDialog(onDismiss: () -> Unit, onAdd: (String, String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Material Rate") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Material Name") }
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") }
                )
                OutlinedTextField(
                    value = rate,
                    onValueChange = { rate = it },
                    label = { Text("Rate") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val r = rate.toDoubleOrNull() ?: 0.0
                onAdd(name, unit, r)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}