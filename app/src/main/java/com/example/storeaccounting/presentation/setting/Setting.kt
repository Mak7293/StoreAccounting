package com.example.storeaccounting.presentation.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.presentation.component.DefaultRadioButton
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.Constants.THEME_KEY
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.setting.SettingEvent
import com.example.storeaccounting.presentation.view_model.setting.SettingViewModel
import com.example.storeaccounting.ui.theme.custom_blue_2
import com.example.storeaccounting.ui.theme.persian_font_regular
import com.example.storeaccounting.ui.theme.persian_font_semi_bold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun Setting() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                all = 20.dp
            ),
        contentAlignment = Alignment.Center
    ){
        content(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    ),
                    shape = RoundedCornerShape(
                        size = 20.dp
                    )
                )
                .border(
                    color = if (isSystemInDarkTheme()) Color.Yellow else Color(0xFFFF5722),
                    width = 1.5.dp,
                    shape = RoundedCornerShape(
                        size = 20.dp
                    )
                )
        )
    }
}

@Composable
fun content(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    var currentTheme by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true){
        viewModel.readCurrentThemeForDataStore().collectLatest {
            currentTheme = it
        }

    }
    val scope = rememberCoroutineScope()
    RightToLeftLayout {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 5.dp,
                        horizontal = 10.dp
                    ),
                textAlign = TextAlign.Start,
                text = "تنظیمات تم:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DefaultRadioButton(
                    text = "روز",
                    selected = currentTheme == ThemeState.ThemeDay.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeDay.theme))
                    }
                )
                DefaultRadioButton(
                    text = "شب",
                    selected = currentTheme == ThemeState.ThemeNight.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeNight.theme))
                    }
                )
                DefaultRadioButton(
                    text = "گوشی",
                    selected = currentTheme == ThemeState.ThemeDefault.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeDefault.theme))
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "ارتباط با توسعه دهنده:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "باز کردن پوشه فاکتور ها:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "اضاقه کردن امضا به فاکتور:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "صفحه اپلیکیشن در کافه بازار:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
        }
    }
}