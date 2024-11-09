package com.tifd.papbm3.local

import kotlinx.coroutines.flow.Flow

class TugasRepository(private val tugasDao: TugasDao) {

    // Fungsi untuk mendapatkan semua tugas
    fun getAllTugas(): Flow<List<Tugas>> {
        return tugasDao.getAllTugas()
    }

    // Fungsi untuk menambahkan tugas baru
    suspend fun insert(tugas: Tugas) {
        tugasDao.insert(tugas)
    }

    // Fungsi untuk memperbarui tugas
    suspend fun update(tugas: Tugas) {
        tugasDao.update(tugas)
    }

    // Fungsi untuk memperbarui status isDone
    suspend fun updateIsDone(tugas: Tugas) {
        tugasDao.update(tugas)
    }
}


