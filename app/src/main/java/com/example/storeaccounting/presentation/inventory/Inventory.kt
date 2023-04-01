package com.example.storeaccounting.presentation.inventory

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.core.TestTag
import com.example.storeaccounting.core.TestTag.INVENTORY_ITEM_LAZY_COLUMN
import com.example.storeaccounting.core.TestTag.inventoryLazyTitle
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.presentation.component.CustomAcceptRefuseDialog
import com.example.storeaccounting.presentation.component.CustomHistoryDialog
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleEvent
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular
import com.example.storeaccounting.ui.theme.persian_font_semi_bold
import kotlinx.coroutines.flow.collectLatest
import saman.zamani.persiandate.PersianDateFormat

@Composable
fun Inventory(
    context: Context = LocalContext.current,
    onClick: (FabRoute, InventoryEntity?) ->  Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClick(FabRoute.InventoryFab,null)
                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add_inventory",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        scaffoldState = scaffoldState
    ){
        InventoryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){  InventoryEntity ->
            onClick(FabRoute.InventoryFab,InventoryEntity)
        }
    }
}


@Composable
fun InventoryContent(
    modifier : Modifier = Modifier,
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    onData:(InventoryEntity) -> Unit
){
    val showDeleteDialog =  remember {
        mutableStateOf(false)
    }
    val showHistoryDialog =  remember {
        mutableStateOf(false)
    }
    val inventory = remember {
        mutableStateOf(InventoryEntity())
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ){
        InventoryHistory(
            modifier =Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
        )
        InventoryList(
            showDeleteCustomDialog = {
                inventory.value = it
                showDeleteDialog.value = true
            },
            showEditBottomSheet = {

                onData(it)
            },
            showHistoryCustomDialog = {
                inventory.value = it
                showHistoryDialog.value = true
            }
        )
    }
    if(showHistoryDialog.value){
        CustomHistoryDialog(
            modifier = Modifier
                .height(600.dp)
                .width(400.dp),
            historyList = produceState<List<History>>(initialValue = emptyList()){
                       value = inventorySaleViewModel.getHistoryByInventory(inventory.value.createdTimeStamp)
                           .history.sortedByDescending {it.timeStamp }
            }.value ,
            setShowDialog = {
                showHistoryDialog.value = it
            }
        )
    }

    if (showDeleteDialog.value) {
        CustomAcceptRefuseDialog(
            modifier = Modifier
                .width(350.dp)
                .height(175.dp),
            setShowDialog = {
                showDeleteDialog.value = it
            },
            title = "حذف کردن کالا از لیست",
            content = "حذف این کالا سبب از بین رفتن تاریخچه آن نیز می شود." +
                    " آیا مطمئن هستید که میخواهید این کالا را از لیست حذف کنید؟",
            positiveButtonTitle = "حذف کن",
            negativeButtonTitle = "خارج شدن",
            positiveButtonColor = Color.Red,
            negativeButtonColor = Color.Green,
            onSuccess = {
                Log.d("delete",inventory.value.title)
                inventorySaleViewModel.onEvent(InventorySaleEvent.DeleteInventory(inventory.value))
                showDeleteDialog.value = false
            },
            onCancel = {
                showDeleteDialog.value = false
            }
        )
    }
}
@Composable
fun InventoryHistory(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    viewModel: InventorySaleViewModel = hiltViewModel()
){
    var text by remember {
        mutableStateOf("")
    }

    RightToLeftLayout {
        Column(
            modifier = modifier.padding(contentPadding),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
        ){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                textAlign = TextAlign.Start,
                text = "لیست کالا های فروشگاه:",
                color = MaterialTheme.colors.primaryVariant,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            EditText(
                hint =  "نام کالا را جهت جست و جو وارد کنید...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                _text = "",
            ){
                text = it
                viewModel.onEvent(InventorySaleEvent.FilterInventory(it))
            }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .width(2.dp),
                color = if(isSystemInDarkTheme()) Color.Gray else Color.DarkGray
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InventoryList(
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    showDeleteCustomDialog: (InventoryEntity) -> Unit,
    showEditBottomSheet: (InventoryEntity)  -> Unit,
    showHistoryCustomDialog: (InventoryEntity)  -> Unit
){
    Log.d("InventoryRecomposition1","@@@@@@@@")
    val inventoryList = remember {
        mutableStateOf(inventorySaleViewModel.state.value.filteredInventory.sortedBy { it.title })
    }
    inventoryList.value = inventorySaleViewModel.state.value.filteredInventory
    LaunchedEffect(key1 = true){
        inventorySaleViewModel.eventFlow.collectLatest { event  ->
            when(event){
                is InventorySaleViewModel.InventorySaleUiEvent.FilteredInventoryList   ->  {
                    inventoryList.value = inventorySaleViewModel.state.value.filteredInventory
                }
                else -> {

                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .testTag(INVENTORY_ITEM_LAZY_COLUMN),
        contentPadding = PaddingValues(16.dp),
    ){
        items(
            count = inventoryList.value.size,
            key = { it }
        ){
            InventoryItem(
                modifier = Modifier
                    .animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                    .semantics {
                        inventoryLazyTitle = inventoryList.value[it].title
                    }
                    .testTag(inventoryList.value[it].title),
                item = inventoryList.value[it],
                verticalPadding = 8.dp,
                contentPadding = 8.dp,
                onEdit = {  Inventory ->
                    showEditBottomSheet(Inventory)
                },
                onDelete = { Inventory ->
                    showDeleteCustomDialog(Inventory)
                },
                onHistory = { Inventory ->
                    showHistoryCustomDialog(Inventory)
                }
            )
        }
    }
}
@Composable
fun AddEditInventoryBottomSheetContent(
    modifier: Modifier = Modifier,
    inventory: InventoryEntity?,
    inventorySaleViewModel: InventorySaleViewModel,
    onDismiss:() -> Unit
) {
    val currentDate = PersianDateFormat("Y/m/d")
        .format(inventorySaleViewModel.getPersianDate()).toString()

    var title by remember {
        mutableStateOf("")
    }
    var number by remember {
        mutableStateOf("")
    }
    var sellPrice by remember {
        mutableStateOf("")
    }
    var buyPrice by remember {
        mutableStateOf("")
    }
    LaunchedEffect(
        inventory?.title,
        inventory?.number,
        inventory?.buyPrice,
        inventory?.sellPrice
    ){
        if(title != inventory?.title ){
            title = inventory?.title ?: ""
        }
        if(number != inventory?.number ){
            number = inventory?.number ?: ""
        }
        if(sellPrice != inventory?.sellPrice ){
            sellPrice = inventory?.sellPrice ?: ""
        }
        if(buyPrice != inventory?.buyPrice ){
            buyPrice = inventory?.buyPrice ?: ""
        }
    }
    Log.d("iTitle",title)
    Log.d("iNumber",number)
    Log.d("iBuyprice",buyPrice)
    Log.d("iSellprice",sellPrice)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    textAlign = TextAlign.Center,
                    text = if(inventory == null) "نام و تعداد کالا را وارد کنید:" else "نام و تعداد کالا را ویرایش کنید:",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
            }
            EditText(
                hint =  "نام کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                _text = inventory?.title ?: title,
                testTag = TestTag.INVENTORY_NAME
            ){
                title = it
            }
            EditText(
                hint ="تعداد کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                _text = inventory?.number ?: number,
                testTag = TestTag.INVENTORY_NUMBER
            ){
                number = it
            }
            EditText(
                hint = "قیمت خرید ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                _text = inventory?.buyPrice ?: buyPrice,
                testTag = TestTag.INVENTORY_BUY_PRICE
            ){
                buyPrice = it
            }
            EditText(
                hint ="قیمت فروش ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                _text = inventory?.sellPrice ?: sellPrice,
                testTag = TestTag.INVENTORY_SELL_PRICE
            ){
                sellPrice = it
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
                        contentDescription = "date_icon",
                        tint = MaterialTheme.colors.primaryVariant)
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
                    modifier = Modifier
                        .width(175.dp),
                    onClick = {
                        onDismiss()
                        title = ""
                        number = ""
                        buyPrice = ""
                        sellPrice = ""
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
                    modifier = Modifier
                        .width(175.dp)
                        .testTag(TestTag.INVENTORY_ADD),
                    onClick = {
                        val inventoryEntity = InventoryEntity(
                            id = inventory?.id,
                            createdTimeStamp = inventory?.createdTimeStamp ?: System.currentTimeMillis(),
                            date = currentDate,
                            timeStamp = System.currentTimeMillis(),
                            title = title,
                            number = number,
                            sellPrice = sellPrice,
                            buyPrice = buyPrice,
                        )
                        if(inventory == null){
                            inventorySaleViewModel.onEvent(InventorySaleEvent.InsertInventory(inventoryEntity))
                        }else{
                            inventorySaleViewModel.onEvent(InventorySaleEvent.UpdateInventory(inventoryEntity))
                        }
                        title = ""
                        number = ""
                        buyPrice = ""
                        sellPrice = ""
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF008506))

                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = if(inventory == null) "ذخیره کردن" else "به روزرسانی",
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
fun InventoryItem(
    modifier: Modifier = Modifier,
    item: InventoryEntity,
    verticalPadding: Dp,
    contentPadding: Dp,
    onEdit: (InventoryEntity) -> Unit,
    onDelete: (InventoryEntity)  -> Unit,
    onHistory: (InventoryEntity)  -> Unit
){
    RightToLeftLayout {
        Box(
            modifier = modifier
                .padding(vertical = verticalPadding)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    )
                )
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.padding(contentPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text ="نام کالا: ${item.title}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text ="تعداد کالا: ${item.number}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text ="قیمت خرید کالا: ${item.buyPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text ="قیمت فروش کالا: ${item.sellPrice}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ){
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Transparent
                            )
                    ){
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text ="حاشیه سود کالا: ${item.sellPrice.toLong() - item.buyPrice.toLong()}",
                            fontFamily = persian_font_semi_bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary,
                        )
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .offset(x = (-20).dp)
                                .size(40.dp)
                                .background(
                                    color = Color.Green,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = CircleShape
                                )
                                .padding(6.dp)
                                .clickable {
                                    onEdit(item)
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .offset(x = (-10).dp)
                                .size(40.dp)
                                .background(
                                    color = Color.Red,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = CircleShape
                                )
                                .padding(6.dp)
                                .clickable {
                                    onDelete(item)
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Yellow,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = CircleShape
                                )
                                .padding(6.dp)
                                .clickable {
                                    onHistory(item)
                                }
                        )
                    }
                }
            }
        }
    }
}
