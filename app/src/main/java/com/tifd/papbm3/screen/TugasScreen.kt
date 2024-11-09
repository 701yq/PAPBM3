package com.tifd.papbm3.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tifd.papbm3.viewmodel.TugasViewModel
import com.tifd.papbm3.local.Tugas
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TugasScreen(viewModel: TugasViewModel = viewModel()) {
    var namaMatkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }

    // Menggunakan `collectAsState` untuk mengumpulkan data dari Flow di ViewModel
    val allTugas by viewModel.allTugas.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = namaMatkul,
            onValueChange = { namaMatkul = it },
            label = { Text("Nama Mata Kuliah") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = detailTugas,
            onValueChange = { detailTugas = it },
            label = { Text("Detail Tugas") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val tugas = Tugas(
                    namaMatkul = namaMatkul,
                    detailTugas = detailTugas,
                    isDone = false  // Set nilai awal isDone menjadi false
                )
                viewModel.insert(tugas)
                namaMatkul = ""
                detailTugas = ""
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Submit Tugas")
        }


        LazyColumn {
            items(allTugas) { tugas ->
                TugasCard(tugas, viewModel)
            }
        }
    }
}

@Composable
fun TugasCard(tugas: Tugas, viewModel: TugasViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Mata Kuliah: ${tugas.namaMatkul}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Detail: ${tugas.detailTugas}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(
                onClick = {
                    viewModel.updateIsDone(tugas.copy(isDone = true))
                }
            )
            {
                Icon(
                    imageVector = if (tugas.isDone) Icons.Default.Check else Icons.Outlined.CheckBoxOutlineBlank,
                    contentDescription = "Mark as Done"
                )
            }
        }
    }
}
