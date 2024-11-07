package com.dicoding.asclepius.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check")
data class Check(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String,
    val classification: String,
    val confidenceScore: String,
    val checkDate: String
)
