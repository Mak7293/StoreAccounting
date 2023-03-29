package com.example.storeaccounting.presentation.component

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular
import com.example.storeaccounting.ui.theme.persian_font_semi_bold

@Composable
fun CustomAcceptRefuseDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    positiveButtonTitle: String,
    negativeButtonTitle: String,
    positiveButtonColor: Color,
    negativeButtonColor: Color,
    onSuccess:() -> Unit,
    onCancel:()  -> Unit,
    setShowDialog: (Boolean) -> Unit
) {
    RightToLeftLayout {
        Dialog(
            onDismissRequest = {
                setShowDialog(true)
            }
        ){
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(16.dp)
                        )
                ){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        text = title,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primaryVariant,
                        fontFamily = persian_font_regular,
                        fontSize = 18.sp
                    )
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .width(2.dp)
                        .padding(horizontal = 5.dp),
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        text = content,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primaryVariant,
                        fontFamily = persian_font_regular,
                        fontSize = 16.sp
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Button(
                            modifier = Modifier
                                .width(120.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = positiveButtonColor
                            ),
                            onClick = {
                                onSuccess()
                            }
                        ){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = positiveButtonTitle,
                                color = Color.Black,
                                fontFamily = persian_font_medium,
                                fontSize = 14.sp
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(120.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = negativeButtonColor
                            ),
                            onClick = {
                                onCancel()
                            }
                        ){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = negativeButtonTitle,
                                color = Color.Black,
                                fontFamily = persian_font_medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomContentDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    setShowDialog: (Boolean) -> Unit,
    onCancel:()  -> Unit,
) {
    RightToLeftLayout {
        Dialog(
            onDismissRequest = {
                setShowDialog(true)
            }
        ){
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(16.dp)
                        )
                ){
                    Box (
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.Center),
                            text = title,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primaryVariant,
                            fontFamily = persian_font_regular,
                            fontSize = 18.sp
                        )
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                                .offset(x = (-10).dp)
                                .clickable {
                                    onCancel()
                                }
                            ,
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(2.dp)
                            .padding(horizontal = 5.dp),
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Column(modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                    ){
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            text = content,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colors.primaryVariant,
                            fontFamily = persian_font_regular,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomHistoryDialog(
    modifier: Modifier = Modifier,
    historyList: List<History>,
    setShowDialog: (Boolean) -> Unit
){
    Log.d("list",historyList.toString())
    RightToLeftLayout {
        Dialog(
            onDismissRequest = {
                setShowDialog(false)
            }
        ){
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp)
            ){
                LazyColumn(
                    modifier = modifier.background(
                        Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.background,
                            MaterialTheme.colors.secondary
                        ))
                    ),
                    contentPadding = PaddingValues(16.dp),

                ){
                    items(
                        count = historyList.size,
                    ){
                        when(historyList[it].transaction){
                            TransactionState.Create.state -> {
                                CreateHistoryItem(
                                    item = historyList[it] ,
                                    verticalPadding = 10.dp,
                                    contentPadding = 8.dp
                                )

                            }
                            TransactionState.Edit.state -> {
                                EditHistoryItem(
                                    item = historyList[it],
                                    _item = historyList[it+1],
                                    verticalPadding = 10.dp ,
                                    contentPadding = 8.dp
                                )
                            }
                            TransactionState.Sale.state -> {
                                SaleHistoryItem(
                                    item = historyList[it],
                                    verticalPadding = 10.dp,
                                    contentPadding = 8.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CreateHistoryItem(
    item: History,
    verticalPadding: Dp,
    contentPadding: Dp,
){
    RightToLeftLayout {
        Column(
            modifier = Modifier
                .padding(vertical = verticalPadding)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = Color(0xFF00B808)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column{
                    Text(
                        text ="نام کالا: ${item.title}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="تعداد کالا: ${item.remainingInventory}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="قیمت خرید کالا: ${item.buyPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="قیمت فروش کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="حاشیه سود کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

            }
            Divider(
                modifier = Modifier
                    .width(600.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    text ="تراکنش : ایجاد کالا",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    text ="${item.date}",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun EditHistoryItem(
    item: History,
    _item: History,
    verticalPadding: Dp,
    contentPadding: Dp,
){
    RightToLeftLayout {
        Column(
            modifier = Modifier
                .padding(vertical = verticalPadding)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = Color(0xFFFFCD36)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(modifier = Modifier.weight(1.0f)){
                    Text(
                        text ="نام کالا: ${item.title}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = Color(0xFF006D04)
                    )
                    Text(
                        text ="تعداد کالا: ${item.remainingInventory}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color(0xFF006D04)
                    )
                    Text(
                        text ="قیمت خرید کالا: ${item.buyPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color(0xFF006D04)
                    )
                    Text(
                        text ="قیمت فروش کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color(0xFF006D04)
                    )
                    Text(
                        text ="حاشیه سود کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color(0xFF006D04)
                    )
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(
                        modifier = Modifier.size(size = 35.dp),
                        imageVector = Icons.Default.KeyboardDoubleArrowRight,
                        contentDescription = "Add",
                        tint = Color.Black
                    )
                }
                Column(modifier = Modifier.weight(1.0f)){
                    Text(
                        text ="نام کالا: ${_item.title}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                    Text(
                        text ="تعداد کالا: ${_item.remainingInventory}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                    Text(
                        text ="قیمت خرید کالا: ${_item.buyPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                    Text(
                        text ="قیمت فروش کالا: ${_item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                    Text(
                        text ="حاشیه سود کالا: ${_item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }

            }
            Divider(
                modifier = Modifier
                    .width(600.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    text ="تراکنش : به روز رسانی",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    text ="${item.date}",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun SaleHistoryItem(
    item: History,
    verticalPadding: Dp,
    contentPadding: Dp,
){
    RightToLeftLayout {
        Column(
            modifier = Modifier
                .padding(vertical = verticalPadding)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = Color(0xFF03A9F4)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column{
                    Text(
                        text ="نام کالا: ${item.title}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="تعداد کالا فروخته شده: ${item.saleNumber}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="تعداد کالا باقی مانده: ${item.remainingInventory}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="قیمت خرید کالا: ${item.buyPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="قیمت فروش کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="حاشیه سود کالا: ${item.sellPrice.toInt() - item.buyPrice.toInt()}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="سود حاصل از قروش کالا: ${(item.sellPrice.toInt() - item.buyPrice.toInt()) * item.saleNumber.toInt()}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

            }
            Divider(
                modifier = Modifier
                    .width(600.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    text ="تراکنش : قروش کالا",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    text ="${item.date}",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
//@Preview(showBackground = true)
@Composable
fun TestPreview() {
    val history = History(
        id = 0,
        remainingInventory = 5,
        createdTimeStamp = 3516516,
        transaction = TransactionState.Create.state,
        title = ";alksdf",
        timeStamp = 6458161,
        date = ";laskdf",
        sellPrice = "54516",
        buyPrice = "5454656",
        saleNumber = "541"
    )
    EditHistoryItem(item = history,_item = history, verticalPadding = 10.dp , contentPadding =  10.dp)
}