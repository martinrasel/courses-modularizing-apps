package de.bembelnaut.courses.modularizingapps.core.domain

/**
 * A generic class for hiding/showing some ui component.
 */
sealed class UIComponentState {

    object Show : UIComponentState()

    object Hide : UIComponentState()
}