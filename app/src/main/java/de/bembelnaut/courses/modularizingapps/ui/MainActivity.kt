package de.bembelnaut.courses.modularizingapps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import de.bembelnaut.courses.modularizingapps.ui.theme.DotaInfoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DotaInfoTheme {
                Text("Starting point")
            }
        }
    }
}