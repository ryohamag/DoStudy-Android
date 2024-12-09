package com.websarva.wings.dostudy_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.websarva.wings.dostudy_android.components.MainScreen
import com.websarva.wings.dostudy_android.ui.theme.DoStudyAndroidTheme
import com.websarva.wings.dostudy_android.viewmodels.MainScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoStudyAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val vm = MainScreenViewModel(context)
                    MainScreen(innerPadding, vm)
                }
            }
        }
    }
}