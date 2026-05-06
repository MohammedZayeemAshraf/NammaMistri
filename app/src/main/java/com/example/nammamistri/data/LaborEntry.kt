package com.example.nammamistri.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labor_entries")
data class LaborEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workerId: Long,
    val date: Long,
    val present: Boolean,
    val advance: Double = 0.0
)