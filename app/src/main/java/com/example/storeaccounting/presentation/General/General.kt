package com.example.storeaccounting.presentation.General

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.ui.theme.persian_font_regular

@Composable
fun General(
    parentNavController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        PersonCreditCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            parentNavController = parentNavController
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp
                ),
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

@Composable
fun PersonCreditCard(
    modifier: Modifier = Modifier,
    parentNavController: NavController
) {
    RightToLeftLayout {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onSurface,
                    shape = CircleShape
                )
                .border(
                    color = MaterialTheme.colors.primaryVariant,
                    width = 1.5.dp,
                    shape = CircleShape
                )
                .padding(all = 10.dp)
                .clickable {
                    parentNavController.navigate(route = NavigationRoute.AddEditCreditCard.route )
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    textAlign = TextAlign.Center,
                    text = "اضافه کردن کارت اعتباری:",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(width = 5.dp))
                Icon(
                    modifier = Modifier
                        .border(
                        color = MaterialTheme.colors.primaryVariant,
                        width = 1.5.dp,
                        shape = CircleShape
                        )
                        .size(24.dp)
                    ,
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.primaryVariant
                )
            }

        }
    }
}