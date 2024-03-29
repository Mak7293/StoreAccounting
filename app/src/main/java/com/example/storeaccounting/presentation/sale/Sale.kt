package com.example.storeaccounting.presentation.sale

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.core.TestTag
import com.example.storeaccounting.core.TestTag.CLOSE_SALE_BOTTOM_SHEET
import com.example.storeaccounting.core.TestTag.SALE_BOTTOM_SHEET
import com.example.storeaccounting.core.TestTag.SALE_ITEM_BOTTOM_SHEET_LAZY_COLUMN
import com.example.storeaccounting.core.TestTag.SALE_ITEM_LAZY_COLUMN
import com.example.storeaccounting.core.TestTag.saleLazyTitle
import com.example.storeaccounting.core.TestTag.saleTitle
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.presentation.component.CustomAcceptRefuseDialog
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.component.date_picker.PersianDataPicker
import com.example.storeaccounting.presentation.util.Constants.DAY
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleEvent
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular
import com.example.storeaccounting.ui.theme.persian_font_semi_bold
import com.razaghimahdi.compose_persian_date.core.PersianDataPickerController
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import com.example.storeaccounting.presentation.util.Constants.FROM
import com.example.storeaccounting.presentation.util.Constants.MONTH
import com.example.storeaccounting.presentation.util.Constants.TEN_DAYS_GRAPH
import com.example.storeaccounting.presentation.util.Constants.THIRTY_DAYS_GRAPH
import com.example.storeaccounting.presentation.util.Constants.UNTIL
import com.example.storeaccounting.presentation.util.Constants.YEAR
import com.patrykandpatrick.vico.compose.axis.axisGuidelineComponent
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.axisLineComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.entry.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Sale(
    inventorySaleViewModel: InventorySaleViewModel,
    onEdit: (History,FabRoute) -> Unit,
    onClick:(FabRoute)  -> Unit,
){
    Log.d("SaleRecomposition","@@@@@@@@")

    val saleHistoryList = remember {
        mutableStateOf(inventorySaleViewModel.state.value.filteredHistory.filter {
            it.transaction == TransactionState.Sale.state
        })
    }
    LaunchedEffect(key1 = inventorySaleViewModel.state.value.filteredHistory){
        saleHistoryList.value = inventorySaleViewModel.state.value.filteredHistory.filter {
            it.transaction == TransactionState.Sale.state
        }
    }

    var graphState by remember {
        mutableStateOf(TEN_DAYS_GRAPH)
    }
    val graphList = remember(key1 =  inventorySaleViewModel.state.value.history){
        mutableStateOf<List<Number>>(
            inventorySaleViewModel.mapSaleProfitByDay(inventorySaleViewModel.graphHistoryList(
                from = System.currentTimeMillis()-(864000000),
                until = System.currentTimeMillis()
        )).values.toMutableList())
    }

    LaunchedEffect(key1 = true){
        inventorySaleViewModel.eventFlow.collectLatest { event  ->
            when(event){
                is InventorySaleViewModel.InventorySaleUiEvent.FilteredHistoryList   ->  {
                    saleHistoryList.value = inventorySaleViewModel.state.value.filteredHistory.filter {
                        it.transaction == TransactionState.Sale.state
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.SaleInventory  ->  {
                    delay(500L)
                    if(graphState == TEN_DAYS_GRAPH){
                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(inventorySaleViewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(864000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }else if (graphState == THIRTY_DAYS_GRAPH){
                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(inventorySaleViewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(2592000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.DeleteSaleHistory  ->  {
                    delay(1000L)
                    if(graphState == TEN_DAYS_GRAPH){
                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(inventorySaleViewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(864000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }else if (graphState == THIRTY_DAYS_GRAPH){
                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(inventorySaleViewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(2592000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }
                }
                else  ->  {

                }
            }
        }
    }
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
                    contentDescription = "add_sale",
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(height = 250.dp)
                .padding(bottom = 5.dp)
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

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(end = 10.dp)
                ){
                    val a = graphList.value.map { it }.toTypedArray()
                    Log.d("resultGraph",graphList.toString())
                    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(*a))
                    Chart(
                        chart =
                            columnChart(
                                columns = listOf(
                                    lineComponent(
                                        color = Color.Red,
                                        thickness = 3.dp,
                                        shape = CircleShape
                                    )
                                )
                            )
                        ,
                        chartModelProducer = remember(key1 = graphList.value) {
                            chartEntryModelProducer1
                        } ,
                        startAxis = startAxis(
                            axis = axisLineComponent(
                                strokeWidth = 2.dp,
                                strokeColor = MaterialTheme.colors.background,
                            ),
                            label = axisLabelComponent(
                                color = MaterialTheme.colors.background,
                                textSize = 14.sp,
                            ),
                            guideline = axisGuidelineComponent(
                                strokeWidth = 1.dp,
                                strokeColor = MaterialTheme.colors.onSurface,
                            )
                        ),
                        bottomAxis = bottomAxis(
                            axis = axisLineComponent(
                                strokeWidth = 2.dp,
                                strokeColor = MaterialTheme.colors.background,
                            ),
                            label = axisLabelComponent(
                                color = MaterialTheme.colors.background,
                                textSize = 14.sp,
                                textAlign = android.graphics.Paint.Align.CENTER,
                            ),
                            guideline = axisGuidelineComponent(
                                strokeWidth = 1.dp,
                                strokeColor = MaterialTheme.colors.onSurface,
                            )
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Button(
                            modifier = Modifier
                                .width(180.dp)
                                .height(40.dp),
                            onClick = {
                                if(inventorySaleViewModel.filterState.value){
                                    inventorySaleViewModel.onEvent(InventorySaleEvent.FilterSaleHistory(null))
                                }else{
                                    onClick(FabRoute.ResultFab)
                                }
                            },
                            shape = CircleShape,
                            colors =  ButtonDefaults.buttonColors(
                                backgroundColor = if(inventorySaleViewModel.filterState.value) Color.Red else Color.White,
                            ),
                            border = BorderStroke(width = 2.dp, color = Color.Black)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text =if(inventorySaleViewModel.filterState.value) "نمایش تمام فروش ها" else "فیلتر کردن تاریخ فروش ها",
                                color = Color.Black,
                                fontFamily = persian_font_semi_bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (inventorySaleViewModel.filterState.value) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "از ${
                                    PersianDateFormat("Y/m/d").format(
                                        inventorySaleViewModel.filterRange?.get(
                                            FROM
                                        )
                                    )
                                }" +
                                        " تا ${
                                            PersianDateFormat("Y/m/d").format(
                                                inventorySaleViewModel.filterRange?.get(
                                                    UNTIL
                                                )
                                            )
                                        } ",
                                color = Color.Black,
                                fontFamily = persian_font_semi_bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 1.5.dp,
                                    shape = CircleShape,
                                    color = if (graphState == TEN_DAYS_GRAPH) Color.Black else MaterialTheme.colors.onPrimary
                                )
                                .background(
                                    shape = CircleShape,
                                    color = if (graphState == TEN_DAYS_GRAPH) Color.Yellow else Color.Transparent
                                )
                                .padding(5.dp)
                                .clickable {
                                    if (graphState != TEN_DAYS_GRAPH) {
                                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(
                                            inventorySaleViewModel.graphHistoryList(
                                                from = System.currentTimeMillis() - (864000000), // 10*24*60*60*1000
                                                until = System.currentTimeMillis()
                                            )
                                        ).values.toMutableList()
                                        graphState = TEN_DAYS_GRAPH
                                    }
                                },
                            textAlign = TextAlign.Center,
                            text = " نمودار فروش 10 روز گزشته",
                            color = if (graphState == TEN_DAYS_GRAPH) Color.Black else MaterialTheme.colors.onPrimary,
                            fontFamily = persian_font_semi_bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 1.5.dp,
                                    shape = CircleShape,
                                    color = if (graphState == THIRTY_DAYS_GRAPH) Color.Black else MaterialTheme.colors.onPrimary
                                )
                                .background(
                                    shape = CircleShape,
                                    color = if (graphState == THIRTY_DAYS_GRAPH) Color.Yellow else Color.Transparent
                                )
                                .padding(5.dp)
                                .clickable {
                                    if (graphState != THIRTY_DAYS_GRAPH) {
                                        graphList.value = inventorySaleViewModel.mapSaleProfitByDay(
                                            inventorySaleViewModel.graphHistoryList(
                                                from = System.currentTimeMillis() - (2592000000),  // 30*24*60*60*1000
                                                until = System.currentTimeMillis()
                                            )
                                        ).values.toMutableList()
                                        graphState = THIRTY_DAYS_GRAPH
                                    }
                                },
                            textAlign = TextAlign.Center,
                            text = " نمودار فروش 30 روز گزشته",
                            color = if (graphState == THIRTY_DAYS_GRAPH) Color.Black else MaterialTheme.colors.onPrimary,
                            fontFamily = persian_font_semi_bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            SaleContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                saleHistoryList = saleHistoryList.value
            ){  History  ->
                onEdit(History,FabRoute.EditSaleFab)
            }
        }
    }
}
@Composable
fun SaleContent(
    modifier: Modifier = Modifier,
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    saleHistoryList: List<History>,
    onEdit: (History) -> Unit
){

    val showDeleteDialog =  remember {
        mutableStateOf(false)
    }
    var deleteHistory by remember {
        mutableStateOf<History?>(null)
    }
    LazyColumn(
        modifier = modifier
            .testTag(SALE_ITEM_LAZY_COLUMN),
        contentPadding = PaddingValues(10.dp),
    ){
        items(
            count = saleHistoryList.size
        ){
            SaleItem(
                modifier = Modifier
                    .semantics {
                        saleLazyTitle = saleHistoryList[it].title
                    }
                    .testTag(saleHistoryList[it].title + "@"),
                item = saleHistoryList[it],
                onDelete = {
                    deleteHistory = it
                    showDeleteDialog.value = true
                }, onEdit = {
                    onEdit(it)
                }
            )
        }
    }
    if (showDeleteDialog.value) {
        CustomAcceptRefuseDialog(
            modifier = Modifier
                .width(350.dp)
                .height(250.dp),
            setShowDialog = {
                showDeleteDialog.value = it
            },
            title = "حذف کردن تراکنش فروش",
            content = "حذف این تراکنش دائمی و غیر قابل بازکشت می باشد. در" +
                    " صورت حذف تعداد کالا های فروخته شده به کالا های موجود در انبار اضافه می شود." +
                    " آیا مطمئن هستید که میخواهید این تراکنش را حذف کنید؟",
            positiveButtonTitle = "حذف کن",
            negativeButtonTitle = "خارج شدن",
            positiveButtonColor = Color.Red,
            negativeButtonColor = Color.Green,
            onSuccess = {
                inventorySaleViewModel.onEvent(InventorySaleEvent.DeleteSaleHistory(deleteHistory!!))
                showDeleteDialog.value = false
            },
            onCancel = {
                showDeleteDialog.value = false
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaleBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: InventorySaleViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    oldHistory: History? = null,
    onCancel:() ->  Unit
){
    val scrollState = rememberScrollState()
    /////????//////
    scrollState.isScrollInProgress
    ////????//////
    Box(
        modifier = modifier
            .testTag(SALE_BOTTOM_SHEET)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical,
            ),
        contentAlignment = Alignment.Center
    ){
        val currentDate = PersianDateFormat("Y/m/d")
            .format(viewModel.getPersianDate()).toString()
        var number by remember {
            mutableStateOf("")
        }
        val inventoryEntity = remember {
            mutableStateOf<InventoryEntity?>(null)
        }
        val saleList = viewModel.state.value.inventory
        LaunchedEffect(key1 = oldHistory){
            if (oldHistory!=null){
                inventoryEntity.value = viewModel.getHistoryByInventory(oldHistory.createdTimeStamp).inventory
                number = oldHistory.saleNumber
            }else{
                inventoryEntity.value = null
                number = ""
            }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(rememberNestedScrollInteropConnection())
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
                    .height(175.dp)
                    .testTag(SALE_ITEM_BOTTOM_SHEET_LAZY_COLUMN),
                contentPadding = PaddingValues(10.dp),
            ){
                items(
                    count = saleList.size,
                ){
                    SaleBottomSheetItem(
                        modifier = Modifier
                            .semantics {
                                saleTitle = saleList[it].title
                            }
                            .testTag(saleList[it].title)
                            .fillMaxWidth()
                            .padding(
                                horizontal = 15.dp,
                                vertical = 5.dp
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
                        ,
                        item = saleList[it],
                        isSelected = inventoryEntity.value?.createdTimeStamp == saleList[it].createdTimeStamp
                    ){ InventoryEntity ->
                        if( InventoryEntity == inventoryEntity.value){
                            inventoryEntity.value = null
                        }else{
                            inventoryEntity.value = InventoryEntity
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
                    text = "نام کالا: ${inventoryEntity.value?.title ?: oldHistory?.title ?: ""}",
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
                hint = "تعداد کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                _text = number,
                testTag = TestTag.SALE_NUMBER
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
                        contentDescription = "date_icon",
                        tint = MaterialTheme.colors.primaryVariant
                    )
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
                        .width(175.dp)
                        .testTag(CLOSE_SALE_BOTTOM_SHEET),
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
                    modifier = Modifier
                        .width(175.dp)
                        .testTag(TestTag.SALE_ADD),
                    onClick = {
                        if(inventoryEntity.value != null){
                            try{
                                val newInventoryEntity = inventoryEntity.value!!.copy(
                                    date = currentDate,
                                    timeStamp = System.currentTimeMillis(),
                                    number = (inventoryEntity.value!!.number.toLong() - number.toLong()).toString()
                                )
                                val newHistory = History(
                                    id = oldHistory?.id ,
                                    createdTimeStamp = newInventoryEntity.createdTimeStamp,
                                    remainingInventory =
                                    if(newInventoryEntity.createdTimeStamp == oldHistory?.createdTimeStamp){
                                        newInventoryEntity.number.toLong() + oldHistory.saleNumber.toLong()
                                    }else{
                                        newInventoryEntity.number.toLong()
                                    },
                                    transaction = TransactionState.Sale.state,
                                    title = newInventoryEntity.title,
                                    saleNumber = number,
                                    buyPrice = newInventoryEntity.buyPrice,
                                    sellPrice = newInventoryEntity.sellPrice,
                                    timeStamp = newInventoryEntity.timeStamp,
                                    date = newInventoryEntity.date
                                )
                                if(oldHistory == null){
                                    viewModel.onEvent(
                                        InventorySaleEvent.SaleInventory(
                                        inventoryEntity = newInventoryEntity,
                                        history = newHistory
                                    ))
                                }else{
                                    viewModel.onEvent(
                                        InventorySaleEvent.UpdateSaleTransaction(
                                        inventoryEntity = newInventoryEntity,
                                        newHistory = newHistory,
                                        oldHistory = oldHistory))
                                }
                                inventoryEntity.value = null
                                number = ""
                            }catch (e: java.lang.NumberFormatException){
                                Toast.makeText(context,"تعداد کالا تنها می تواند عدد باشد.",Toast.LENGTH_SHORT).show()
                            }

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
                        text = if(oldHistory == null) "فروش کالا" else "به روز رسانی",
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
fun SaleBottomSheetItem(
    modifier: Modifier = Modifier,
    item: InventoryEntity,
    contentPadding: Dp = 10.dp,
    isSelected: Boolean = false,
    onSelect:(InventoryEntity) -> Unit
){
    RightToLeftLayout {
        Row(
            modifier = modifier
                .clickable {
                    Log.d("Click","AA")
                    onSelect(item)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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
                        .padding(end = 5.dp),
                    imageVector = Icons.Default.CheckBoxOutlineBlank ,
                    contentDescription = item.title,
                    tint = Color.Black
                )
                if (isSelected){
                    Icon(
                        modifier = Modifier
                            .size(size = 25.dp)
                            .padding(end = 5.dp),
                        imageVector =  Icons.Default.Check,
                        contentDescription = "Add",
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
fun ResultBottomSheetContent(
    modifier: Modifier = Modifier,
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    controller: PersianDataPickerController,
    onCancel:() ->  Unit,
    onResult:(Map<String,Long>) ->  Unit,
){
    val currentDate = inventorySaleViewModel.getPersianDate()
    var btnDate by remember {
        mutableStateOf(mutableMapOf<String,Boolean>(FROM to true , UNTIL to false))
    }
    var date by remember {
        mutableStateOf(mutableMapOf<String,Int>(YEAR to 0 , MONTH to 0 , DAY to 0))
    }
    var fromDate by remember {
        mutableStateOf(mutableMapOf<String,Int>(
            YEAR to currentDate.shYear ,
            MONTH to currentDate.shMonth ,
            DAY to currentDate.shDay
        ))
    }
    var untilDate by remember {
        mutableStateOf(mutableMapOf<String,Int>(
            YEAR to currentDate.shYear ,
            MONTH to currentDate.shMonth ,
            DAY to currentDate.shDay
        ))
    }
    val fromDateString = "${fromDate[YEAR]}/${fromDate[MONTH]}/${fromDate[DAY]}"
    val untilDateString = "${untilDate[YEAR]}/${untilDate[MONTH]}/${untilDate[DAY]}"

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        RightToLeftLayout {
            Text(
                modifier = Modifier.padding(vertical = 15.dp),
                text = "تاریخ مورد نظر را انتخاب کنید:",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.primaryVariant,
                fontFamily = persian_font_semi_bold,
                fontSize = 18.sp
            )
        }
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(200.dp)
                .border(
                    width = 1.5.dp,
                    shape = RoundedCornerShape(size = 30.dp),
                    color = MaterialTheme.colors.primaryVariant
                ),
            contentAlignment = Alignment.Center
        ){
            PersianDataPicker(
                spacerColor = MaterialTheme.colors.primaryVariant,
                textColor = MaterialTheme.colors.primaryVariant.toArgb(),
                textSize = (126.sp).value,
                controller = controller,
                modifier = Modifier.padding(8.dp),
            ){ year, month, day ->
                date = mutableMapOf( YEAR to year , MONTH to month , DAY to day)
                if(btnDate[UNTIL]!!){
                    untilDate = date
                }else if(btnDate[FROM]!!){
                    fromDate = date
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .background(
                            color = if (btnDate[UNTIL]!!) Color.Yellow else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.5.dp,
                            shape = CircleShape,
                            color = if (btnDate[UNTIL]!!) Color.Yellow else MaterialTheme.colors.primaryVariant
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable {
                            btnDate = mutableMapOf(FROM to false, UNTIL to true)
                        },
                    text = "تا: $untilDateString",
                    textAlign = TextAlign.Start,
                    color = if(btnDate[UNTIL]!!) Color.Black else MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_semi_bold,
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier
                        .background(
                            color = if (btnDate[FROM]!!) Color.Yellow else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.5.dp,
                            shape = CircleShape,
                            color = if (btnDate[FROM]!!) Color.Yellow else MaterialTheme.colors.primaryVariant
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clickable {
                            btnDate = mutableMapOf(FROM to true, UNTIL to false)
                        },
                    text = "از: $fromDateString",
                    textAlign = TextAlign.Start,
                    color = if(btnDate[FROM]!!) Color.Black else MaterialTheme.colors.primaryVariant ,
                    fontFamily = persian_font_semi_bold,
                    fontSize = 16.sp
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
                    val fromPd = PersianDate()
                    fromPd.shYear = fromDate[YEAR]!!
                    fromPd.shMonth = fromDate[MONTH]!!
                    fromPd.shDay = fromDate[DAY]!!
                    val untilPd = PersianDate()
                    untilPd.shYear = untilDate[YEAR]!!
                    untilPd.shMonth = untilDate[MONTH]!!
                    untilPd.shDay = untilDate[DAY]!!

                    inventorySaleViewModel.onEvent(
                        InventorySaleEvent.FilterSaleHistory(
                        mapOf(FROM to fromPd, UNTIL to untilPd)
                    ))
                    onCancel()
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF008506))
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "فیلتر کن",
                    color = Color.White,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun SaleItem(
    modifier: Modifier = Modifier,
    item: History,
    verticalPadding: Dp = 5.dp,
    contentPadding: Dp = 5.dp,
    onDelete:(History) -> Unit,
    onEdit:(History)  ->  Unit
){
    Log.d("recompose","@@@")
    RightToLeftLayout {
        Column(
            modifier = modifier
                .padding(vertical = verticalPadding)
                .shadow(5.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = if (isSystemInDarkTheme()) {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF009C07),
                                Color(0xFF75FF7B)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFD7CA),
                                Color(0xFF00E1FD)
                            )
                        )
                    }
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                        text ="حاشیه سود کالا: ${item.sellPrice.toLong() - item.buyPrice.toLong()}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="سود حاصل از فروش کالا: ${(item.sellPrice.toLong() - item.buyPrice.toLong()) * item.saleNumber.toLong()}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Row(modifier = Modifier.align(Alignment.Top)) {
                    Icon(
                        modifier = Modifier
                            .size(size = 40.dp)
                            .padding(all = 8.dp)
                            .clickable {
                                onEdit(item)
                            },
                        imageVector = Icons.Default.Edit ,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                    Icon(
                        modifier = Modifier
                            .size(size = 40.dp)
                            .padding(all = 8.dp)
                            .clickable {
                                onDelete(item)
                            },
                        imageVector = Icons.Default.Cancel ,
                        contentDescription = "Cancel",
                        tint = Color.Black
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
                    text ="تراکنش : فروش کالا",
                    fontFamily = persian_font_semi_bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    text ="تاریخ تراکنش: ${item.date}",
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
    val history = History(
        id = 0,
        remainingInventory = 5,
        transaction = TransactionState.Sale.state,
        createdTimeStamp = 3516516,
        title = ";alksdf",
        timeStamp = 6458161,
        date = ";laskdf",
        sellPrice = "54516",
        buyPrice = "5454656",
        saleNumber = "541"
    )

    SaleItem(modifier = Modifier.fillMaxWidth(),item = history, onDelete = {}){

    }
}