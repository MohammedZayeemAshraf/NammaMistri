package com.example.nammamistri.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nammamistri.data.dao.*

@Database(
    entities = [Site::class, Worker::class, LaborEntry::class, MaterialRate::class, Photo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun siteDao(): SiteDao
    abstract fun workerDao(): WorkerDao
    abstract fun laborEntryDao(): LaborEntryDao
    abstract fun materialRateDao(): MaterialRateDao
    abstract fun photoDao(): PhotoDao
}