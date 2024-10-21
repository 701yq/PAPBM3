package com.tifd.papbm3.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tifd.papbm3.GithubProfile

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
