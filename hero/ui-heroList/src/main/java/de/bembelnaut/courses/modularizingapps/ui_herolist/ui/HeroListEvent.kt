package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import de.bembelnaut.courses.modularizingapps.core.domain.UIComponentState
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroFilter

sealed class HeroListEvent {

    object GetHeros : HeroListEvent()

    object FilterHeros : HeroListEvent()

    data class UpdateHeroName(
        val heroName: String,
    ) : HeroListEvent()

    data class UpdateHeroFilter(
        val heroFilter: HeroFilter
    ) : HeroListEvent()

    data class UpdateFilterDialogState(
        val uiComponentState: UIComponentState
    ) : HeroListEvent()
}
