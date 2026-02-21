package com.fabmax.technews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fabmax.technews.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {

    @Query("SELECT * FROM sources ORDER BY name ASC")
    fun getAllSources(): Flow<List<SourceEntity>>

    @Query("SELECT * FROM sources WHERE isEnabled = 1")
    suspend fun getEnabledSources(): List<SourceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSources(sources: List<SourceEntity>)

    @Query("UPDATE sources SET isEnabled = NOT isEnabled WHERE id = :id")
    suspend fun toggleSource(id: String)

    @Query("SELECT COUNT(*) FROM sources")
    suspend fun count(): Int
}
