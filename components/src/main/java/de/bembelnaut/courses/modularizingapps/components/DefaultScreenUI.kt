package de.bembelnaut.courses.modularizingapps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState

@Composable
fun DefaultScreenUI(
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    content: @Composable () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ){
            content()
            if(progressBarState is ProgressBarState.Loading){
                CircularIndeterminateProgressBar()
            }
        }

    }
}