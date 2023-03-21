package com.example.storeaccounting.presentation.component

import android.text.Spannable.Factory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeaccounting.ui.theme.custom_blue_3
import com.example.storeaccounting.ui.theme.persian_font_medium

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hint: String = "",
    _text: String,
    onText:(String) -> Unit,
){
    var text by remember {
        mutableStateOf(_text)
    }
    var isHintDisplayed by remember {
        mutableStateOf(text == "")
    }
    LaunchedEffect(key1 = _text){
        if (_text == ""){
            isHintDisplayed = true
            text = _text
        }else{
            isHintDisplayed = false
            text = _text
        }
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
                    },
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
fun FactorEditText(
    modifier: Modifier = Modifier,
    editTextModifier: Modifier = Modifier,
    hint: String = "",
    _text: String,
    keyboardOptions: KeyboardOptions,
    onText:(String) -> Unit,
){
    var text by remember {
        mutableStateOf(_text)
    }
    var isHintDisplayed by remember {
        mutableStateOf(text == "")
    }
    LaunchedEffect(key1 = _text){
        if (_text == ""){
            isHintDisplayed = true
            text = _text
        }else{
            isHintDisplayed = false
            text = _text
        }
    }
    RightToLeftLayout {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onText(it)
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = keyboardOptions,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontFamily = persian_font_medium,
                    fontSize = 14.sp
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.primaryVariant,
                    1.00f to MaterialTheme.colors.primaryVariant
                ),
                modifier = editTextModifier
                    .fillMaxWidth()
                    .padding(
                        all = 8.dp
                    )
                    .onFocusChanged {
                        if (text.isEmpty()) {
                            isHintDisplayed = !it.isFocused
                        }
                    }
                    .align(Alignment.Center)
            )
            if(isHintDisplayed){
                Text(
                    text = hint,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )
            }
        }
    }
}


@Composable
fun DescriptionEditText(
    modifier: Modifier = Modifier,
    hint: String = "",
    _text: String,
    onText:(String) -> Unit,
){
    var text by remember {
        mutableStateOf(_text)
    }
    var isHintDisplayed by remember {
        mutableStateOf(text == "")
    }
    LaunchedEffect(key1 = _text){
        if (_text == ""){
            isHintDisplayed = true
            text = _text
        }else{
            isHintDisplayed = false
            text = _text
        }
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
                    .fillMaxWidth().height(height = 150.dp)
                    .background(Color.Transparent, CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(size = 15.dp)
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                    .onFocusChanged {
                        if (text.isEmpty()) {
                            isHintDisplayed = !it.isFocused
                        }
                    },
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