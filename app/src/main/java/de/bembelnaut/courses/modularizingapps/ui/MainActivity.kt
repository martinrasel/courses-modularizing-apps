package de.bembelnaut.courses.modularizingapps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.memory.MemoryCache
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.AndroidEntryPoint
import de.bembelnaut.courses.modularizingapps.R
import de.bembelnaut.courses.modularizingapps.core.DataState
import de.bembelnaut.courses.modularizingapps.core.Logger
import de.bembelnaut.courses.modularizingapps.core.ProgressBarState
import de.bembelnaut.courses.modularizingapps.core.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_interactors.HeroInteractors
import de.bembelnaut.courses.modularizingapps.ui.navigation.Screen
import de.bembelnaut.courses.modularizingapps.ui.theme.DotaInfoTheme
import de.bembelnaut.courses.modularizingapps.ui_herodetail.ui.HeroDetail
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroList
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroListState
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DotaInfoTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route,
                    builder = {
                        addHeroList(
                            navController = navController,
                            imageLoader = imageLoader,
                        )
                        addHeroDetail()
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader,
) {
    composable(
        route = Screen.HeroList.route
    ) {
        val heroListViewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = heroListViewModel.state.value,
            imageLoader = imageLoader,
            navigateToDetailScreen = { heroId ->
                navController.navigate("${Screen.HeroDetail.route}/$heroId")
            }
        )
    }
}

fun NavGraphBuilder.addHeroDetail() {
    composable(
        route = Screen.HeroDetail.route + "/{heroId}",
        arguments = Screen.HeroDetail.arguments,
    ) {
        HeroDetail(
            heroId = it.arguments?.get("heroId") as Int?
        )
    }
}