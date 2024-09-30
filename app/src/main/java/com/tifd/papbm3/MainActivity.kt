    package com.tifd.papbm3

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.gestures.detectTapGestures
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.AccountBox
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.input.pointer.pointerInput
    import com.tifd.papbm3.ui.theme.PAPBM3Theme
    import com.google.firebase.auth.FirebaseAuth

    class MainActivity : ComponentActivity() {

        // Inisialisasi FirebaseAuth
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Menginisialisasi Firebase Auth
            auth = FirebaseAuth.getInstance()

            setContent {
                PAPBM3Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MyScreen(auth)
                    }
                }
            }
        }
    }

    @Composable
    fun MyScreen(auth: FirebaseAuth) {
        var nama by remember { mutableStateOf("") }
        var inputNama by remember { mutableStateOf("") }
        var nim by remember { mutableStateOf("") }
        var inputNim by remember { mutableStateOf("") }
        val context = LocalContext.current

        // Validasi form untuk tombol Submit
        val isFormValid = inputNama.isNotBlank() && inputNim.isNotBlank()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Input Nama
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Icon Profile",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = inputNama,
                    onValueChange = { inputNama = it },
                    label = { Text("Masukkan Email") },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input NIM
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.num1),
                    contentDescription = "Icon Profile",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = inputNim,
                    onValueChange = { inputNim = it },
                    label = { Text("Masukkan Password") },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Submit dengan logika Firebase Authentication
            Button(
                onClick = {
                    if (isFormValid) {
                        // Contoh login dengan email (nama) dan password (nim)
                        auth.signInWithEmailAndPassword(inputNama, inputNim)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Login berhasil, navigasi ke ListActivity
                                    Toast.makeText(
                                        context,
                                        "Login Berhasil: ${auth.currentUser?.email}",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Navigasi ke ListActivity
                                    val intent = Intent(context, ListActivity::class.java)
                                    context.startActivity(intent)

                                } else {
                                    // Login gagal
                                    Toast.makeText(
                                        context,
                                        "Login Gagal: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                },
                enabled = isFormValid,  // Tombol nonaktif jika form tidak valid
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Toast.makeText(context, "Email: $nama, Password: $nim", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            ) {
                Text("Login")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PAPBM3Theme {
            MyScreen(FirebaseAuth.getInstance()) // Hanya untuk preview
        }
    }
