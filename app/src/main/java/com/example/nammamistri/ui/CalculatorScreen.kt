package com.example.nammamistri.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nammamistri.viewmodel.CalculatorViewModel
import com.example.nammamistri.viewmodel.MaterialCalculation

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var thickness by remember { mutableStateOf("0.23") } // Default brick wall thickness in meters
    var calculation by remember { mutableStateOf<MaterialCalculation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Material Calculator", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = length,
            onValueChange = { length = it },
            label = { Text("Length (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = width,
            onValueChange = { width = it },
            label = { Text("Width (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = thickness,
            onValueChange = { thickness = it },
            label = { Text("Wall Thickness (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val l = length.toDoubleOrNull() ?: 0.0
                val w = width.toDoubleOrNull() ?: 0.0
                val h = height.toDoubleOrNull() ?: 0.0
                val t = thickness.toDoubleOrNull() ?: 0.23
                calculation = viewModel.calculateMaterials(l, w, h, t)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        calculation?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Materials Required:", style = MaterialTheme.typography.titleMedium)
                    Text("Bricks: ${it.bricks.toInt()}")
                    Text("Cement Bags: ${it.cementBags.toInt()}")
                    Text("Sand (cubic meters): ${String.format("%.2f", it.sandCubicMeters)}")
                }
            }
        }
    }
}