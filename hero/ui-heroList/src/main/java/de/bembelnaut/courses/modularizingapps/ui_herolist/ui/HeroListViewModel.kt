package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.bembelnaut.courses.modularizingapps.core.domain.DataState
import de.bembelnaut.courses.modularizingapps.core.domain.Queue
import de.bembelnaut.courses.modularizingapps.core.util.Logger
import de.bembelnaut.courses.modularizingapps.core.domain.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroAttribute
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroFilter
import de.bembelnaut.courses.modularizingapps.hero_interactors.FilterHeros
import de.bembelnaut.courses.modularizingapps.hero_interactors.GetHeros
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroListViewModel
@Inject
constructor(
    private val getHeros: GetHeros,
    private val filterHeros: FilterHeros,
    @Named("heroListLogger") private val logger: Logger,
) : ViewModel() {

    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        onTriggerEvent(HeroListEvent.GetHeros)
    }

    fun onTriggerEvent(event: HeroListEvent) {
        when (event) {
            is HeroListEvent.GetHeros -> {
                getHeros()
            }
            is HeroListEvent.FilterHeros -> {
                filterHeros()
            }
            is HeroListEvent.UpdateHeroFilter -> {
                updateHeroFilter(event.heroFilter)
            }
            is HeroListEvent.UpdateHeroName -> {
                updateHeroName(event.heroName)
            }
            is HeroListEvent.UpdateAttributeFilter -> {
                updateAttributeFilter(event.attribute)
            }
            is HeroListEvent.UpdateFilterDialogState -> {
                state.value = state.value.copy(filterDialogState = event.uiComponentState)
            }
        }
    }

    private fun updateAttributeFilter(attribute: HeroAttribute) {
        state.value = state.value.copy(primaryAttrFilter = attribute)
        filterHeros()
    }

    private fun updateHeroFilter(heroFilter: HeroFilter){
        state.value = state.value.copy(heroFilter = heroFilter)
        filterHeros()
    }

    private fun updateHeroName(heroName: String) {
        state.value = state.value.copy(heroName = heroName)
    }

    private fun filterHeros() {
        val filteredList = filterHeros.execute(
            current = state.value.heros,
            heroName = state.value.heroName,
            heroFilter = state.value.heroFilter,
            attributeFilter = state.value.primaryAttrFilter,
        )
        state.value = state.value.copy(filteredHeros = filteredList)
    }

    private fun getHeros() {
        getHeros.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.Dialog).description)
                        }
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(heros = dataState.data ?: listOf())
                    filterHeros()
                }
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        state.value = state.value.copy(errorQueue = queue)
    }
}