package com.tifd.papbm3.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas_table")
data class Tugas(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaMatkul: String,
    val detailTugas: String,
    val isDone: Boolean = false
)
