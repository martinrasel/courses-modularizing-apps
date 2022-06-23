package de.bembelnaut.courses.modularizingapps.core.domain

sealed class ProgressBarState {

    object Loading : ProgressBarState()

    object Idle : ProgressBarState()
}
