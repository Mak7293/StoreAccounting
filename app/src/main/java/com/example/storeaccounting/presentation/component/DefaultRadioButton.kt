package com.example.storeaccounting.presentation.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storeaccounting.ui.theme.persian_font_regular

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: ()  ->  Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier
                .padding(all = 5.dp),
        textAlign = TextAlign.Start,
        text = text,
        color = MaterialTheme.colors.onPrimary,
        fontFamily = persian_font_regular,
        fontSize = 16.sp
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = if(isSystemInDarkTheme()) Color(0xFFFF5722) else Color(0xFFFFE925),
                unselectedColor = MaterialTheme.colors.onPrimary
            ),
            modifier = Modifier.semantics {
                contentDescription = text
            }
        )
    }
}