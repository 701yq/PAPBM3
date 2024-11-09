package com.tifd.papbm3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tifd.papbm3.navigation.BottomNavBar
import com.tifd.papbm3.screen.MatkulScreen
import com.tifd.papbm3.screen.TugasScreen
import com.tifd.papbm3.screen.GithubProfileScreen
import com.tifd.papbm3.ui.theme.PAPBM3Theme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(auth: FirebaseAuth) {
    var nama by remember { mutableStateOf("") }
    var inputNama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var inputNim by remember { mutableStateOf("") }
    val context = LocalContext.current

    val isFormValid = inputNama.isNotBlank() && inputNim.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                shape = androidx.compose.material3.Shapes().small
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                shape = androidx.compose.material3.Shapes().small,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    auth.signInWithEmailAndPassword(inputNama, inputNim)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Login Berhasil: ${auth.currentUser?.email}",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(context, MainScreenActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login Gagal: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            },
            enabled = isFormValid
        ) {
            Text("Login")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHostContainer(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    var profile by remember { mutableStateOf<GithubProfile?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(GithubService::class.java)
                profile = service.getProfile("701yq")
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    NavHost(navController, startDestination = "home") {
        composable("home") { MatkulScreen() }
        composable("profile") {
            GithubProfileScreen(profile = profile, errorMessage = errorMessage)
        }
        composable("settings") { TugasScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PAPBM3Theme {
        MyScreen(FirebaseAuth.getInstance()) // Preview layout only
    }
}
