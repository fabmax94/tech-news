package com.fabmax.technews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class SourceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val feedUrl: String,
    val siteUrl: String,
    val logoUrl: String?,
    val category: String,
    val isEnabled: Boolean = true
)
