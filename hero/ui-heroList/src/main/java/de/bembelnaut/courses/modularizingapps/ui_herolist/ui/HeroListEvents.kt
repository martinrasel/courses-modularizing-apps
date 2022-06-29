package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import de.bembelnaut.courses.modularizingapps.core.domain.UIComponentState
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroAttribute
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroFilter

sealed class HeroListEvents {

    object GetHeros : HeroListEvents()

    object FilterHeros : HeroListEvents()

    data class UpdateHeroName(
        val heroName: String,
    ) : HeroListEvents()

    data class UpdateHeroFilter(
        val heroFilter: HeroFilter
    ) : HeroListEvents()

    data class UpdateAttributeFilter(
        val attribute: HeroAttribute
    ): HeroListEvents()

    data class UpdateFilterDialogState(
        val uiComponentState: UIComponentState
    ) : HeroListEvents()

    object OnRemoveHeadFromQueue: HeroListEvents()
}
