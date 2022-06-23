package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState
import de.bembelnaut.courses.modularizingapps.ui_herolist.components.HeroListFilter
import de.bembelnaut.courses.modularizingapps.ui_herolist.components.HeroListItem
import de.bembelnaut.courses.modularizingapps.ui_herolist.components.HeroListToolbar

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    events: (HeroListEvent) -> Unit,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            HeroListToolbar(
                heroName = state.heroName,
                onHeroNameChanged = { heroName ->
                    events(HeroListEvent.UpdateHeroName(heroName = heroName))
                },
                onExecuteSearch = {
                    events(HeroListEvent.FilterHeros)
                },
                onShowFilterDialog = {

                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.filteredHeros) { hero ->
                    HeroListItem(
                        hero = hero,
                        onSelectHero = { heroId ->
                            navigateToDetailScreen(heroId)
                        },
                        imageLoader = imageLoader,
                    )
                }
            }
        }
        HeroListFilter(
            heroFilter = state.heroFilter,
            onUpdateHeroFilter = { heroFilter ->
                events(HeroListEvent.UpdateHeroFilter(heroFilter))
            },
        ) {

        }

        if (state.progressBarState is ProgressBarState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
