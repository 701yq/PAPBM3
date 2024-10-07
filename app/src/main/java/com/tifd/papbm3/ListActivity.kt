package com.tifd.papbm3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tifd.papbm3.ui.theme.PAPBM3Theme

// Data class untuk menampung informasi mata kuliah
data class Course(
    val hari: String = "",
    val namaMataKuliah: String = "",
    val jam: String = ""
)

class ListActivity : ComponentActivity() {

    // Referensi ke Firebase Realtime Database
    private val database = FirebaseDatabase.getInstance().getReference("courses")
    private lateinit var auth: FirebaseAuth  // Inisialisasi FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        setContent {
            PAPBM3Theme {
                Scaffold(
                    topBar = { TopAppBarWithButton() }  // Menambahkan TopAppBar
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Tampilan daftar mata kuliah
                            CourseListScreen(
                                modifier = Modifier
                                    .weight(1f)  // Gunakan weight untuk memenuhi ruang
                                    .fillMaxWidth()
                            )

                            // Tambah Spacer untuk memberikan ruang
                            Spacer(modifier = Modifier.height(16.dp))

                            // Tombol Logout
                            Button(
                                onClick = {
                                    logoutUser(auth) { message ->
                                        // Tampilkan pesan logout
                                        Toast.makeText(this@ListActivity, message, Toast.LENGTH_SHORT).show()
                                        finish() // Kembali ke MainActivity
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Logout")
                            }
                        }
                    }
                }
            }
        }
    }

    // Fungsi untuk logout
    fun logoutUser(auth: FirebaseAuth, callback: (String) -> Unit) {
        auth.signOut()
        callback("Logged out successfully!")
    }

    // Fungsi untuk menampilkan TopAppBar dengan button untuk GitHub profile
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarWithButton() {
        TopAppBar(
            title = { Text("Course List") },
            actions = {
                IconButton(onClick = {
                    // Navigasi ke GithubProfileActivity
                    val intent = Intent(this@ListActivity, GithubProfileActivity::class.java)
                    startActivity(intent)
                }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "GitHub Profile")
                }
            }
        )
    }

    @Composable
    fun CourseListScreen(modifier: Modifier = Modifier) {
        var courseList by remember { mutableStateOf(listOf<Course>()) }

        // Ambil data dari Firebase Realtime Database
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
                    Log.w("ListActivity", "Failed to read value.", error.toException())
                }
            })
        }

        // Tampilan List menggunakan LazyColumn
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier  // Meneruskan modifier dari parameter
        ) {
            items(courseList) { course ->
                CourseCard(course)
            }
        }
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
}
