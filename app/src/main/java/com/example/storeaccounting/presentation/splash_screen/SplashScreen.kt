package com.example.storeaccounting.presentation.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.R
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.ui.theme.persian_font_semi_bold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    parentNavController: NavController
) {
    LaunchedEffect(key1 = true ){
        delay(1500L)
        parentNavController.navigate(
            route = NavigationRoute.Main.route,
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            modifier = Modifier
                .size(
                    size = 200.dp
                )
                .background(color = Color.LightGray, shape = CircleShape)
                .padding(all=10.dp)
            ,
            painter = painterResource(
                id = R.drawable.ic_splash_screen
            ),
            contentDescription = "splash_screen"
        )
        Spacer(modifier = Modifier.height(12.dp))
        RightToLeftLayout {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "حسابداری فروشگاهی V1.0",
                color = MaterialTheme.colors.primaryVariant,
                fontFamily = persian_font_semi_bold,
                fontSize = 18.sp,
            )
        }
    }
}
