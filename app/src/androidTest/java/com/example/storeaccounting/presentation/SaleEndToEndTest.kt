package com.example.storeaccounting.presentation

import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.storeaccounting.core.TestTag
import com.example.storeaccounting.core.TestTag.INVENTORY_ITEM_LAZY_COLUMN
import com.example.storeaccounting.core.TestTag.InventoryLazyKey
import com.example.storeaccounting.core.TestTag.SALE_ITEM_LAZY_COLUMN
import com.example.storeaccounting.core.TestTag.SaleKey
import com.example.storeaccounting.core.TestTag.SaleLazyKey
import com.example.storeaccounting.di.AppModule
import com.example.storeaccounting.presentation.add_edit_credit_card.AddEditCreditCardTopBar
import com.example.storeaccounting.presentation.add_edit_factor.AddEditFactor
import com.example.storeaccounting.presentation.splash_screen.SplashScreen
import com.example.storeaccounting.presentation.util.Constants
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.setting.SettingViewModel
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.collectLatest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class SaleEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    var _route = ""


    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.activity.setContent {
            StoreAccountingTheme {
                val settingViewModel = viewModel<SettingViewModel>()
                LaunchedEffect(key1 = true){
                    settingViewModel.readCurrentThemeForDataStore().collectLatest { theme ->
                        when(theme){
                            ThemeState.ThemeNight.theme  ->  {
                                Log.d("ThemeNight","aa")
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            }
                            ThemeState.ThemeDay.theme  ->  {
                                Log.d("ThemeDay","aa")
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            }
                            ThemeState.ThemeDefault.theme  ->  {
                                Log.d("ThemeDefault","aa")
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            }
                        }
                    }
                }
                SetStatusBarTheme(
                    window = composeRule.activity.window,
                    currentFragment = ""
                )
                val navController = rememberNavController()
                val parentNavController = rememberNavController()
                NavHost(
                    navController = parentNavController,
                    startDestination = NavigationRoute.SplashScreen.route,
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ){
                    composable(route = NavigationRoute.SplashScreen.route){
                        _route = it.destination.route!!
                        SplashScreen(parentNavController = parentNavController)
                    }
                    composable(route = NavigationRoute.Main.route){
                        _route = it.destination.route!!
                        Main(
                            navController = navController,
                            parentNavController = parentNavController,
                            window = composeRule.activity.window
                        )

                    }
                    composable(
                        route = "${NavigationRoute.AddEditCreditCard.route}/{${Constants.CREDIT_CARD_ID}}",
                        arguments = listOf(
                            navArgument(Constants.CREDIT_CARD_ID){
                                type = NavType.IntType
                                this.defaultValue = -1
                            }
                        )
                    ){
                        _route = it.destination.route!!
                        SetStatusBarTheme(composeRule.activity.window,it.destination.route!!.split("/").first())
                        AddEditCreditCardTopBar(
                            parentNavController = parentNavController,
                            editCardId = it.arguments?.getInt(Constants.CREDIT_CARD_ID)
                        )
                    }
                    composable(
                        route = NavigationRoute.AddEditFactor.route,
                    ){
                        _route = it.destination.route!!
                        SetStatusBarTheme(composeRule.activity.window,it.destination.route!!.split("/").first())
                        AddEditFactor(
                            parentNavController = parentNavController,
                        )
                    }
                }
            }
        }
    }
    @Test
    fun testAddInventoryAddSale(){
        //AddInventory
        composeRule.waitUntil(
            4000,
            condition ={
                _route.lowercase() == NavigationRoute.Main.route.lowercase()
            }
        )
        composeRule.onNodeWithContentDescription(NavigationRoute.Inventory.title!!).performClick()
        ('a'..'j').forEachIndexed { index, c ->

            composeRule
                .onNodeWithContentDescription("add_inventory")
                .performClick()
            composeRule
                .onNodeWithTag(TestTag.INVENTORY_NAME)
                .performTextInput(c.toString())
            composeRule
                .onNodeWithTag(TestTag.INVENTORY_NUMBER)
                .performTextInput((c.code * (5..10)
                    .random()).toString())
            composeRule
                .onNodeWithTag(TestTag.INVENTORY_BUY_PRICE)
                .performTextInput((c.code * (10..20)
                    .random()).toString())
            composeRule
                .onNodeWithTag(TestTag.INVENTORY_SELL_PRICE)
                .performTextInput((c.code * (20..30)
                    .random()).toString())
            composeRule
                .onNodeWithTag(TestTag.INVENTORY_ADD)
                .performClick()

            //assert added inventory is displayed
            composeRule
                .onNodeWithTag(INVENTORY_ITEM_LAZY_COLUMN)
                .performScrollToNode(
                matcher = SemanticsMatcher.expectValue(key = InventoryLazyKey, expectedValue = c.toString())
            )
            composeRule
                .onNodeWithTag(c.toString())
                .assertIsDisplayed()
        }
        //add Sale
        composeRule.onNodeWithContentDescription(NavigationRoute.Sale.title!!).performClick()
        ('a'..'j').forEachIndexed { index, c ->

            composeRule
                .onNodeWithContentDescription("add_sale")
                .performClick()
            composeRule
                .onNodeWithTag(TestTag.SALE_ITEM_BOTTOM_SHEET_LAZY_COLUMN)
                .performScrollToNode(
                matcher = SemanticsMatcher.expectValue(key = SaleKey, expectedValue = c.toString())
            )
            composeRule
                .onNodeWithTag(c.toString())
                .performClick()
            composeRule
                .onNodeWithTag(TestTag.SALE_NUMBER)
                .performTextInput((c.code*(1..4)
                    .random()).toString())
            composeRule
                .onNodeWithTag(TestTag.SALE_ADD)
                .performClick()

            // assert sale inventory is displayed
            composeRule
                .onNodeWithTag(SALE_ITEM_LAZY_COLUMN)
                .performScrollToNode(
                    matcher = SemanticsMatcher.expectValue(key = SaleLazyKey, expectedValue = c.toString())
                )
            composeRule
                .onNodeWithTag(c.toString()+"@")
                .assertIsDisplayed()
        }
    }





}