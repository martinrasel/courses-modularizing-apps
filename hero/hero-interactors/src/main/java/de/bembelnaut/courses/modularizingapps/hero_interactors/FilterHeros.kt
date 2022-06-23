package de.bembelnaut.courses.modularizingapps.hero_interactors

import de.bembelnaut.courses.modularizingapps.core.domain.FilterOrder
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroAttribute
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroFilter
import kotlin.math.round

class FilterHeros {

    fun execute(
        current: List<Hero>,
        heroName: String,
        heroFilter: HeroFilter,
        attributeFilter: HeroAttribute,
    ): List<Hero> {
        var filteredList: MutableList<Hero> = current.filter {
            it.localizedName.lowercase().contains(heroName.lowercase())
        }.toMutableList()

        when (heroFilter) {
            is HeroFilter.Hero -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending { it.localizedName }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy { it.localizedName }
                    }
                }
            }
            is HeroFilter.ProWins -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending {
                            getProWins(it.proPick.toDouble(), it.proWins.toDouble())
                        }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy {
                            getProWins(it.proPick.toDouble(), it.proWins.toDouble())
                        }
                    }
                }
            }
        }

        when (attributeFilter) {
            is HeroAttribute.Strength -> {
                filteredList = filteredList.filter { it.primaryAttribute is HeroAttribute.Strength }
                    .toMutableList()
            }
            is HeroAttribute.Agility -> {
                filteredList = filteredList.filter { it.primaryAttribute is HeroAttribute.Agility }
                    .toMutableList()
            }
            is HeroAttribute.Intelligence -> {
                filteredList =
                    filteredList.filter { it.primaryAttribute is HeroAttribute.Intelligence }
                        .toMutableList()
            }
            is HeroAttribute.Unknown -> {
                // do not filter
            }
        }

        return filteredList
    }

    private fun getProWins(proPick: Double, proWin: Double) =
        if (proPick <= 0) { // can't divide by 0
            0
        } else {
            val winRate: Int =
                round(proWin / proPick * 100).toInt()
            winRate
        }
}