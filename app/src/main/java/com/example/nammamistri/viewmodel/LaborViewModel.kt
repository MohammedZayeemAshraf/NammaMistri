package com.example.nammamistri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammamistri.data.LaborEntry
import com.example.nammamistri.data.Worker
import com.example.nammamistri.repository.NammaMistriRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LaborViewModel(private val repository: NammaMistriRepository) : ViewModel() {

    private val currentSiteId: Long = 1 // Default site

    val workers = repository.getWorkersBySite(currentSiteId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addWorker(name: String, dailyWage: Double) {
        viewModelScope.launch {
            repository.insertWorker(Worker(name = name, dailyWage = dailyWage, siteId = currentSiteId))
        }
    }

    fun addLaborEntry(workerId: Long, date: Long, present: Boolean, advance: Double) {
        viewModelScope.launch {
            repository.insertLaborEntry(LaborEntry(workerId = workerId, date = date, present = present, advance = advance))
        }
    }

    suspend fun getBalanceForWorker(worker: Worker): Double {
        val totalAdvance = repository.getTotalAdvance(worker.id)
        val daysWorked = repository.getTotalDaysWorked(worker.id)
        val wagesEarned = daysWorked * worker.dailyWage
        return wagesEarned - totalAdvance
    }

    suspend fun getTotalAdvance(workerId: Long): Double = repository.getTotalAdvance(workerId)

    suspend fun getDaysWorked(workerId: Long): Int = repository.getTotalDaysWorked(workerId)

    fun getTotalAdvanceFlow(workerId: Long) = repository.getTotalAdvanceFlow(workerId)

    fun getDaysWorkedFlow(workerId: Long) = repository.getTotalDaysWorkedFlow(workerId)

    fun deleteWorker(workerId: Long) {
        viewModelScope.launch {
            repository.deleteWorker(workerId)
        }
    }
}