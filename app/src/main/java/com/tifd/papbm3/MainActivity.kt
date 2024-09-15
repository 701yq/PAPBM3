package com.tifd.papbm3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAPBM3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun MyScreen() {
    var nama by remember { mutableStateOf("") }
    var inputNama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var inputNim by remember { mutableStateOf("") }

    // UI state untuk menonaktifkan button ketika form belum terisi sempurna
    val isFormValid = inputNama.isNotBlank() && inputNim.isNotBlank()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nama: $nama")
        Text(text = "NIM: $nim")
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
                label = { Text("Masukkan nama") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input NIM (Hanya angka)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                contentDescription = "Icon Profile",
                tint = Color.Black,
                modifier = Modifier
                    .size(25.dp),
                painter = painterResource(id = R.drawable.num1)

            )

            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = inputNim,
                onValueChange = { inputNim = it },
                label = { Text("Masukkan NIM") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Submit
        Button(
            onClick = {
                nama = inputNama
                nim = inputNim
            },
            enabled = isFormValid,  // Button akan nonaktif jika form tidak valid
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        Toast
                            .makeText(context, "Nama: $nama, NIM: $nim", Toast.LENGTH_LONG)
                            .show()
                    }
                )
            }
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PAPBM3Theme {
        MyScreen()
    }
}
