package com.tifd.papbm3

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Data class untuk menampung informasi profil GitHub
data class GithubProfile(
    @SerializedName("avatar_url") val avatarUrl: String,
    val login: String,
    val name: String?,
    val following: Int,
    val followers: Int
)

// Interface Retrofit untuk API GitHub
interface GithubService {
    @GET("users/{username}")
    suspend fun getProfile(@Path("username") username: String): GithubProfile
}

class GithubProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat instance Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GithubService::class.java)

        setContent {
            var profile by remember { mutableStateOf<GithubProfile?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            // Fetch data profil GitHub
            LaunchedEffect(Unit) {
                try {
                    Log.d("GithubProfileActivity", "Fetching profile...")
                    profile = service.getProfile("701yq") // Ganti dengan username dinamis jika diperlukan
                    Log.d("GithubProfileActivity", "Profile fetched: $profile")
                } catch (e: Exception) {
                    errorMessage = e.message
                    Log.e("GithubProfileActivity", "Error fetching profile", e)
                    Toast.makeText(this@GithubProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            // Tampilkan profil GitHub
            GithubProfileScreen(profile, errorMessage)
        }
    }

    @Composable
    fun GithubProfileScreen(profile: GithubProfile?, errorMessage: String?) {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (errorMessage != null) {
                // Menampilkan pesan error jika terjadi kesalahan
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $errorMessage")
                }
            } else if (profile != null) {
                // Menampilkan profil jika berhasil di-fetch
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(profile.avatarUrl),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clip(CircleShape)
                            .size(128.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Username: ${profile.login}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Name: ${profile.name ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Followers: ${profile.followers}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Following: ${profile.following}", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // Menampilkan loading indicator jika data belum tersedia
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
