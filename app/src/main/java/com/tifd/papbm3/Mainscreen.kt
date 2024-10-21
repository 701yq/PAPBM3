package com.tifd.papbm3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.tifd.papbm3.navigation.BottomNavBar
import com.tifd.papbm3.ui.theme.PAPBM3Theme

class MainScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAPBM3Theme {
                MainScreen()
            }
        }
    }
}





