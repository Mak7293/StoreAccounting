package com.example.storeaccounting.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.storeaccounting.presentation.util.NavigationRoute
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeaccounting.ui.theme.custom_gray_4
import com.example.storeaccounting.ui.theme.persian_font_medium


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val menuItems = listOf(
        NavigationRoute.Inventory,
        NavigationRoute.Sale,
        NavigationRoute.General,
        NavigationRoute.Setting,
    )
    BottomNavigation(
        contentColor = MaterialTheme.colors.surface,
        backgroundColor = MaterialTheme.colors.onSurface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            BottomNavigationItem(
                label = {
                    Text(text = it.title!!)
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.surface,
                unselectedContentColor = MaterialTheme.colors.secondaryVariant,
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route){
                        navController.graph.startDestinationRoute?.let{ route  ->
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = it.icon!!, contentDescription = it.title) }
            )
        }
    }
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hint: String = "",
    onText:(String) -> Unit
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(true)
    }
    RightToLeftLayout {
        Box(modifier = modifier){
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onText(it)
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.primaryVariant,
                    1.00f to MaterialTheme.colors.primaryVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent, CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                    .onFocusChanged {
                        if (text.isEmpty()) {
                            isHintDisplayed = !it.isFocused
                        }
                    }
            )
            if(isHintDisplayed){
                Text(
                    text = hint,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(
                        horizontal = 20.dp ,
                        vertical = 12.dp
                    )
                )
            }
        }
    }
}

@Composable
fun RightToLeftLayout(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}