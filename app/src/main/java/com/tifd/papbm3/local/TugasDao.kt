package com.tifd.papbm3.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas_table")
    fun getAllTugas(): Flow<List<Tugas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)
}

