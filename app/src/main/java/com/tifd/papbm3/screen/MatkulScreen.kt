package com.tifd.papbm3.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tifd.papbm3.Course


@Composable
fun MatkulScreen(modifier: Modifier = Modifier) {
    var courseList by remember { mutableStateOf(listOf<Course>()) }
    val database = FirebaseDatabase.getInstance().getReference("courses")


    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val courses = mutableListOf<Course>()
                for (dataSnapshot in snapshot.children) {
                    val course = dataSnapshot.getValue(Course::class.java)
                    course?.let { courses.add(it) }
                }
                courseList = courses
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MatkulScreen", "Failed to read value.", error.toException())
            }
        })
    }

    Scaffold(
        topBar = { TopAppBarWithButton() }
    ) { innerPadding ->
        // Tampilan List menggunakan LazyColumn
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(innerPadding)
        ) {
            items(courseList) { course ->
                CourseCard(course)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithButton() {
    TopAppBar(
        title = { Text("Course List") },


        )
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)  // Menggunakan CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Menampilkan hari
            Text(text = "Hari: ${course.hari}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            // Menampilkan nama mata kuliah
            Text(text = "Mata Kuliah: ${course.namaMataKuliah}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Menampilkan jam mata kuliah
            Text(text = "Jam: ${course.jam}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
