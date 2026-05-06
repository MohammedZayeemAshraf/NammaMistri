package com.example.nammamistri.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nammamistri.data.Site
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Insert
    suspend fun insert(site: Site): Long

    @Query("SELECT * FROM sites")
    fun getAllSites(): Flow<List<Site>>

    @Query("SELECT * FROM sites WHERE id = :id")
    suspend fun getSiteById(id: Long): Site?
}