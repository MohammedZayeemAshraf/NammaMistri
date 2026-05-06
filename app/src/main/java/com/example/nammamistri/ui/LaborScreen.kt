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
import com.example.nammamistri.data.Worker
import com.example.nammamistri.viewmodel.LaborViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaborScreen(viewModel: LaborViewModel = viewModel()) {
    val workers by viewModel.workers.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showAddWorkerDialog by remember { mutableStateOf(false) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var selectedWorker by remember { mutableStateOf<Worker?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddWorkerDialog = true }) {
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
                Text("Labor Team", style = MaterialTheme.typography.headlineMedium)
            }

            items(workers) { worker ->
                val totalAdvance by viewModel.getTotalAdvanceFlow(worker.id)
                    .collectAsState(initial = 0.0)
                val daysWorked by viewModel.getDaysWorkedFlow(worker.id)
                    .collectAsState(initial = 0)
                val balance = (daysWorked * worker.dailyWage) - totalAdvance

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(worker.name, style = MaterialTheme.typography.titleMedium)
                            TextButton(
                                onClick = { viewModel.deleteWorker(worker.id) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Remove")
                            }
                        }
                        Text("Daily Wage: ₹${worker.dailyWage}")
                        Text("Days Present: $daysWorked")
                        Text("Total Advance: ₹${String.format("%.2f", totalAdvance)}")
                        Text("Balance Due: ₹${String.format("%.2f", balance)}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                selectedWorker = worker
                                showAddEntryDialog = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Entry")
                        }
                    }
                }
            }
        }
    }

    if (showAddWorkerDialog) {
        AddWorkerDialog(
            onDismiss = { showAddWorkerDialog = false },
            onAdd = { name, wage ->
                viewModel.addWorker(name, wage)
                showAddWorkerDialog = false
            }
        )
    }

    if (showAddEntryDialog && selectedWorker != null) {
        AddLaborEntryDialog(
            worker = selectedWorker!!,
            onDismiss = { showAddEntryDialog = false },
            onAdd = { present, advance ->
                viewModel.addLaborEntry(selectedWorker!!.id, System.currentTimeMillis(), present, advance)
                showAddEntryDialog = false
            }
        )
    }
}

@Composable
fun AddWorkerDialog(onDismiss: () -> Unit, onAdd: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var wage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Worker") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = wage,
                    onValueChange = { wage = it },
                    label = { Text("Daily Wage") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val w = wage.toDoubleOrNull() ?: 0.0
                onAdd(name, w)
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

@Composable
fun AddLaborEntryDialog(worker: Worker, onDismiss: () -> Unit, onAdd: (Boolean, Double) -> Unit) {
    var present by remember { mutableStateOf(true) }
    var advance by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Labor Entry for ${worker.name}") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = present, onCheckedChange = { present = it })
                    Text("Present")
                }
                OutlinedTextField(
                    value = advance,
                    onValueChange = { advance = it },
                    label = { Text("Advance Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val adv = advance.toDoubleOrNull() ?: 0.0
                onAdd(present, adv)
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