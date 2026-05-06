package com.example.nammamistri.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nammamistri.data.Worker
import com.example.nammamistri.ui.theme.BalanceGreen
import com.example.nammamistri.ui.theme.BalanceRed
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
            ExtendedFloatingActionButton(
                onClick = { showAddWorkerDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+ Add Worker", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    "Labor Team",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${workers.size} worker${if (workers.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(workers) { worker ->
                val totalAdvance by viewModel.getTotalAdvanceFlow(worker.id)
                    .collectAsState(initial = 0.0)
                val daysWorked by viewModel.getDaysWorkedFlow(worker.id)
                    .collectAsState(initial = 0)
                val balance = (daysWorked * worker.dailyWage) - totalAdvance
                val balanceColor = if (balance >= 0) BalanceGreen else BalanceRed
                val initials = worker.name.trim().split(" ")
                    .take(2).joinToString("") { it.first().uppercaseChar().toString() }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header row: avatar + name + remove button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    initials,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    worker.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "₹${worker.dailyWage}/day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            TextButton(
                                onClick = { viewModel.deleteWorker(worker.id) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Remove")
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Stats row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatChip(label = "Days", value = "$daysWorked")
                            StatChip(label = "Advance", value = "₹${String.format("%.0f", totalAdvance)}")
                            StatChip(
                                label = "Balance",
                                value = "₹${String.format("%.0f", kotlin.math.abs(balance))}",
                                valueColor = balanceColor,
                                sublabel = if (balance >= 0) "to pay" else "overpaid"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                selectedWorker = worker
                                showAddEntryDialog = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Add Today's Entry")
                        }
                    }
                }
            }

            if (workers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No workers added yet", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "Tap '+ Add Worker' to get started",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
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
fun StatChip(label: String, value: String, valueColor: Color = Color.Unspecified, sublabel: String = "") {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        if (sublabel.isNotEmpty()) {
            Text(sublabel, style = MaterialTheme.typography.labelSmall,
                color = valueColor.copy(alpha = 0.8f))
        }
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