package de.bembelnaut.courses.modularizingapps.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import de.bembelnaut.courses.modularizingapps.coil.FakeImageLoader
import de.bembelnaut.courses.modularizingapps.di.HeroInteractorsModule
import de.bembelnaut.courses.modularizingapps.hero_datasource.cache.HeroCache
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.HeroService
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.cache.HeroCacheFake
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.cache.HeroDatabaseFake
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.network.HeroServiceFake
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.network.HeroServiceResponseType
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroAttribute
import de.bembelnaut.courses.modularizingapps.hero_interactors.FilterHeros
import de.bembelnaut.courses.modularizingapps.hero_interactors.GetHeroFromCache
import de.bembelnaut.courses.modularizingapps.hero_interactors.GetHeros
import de.bembelnaut.courses.modularizingapps.hero_interactors.HeroInteractors
import de.bembelnaut.courses.modularizingapps.ui.navigation.Screen
import de.bembelnaut.courses.modularizingapps.ui.theme.DotaInfoTheme
import de.bembelnaut.courses.modularizingapps.ui_herodetail.ui.HeroDetail
import de.bembelnaut.courses.modularizingapps.ui_herodetail.ui.HeroDetailViewModel
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroList
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.HeroListViewModel
import de.bembelnaut.courses.modularizingapps.ui_herolist.ui.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@UninstallModules(HeroInteractorsModule::class)
@HiltAndroidTest
class HeroListEndToEnd {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestHeroInteractorsModule {

        @Provides
        @Singleton
        fun provideHeroCache(): HeroCache {
            return HeroCacheFake(HeroDatabaseFake())
        }

        @Provides
        @Singleton
        fun provideHeroService(): HeroService {
            return HeroServiceFake.build(
                type = HeroServiceResponseType.GoodData
            )
        }

        @Provides
        @Singleton
        fun provideHeroInteractors(
            cache: HeroCache,
            service: HeroService
        ): HeroInteractors {
            return HeroInteractors(
                getHeros = GetHeros(
                    cache = cache,
                    service = service,
                ),
                filterHeros = FilterHeros(),
                getHeroFromCache = GetHeroFromCache(
                    cache = cache,
                ),
            )
        }
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader(context)

    @Before
    fun before() {
        composeTestRule.setContent {
            DotaInfoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route,
                    builder = {
                        composable(
                            route = Screen.HeroList.route,
                        ) {
                            val viewModel: HeroListViewModel = hiltViewModel()
                            HeroList(
                                state = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                navigateToDetailScreen = { heroId ->
                                    navController.navigate("${Screen.HeroDetail.route}/$heroId")
                                },
                                imageLoader = imageLoader,
                            )
                        }
                        composable(
                            route = Screen.HeroDetail.route + "/{heroId}",
                            arguments = Screen.HeroDetail.arguments,
                        ) {
                            val viewModel: HeroDetailViewModel = hiltViewModel()
                            HeroDetail(
                                heroDetailState = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                imageLoader = imageLoader
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun testSearchHeroByName() {
        composeTestRule.onRoot(useUnmergedTree = true)
            .printToLog("TAG") // For learning the ui tree system

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Anti-Mage")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Anti-Mage",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Storm Spirit")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Storm Spirit",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Mirana")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Mirana",
        )
    }

    @Test
    fun testFilterHeroAlphabetically() {
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by "Hero" name
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_HERO_CHECKBOX).performClick()

        // Order Descending (z-a)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true)
            .assertAny(hasText("Zeus"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (a-z)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true)
            .assertAny(hasText("Abaddon"))
    }

    @Test
    fun testFilterHeroByProWins() {
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by ProWin %
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_PROWINS_CHECKBOX).performClick()

        // Order Descending (100% - 0%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true)
            .assertAny(hasText("Chen"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (0% - 100%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true)
            .assertAny(hasText("Dawnbreaker"))
    }

    @Test
    fun testFilterHeroByStrength() {
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_STENGTH_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true)
            .assertAll(
                hasText(
                    HeroAttribute.Strength.uiValue
                )
            )
    }

    @Test
    fun testFilterHeroByAgility() {
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_AGILITY_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true)
            .assertAll(hasText(HeroAttribute.Agility.uiValue))
    }

    @Test
    fun testFilterHeroByIntelligence() {
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_INT_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true)
            .assertAll(
                hasText(
                    HeroAttribute.Intelligence.uiValue
                )
            )
    }
}