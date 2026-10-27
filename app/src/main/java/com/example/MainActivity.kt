package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.phase1.Phase1Screen
import com.example.ui.phase1.Phase1ViewModel
import com.example.ui.theme.DiskiFootballTheme

class MainActivity : ComponentActivity() {
    private val phase1ViewModel: Phase1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiskiFootballTheme {
                Phase1Screen(viewModel = phase1ViewModel)
            }
        }
    }
}
