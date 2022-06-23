package de.bembelnaut.courses.modularizingapps.ui_herodetail.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.bembelnaut.courses.modularizingapps.core.domain.DataState
import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState
import de.bembelnaut.courses.modularizingapps.hero_interactors.GetHeroFromCache
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel
@Inject
constructor(
    private val getHeroFromCache: GetHeroFromCache,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState(progressBarState = ProgressBarState.Idle))

    init {
        savedStateHandle.get<Int>("heroId")?.let {
            onTriggerEvent(HeroDetailEvents.GetHeroFromCache(it))
        }
    }

    fun onTriggerEvent(event: HeroDetailEvents) {
        when(event) {
            is HeroDetailEvents.GetHeroFromCache -> {
                getHeroFromCache(event.id)
            }
        }

    }

    private fun getHeroFromCache(id: Int) {
        getHeroFromCache.execute(id).onEach { dataState ->
            when(dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
                is DataState.Response -> {
                    // TODO(handle errors)
                }
            }
        }.launchIn(viewModelScope)
    }

}