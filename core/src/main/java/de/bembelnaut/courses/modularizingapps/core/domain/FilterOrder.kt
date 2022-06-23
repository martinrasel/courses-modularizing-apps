package de.bembelnaut.courses.modularizingapps.core.domain

sealed class FilterOrder {

    object Ascending : FilterOrder()

    object Descending : FilterOrder()
}
