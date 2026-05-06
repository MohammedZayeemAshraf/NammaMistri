package com.example.nammamistri.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nammamistri.data.LaborEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LaborEntryDao {
    @Insert
    suspend fun insert(entry: LaborEntry): Long

    @Query("SELECT * FROM labor_entries WHERE workerId = :workerId ORDER BY date DESC")
    fun getEntriesByWorker(workerId: Long): Flow<List<LaborEntry>>

    @Query("SELECT SUM(advance) FROM labor_entries WHERE workerId = :workerId")
    suspend fun getTotalAdvance(workerId: Long): Double?

    @Query("SELECT COUNT(*) FROM labor_entries WHERE workerId = :workerId AND present = 1")
    suspend fun getTotalDaysWorked(workerId: Long): Int

    @Query("SELECT COALESCE(SUM(advance), 0.0) FROM labor_entries WHERE workerId = :workerId")
    fun getTotalAdvanceFlow(workerId: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM labor_entries WHERE workerId = :workerId AND present = 1")
    fun getTotalDaysWorkedFlow(workerId: Long): Flow<Int>
}