package com.tifd.papbm3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tifd.papbm3.local.Tugas
import com.tifd.papbm3.local.TugasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TugasViewModel(private val repository: TugasRepository) : ViewModel() {

    val allTugas: Flow<List<Tugas>> = repository.getAllTugas()

    fun insert(tugas: Tugas) {
        viewModelScope.launch {
            repository.insert(tugas)
        }
    }

    fun updateIsDone(tugas: Tugas) {
        viewModelScope.launch {
            repository.update(tugas)
        }
    }
}


