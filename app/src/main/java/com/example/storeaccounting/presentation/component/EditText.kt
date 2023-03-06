package com.example.storeaccounting.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeaccounting.ui.theme.persian_font_medium

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hint: String = "",
    _text: String,
    onText:(String) -> Unit,
){
    Log.d("edit",_text)
    var text by remember {
        mutableStateOf(_text)
    }
    var isHintDisplayed by remember {
        mutableStateOf(text == "")
    }
    Log.d("edit1",text)

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