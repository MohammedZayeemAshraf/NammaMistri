package com.example.nammamistri.repository

import com.example.nammamistri.data.*
import com.example.nammamistri.data.dao.*
import kotlinx.coroutines.flow.Flow

class NammaMistriRepository(
    private val siteDao: SiteDao,
    private val workerDao: WorkerDao,
    private val laborEntryDao: LaborEntryDao,
    private val materialRateDao: MaterialRateDao,
    private val photoDao: PhotoDao
) {
    // Sites
    suspend fun insertSite(site: Site) = siteDao.insert(site)
    fun getAllSites() = siteDao.getAllSites()
    suspend fun getSiteById(id: Long) = siteDao.getSiteById(id)

    // Workers
    suspend fun insertWorker(worker: Worker) = workerDao.insert(worker)
    fun getWorkersBySite(siteId: Long) = workerDao.getWorkersBySite(siteId)
    suspend fun getWorkerById(id: Long) = workerDao.getWorkerById(id)

    // Labor Entries
    suspend fun insertLaborEntry(entry: LaborEntry) = laborEntryDao.insert(entry)
    fun getEntriesByWorker(workerId: Long) = laborEntryDao.getEntriesByWorker(workerId)
    suspend fun getTotalAdvance(workerId: Long) = laborEntryDao.getTotalAdvance(workerId) ?: 0.0
    suspend fun getTotalDaysWorked(workerId: Long) = laborEntryDao.getTotalDaysWorked(workerId)

    // Material Rates
    suspend fun insertMaterialRate(rate: MaterialRate) = materialRateDao.insert(rate)
    suspend fun updateMaterialRate(rate: MaterialRate) = materialRateDao.update(rate)
    fun getAllMaterialRates() = materialRateDao.getAllRates()
    suspend fun getMaterialRateById(id: Long) = materialRateDao.getRateById(id)

    // Photos
    suspend fun insertPhoto(photo: Photo) = photoDao.insert(photo)
    fun getPhotosBySite(siteId: Long) = photoDao.getPhotosBySite(siteId)
    suspend fun deletePhoto(photoId: Long) = photoDao.deleteById(photoId)
}