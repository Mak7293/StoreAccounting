package com.example.storeaccounting.presentation.sale

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.Event
import com.example.storeaccounting.presentation.view_model.ViewModel
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular
import com.example.storeaccounting.ui.theme.persian_font_semi_bold
import saman.zamani.persiandate.PersianDateFormat

@Composable
fun Sale(
    viewModel: ViewModel = hiltViewModel(),
    onClick:(FabRoute)  -> Unit
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClick(FabRoute.SaleFab)
                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        scaffoldState = scaffoldState
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(height = 250.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 35.dp,
                        bottomStart = 35.dp
                    )
                )
            ){

            }
            SaleContent()
        }
    }
}

@Composable
fun SaleContent(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaleBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel(),
    saleList: List<InventoryEntity>,
    context: Context = LocalContext.current,
    onCancel:() ->  Unit
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        val currentDate = PersianDateFormat("Y/m/d")
            .format(viewModel.getPersianDate()).toString()
        var number by remember {
            mutableStateOf("")
        }
        var inventoryEntity = remember {
            mutableStateOf<InventoryEntity?>(null)
        }
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 5.dp),
                    textAlign = TextAlign.Center,
                    text = "کالایی که میخواهید بفروشید را از لیست کالاهای موجود انتخاب کنید.",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentPadding = PaddingValues(10.dp),
            ){
                items(
                    count = saleList.size,
                    key = { it }
                ){
                    SaleItem(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 1500
                            )
                        ),
                        item = saleList[it],
                        isSelected = inventoryEntity.value == saleList[it]
                    ){
                        if(it == inventoryEntity.value){
                            inventoryEntity.value = null
                        }else{
                            inventoryEntity.value = it
                        }
                    }
                }
            }
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 5.dp),
                    textAlign = TextAlign.Start,
                    text = "نام کالا: ${inventoryEntity.value?.title ?: ""}",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
            }
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 15.dp),
                    textAlign = TextAlign.Start,
                    text = "تعداد کالا را وارد کنید:",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
            }
            EditText(
                hint ="تعداد کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                _text = ""
            ){
                number = it
            }
            RightToLeftLayout {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 15.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "date_icon")
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp),
                        text = currentDate ,
                        color = MaterialTheme.colors.primaryVariant,
                        fontFamily = persian_font_regular,
                        fontSize = 18.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(
                    modifier = Modifier.width(175.dp),
                    onClick = {
                        onCancel()
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF013A63))
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "خارج شدن",
                        color = Color.White,
                        fontFamily = persian_font_medium,
                        fontSize = 16.sp
                    )
                }
                Button(
                    modifier = Modifier.width(175.dp),
                    onClick = {
                        if(inventoryEntity != null){
                            val newInventoryEntity = InventoryEntity(
                                id = inventoryEntity.value?.id,
                                createdTimeStamp = inventoryEntity.value!!.createdTimeStamp,
                                date = currentDate,
                                timeStamp = System.currentTimeMillis(),
                                title = inventoryEntity.value!!.title,
                                number = number,
                                sellPrice = inventoryEntity.value!!.sellPrice,
                                buyPrice = inventoryEntity.value!!.buyPrice,
                            )
                            viewModel.onEvent(Event.SaleInventory(
                                newInventoryEntity = newInventoryEntity,
                                previousInventory = inventoryEntity.value!!
                            ))
                        }else {
                            Toast.makeText(context,"لطفا ابتدا کالا مورد نظر را انتخاب کنید."
                                ,Toast.LENGTH_SHORT).show()
                        }

                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF008506))
                    ){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "فروش کالا",
                        color = Color.White,
                        fontFamily = persian_font_medium,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SaleItem(
    modifier: Modifier = Modifier,
    item: InventoryEntity,
    verticalPadding: Dp = 5.dp,
    horizontalPadding: Dp = 15.dp,
    contentPadding: Dp = 10.dp,
    isSelected: Boolean = false,
    onSelect:(InventoryEntity) -> Unit
){
    RightToLeftLayout {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
                .background(
                    brush = if (isSystemInDarkTheme()) {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF3D00),
                                Color(0xFFFFE500)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF009C07),
                                Color(0xFF63FF6A)
                            )
                        )
                    },
                    shape = CircleShape
                )
                .clickable {
                    onSelect(item)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = contentPadding),
                text = item.title,
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontFamily = persian_font_semi_bold,
                fontSize = 16.sp
            )
            Box(contentAlignment = Alignment.Center){

                Icon(
                    modifier = Modifier
                        .size(size = 35.dp)
                        .padding(end =  5.dp),
                    imageVector = Icons.Default.CheckBoxOutlineBlank ,
                    contentDescription = "Add",
                    tint = Color.Black
                )
                if (isSelected){
                    Icon(
                        modifier = Modifier
                            .size(size = 25.dp)
                            .padding(end =  5.dp),
                        imageVector =  Icons.Default.Check,
                        contentDescription = "Add",
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun TestPreview(){
    val inventoryEntity = InventoryEntity(
        id = 0,
        createdTimeStamp = 3516516,
        title = ";alksdf",
        timeStamp = 6458161,
        date = ";laskdf",
        sellPrice = "54516",
        buyPrice = "5454656",
        number = "541"
    )
    SaleItem(
        modifier = Modifier,
        item = inventoryEntity,
        contentPadding = 5.dp,
        verticalPadding = 5.dp,
        isSelected = true
    ){

    }
}