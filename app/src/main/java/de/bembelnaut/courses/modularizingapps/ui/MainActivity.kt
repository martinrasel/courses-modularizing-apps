package de.bembelnaut.courses.modularizingapps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import de.bembelnaut.courses.modularizingapps.ui.navigation.Screen
import de.bembelnaut.courses.modularizingapps.ui.theme.DotaInfoTheme
import de.bembelnaut.courses.modularizingapps.ui_herodetail.ui.HeroDetail
import de.bembelnaut.courses.modularizingapps.ui_herodetail.ui.HeroDetailViewModel
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroList
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroListViewModel
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
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
                        addHeroDetail(
                            imageLoader = imageLoader,
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader,
) {
    composable(
        route = Screen.HeroList.route,
    ) {
        val viewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = viewModel.state.value,
            imageLoader = imageLoader,
            navigateToDetailScreen = { heroId ->
                navController.navigate("${Screen.HeroDetail.route}/$heroId")
            },
            events = viewModel::onTriggerEvent
        )
    }
}

fun NavGraphBuilder.addHeroDetail(
    imageLoader: ImageLoader,
) {
    composable(
        route = Screen.HeroDetail.route + "/{heroId}",
        arguments = Screen.HeroDetail.arguments,
    ) {
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetail(
            heroDetailState = viewModel.state.value,
            imageLoader = imageLoader,
        )
    }
}
