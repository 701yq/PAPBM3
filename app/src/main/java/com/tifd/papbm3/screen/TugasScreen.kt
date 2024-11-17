package com.tifd.papbm3.screen

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun TugasScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var namaMatkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("Nama file gambar belum tersedia") }
    val taskList = remember { mutableStateListOf<Task>() }
    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isCameraVisible by remember { mutableStateOf(false) } // Kontrol visibilitas kamera
    var previewView: PreviewView? = null

    // Permission Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Check and Request Permission
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (isCameraVisible) {
        // Tampilan Kamera Layar Penuh
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }.also {
                        previewView = it
                        startCamera(context, lifecycleOwner, it) { capture ->
                            imageCapture = capture
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            // Tombol Capture di atas preview kamera
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        captureImage(context, imageCapture) { uri ->
                            if (uri != null) {
                                fileName = uri.lastPathSegment ?: "Gambar disimpan"
                                Toast.makeText(context, "Gambar berhasil disimpan di galeri.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Gagal menyimpan gambar.", Toast.LENGTH_SHORT).show()
                            }
                            isCameraVisible = false // Kembali ke tampilan utama setelah capture
                        }
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                ) {
                    Text("Capture")
                }
                Button(
                    onClick = {
                        isCameraVisible = false // Kembali ke tampilan utama tanpa mengambil gambar
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .padding(8.dp)
                ) {
                    Text("Back")
                }
            }
        }
    } else {
        // Tampilan Utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Input untuk nama matkul
            TextField(
                value = namaMatkul,
                onValueChange = { namaMatkul = it },
                label = { Text("Nama Matkul") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input untuk detail tugas
            TextField(
                value = detailTugas,
                onValueChange = { detailTugas = it },
                label = { Text("Detail Tugas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Kamera dan Add
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { isCameraVisible = true }) {
                    Text("Camera")
                }
                Button(
                    onClick = {
                        taskList.add(Task(namaMatkul, detailTugas, fileName))
                        Toast.makeText(context, "Data dan gambar ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scroll view untuk menampilkan daftar tugas
            Text(text = "Daftar Tugas", modifier = Modifier.padding(bottom = 8.dp))
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(taskList) { task ->
                    TaskItem(task = task)
                }
            }
        }
    }
}

// Fungsi untuk memulai kamera
private fun startCamera(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    previewView: PreviewView,
    onCameraInitialized: (ImageCapture) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val imageCapture = ImageCapture.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Set ImageCapture untuk digunakan saat mengambil gambar
        onCameraInitialized(imageCapture)

        // Hubungkan PreviewView dengan kamera
        preview.setSurfaceProvider(previewView.surfaceProvider)

        try {
            // Unbind semua sebelumnya dan bind lifecycle dengan kamera
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Toast.makeText(context, "Gagal memulai kamera.", Toast.LENGTH_SHORT).show()
        }
    }, ContextCompat.getMainExecutor(context))
}

// Fungsi untuk menangkap gambar dan menyimpannya di MediaStore
fun captureImage(
    context: Context,
    imageCapture: ImageCapture?,
    onImageSaved: (Uri?) -> Unit
) {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_$timestamp.jpg") // Nama gambar
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Format gambar
        put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/MyCameraApp") // Folder penyimpanan
    }

    val outputUri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )

    if (outputUri != null) {
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            outputUri,
            contentValues
        ).build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context, "Gagal mengambil gambar: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(context, "Gambar disimpan: $outputUri", Toast.LENGTH_SHORT).show()
                    onImageSaved(outputUri) // Callback dengan URI gambar
                }
            }
        )
    } else {
        Toast.makeText(context, "Gagal menyimpan gambar.", Toast.LENGTH_SHORT).show()
    }
}

// Komponen untuk menampilkan setiap item tugas
@Composable
fun TaskItem(task: Task) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.LightGray)
            .padding(16.dp)
    ) {
        Text(text = "Nama Matkul: ${task.namaMatkul}")
        Text(text = "Detail Tugas: ${task.detailTugas}", modifier = Modifier.padding(top = 4.dp))
        if (task.fileName.isNotEmpty()) {
            Text(text = "File Gambar: ${task.fileName}", modifier = Modifier.padding(top = 4.dp))
        }
    }
}

// Data class untuk menyimpan informasi tugas
data class Task(
    val namaMatkul: String,
    val detailTugas: String,
    val fileName: String = ""
)

