package de.bembelnaut.courses.modularizingapps.core

sealed class ProgressBarState {

    object Loading : ProgressBarState()

    object Idle : ProgressBarState()
}
