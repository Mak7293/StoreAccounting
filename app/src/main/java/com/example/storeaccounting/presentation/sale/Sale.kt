package com.example.storeaccounting.presentation.sale

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.presentation.component.CustomDeleteDialog
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.component.date_picker.PersianDataPicker
import com.example.storeaccounting.presentation.util.Constants.DAY
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.Event
import com.example.storeaccounting.presentation.view_model.ViewModel
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
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Sale(
    viewModel: ViewModel,
    onEdit: (History,FabRoute) -> Unit,
    onClick:(FabRoute)  -> Unit,
){
    Log.d("SaleRecomposition","@@@@@@@@")

    val saleHistoryList = remember() {
        mutableStateOf(viewModel.state.value.filteredHistory.filter {
            it.transaction == TransactionState.Sale.state
        })
    }
    LaunchedEffect(key1 = viewModel.state.value.filteredHistory[0].id){
        saleHistoryList.value = viewModel.state.value.filteredHistory
    }

    var graphState by remember {
        mutableStateOf(TEN_DAYS_GRAPH)
    }
    val graphList = remember(key1 =  viewModel.state.value.history){
        mutableStateOf<List<Number>>(
            viewModel.mapSaleProfitByDay(viewModel.graphHistoryList(
                from = System.currentTimeMillis()-(864000000),
                until = System.currentTimeMillis()
        )).values.toMutableList())
    }

    Log.d("SaleRecomposition", viewModel.state.value.filteredHistory.toString())
    Log.d("SaleRecomposition", viewModel.state.value.history.toString())
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event  ->
            when(event){
                is ViewModel.UiEvent.FilteredHistoryList   ->  {
                    Log.d("SaleRecomposition!!!-0", saleHistoryList.value.toString())
                    saleHistoryList.value = viewModel.state.value.filteredHistory
                    Log.d("SaleRecomposition!!!-1", saleHistoryList.value.toString())
                }
                is ViewModel.UiEvent.SaleInventory  ->  {
                    delay(500L)
                    if(graphState == TEN_DAYS_GRAPH){
                        graphList.value = viewModel.mapSaleProfitByDay(viewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(864000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }else if (graphState == THIRTY_DAYS_GRAPH){
                        graphList.value = viewModel.mapSaleProfitByDay(viewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(2592000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }
                }
                is ViewModel.UiEvent.DeleteSaleHistory  ->  {
                    delay(1000L)
                    if(graphState == TEN_DAYS_GRAPH){
                        graphList.value = viewModel.mapSaleProfitByDay(viewModel.graphHistoryList(
                            from = System.currentTimeMillis()-(864000000),
                            until = System.currentTimeMillis()
                        )).values.toMutableList()
                    }else if (graphState == THIRTY_DAYS_GRAPH){
                        graphList.value = viewModel.mapSaleProfitByDay(viewModel.graphHistoryList(
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
                    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(*a))
                    Chart(
                        chart =
                            lineChart(
                                lines = listOf(lineSpec(
                                    lineColor = Color.Red
                                ))
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
                            ),
                            valueFormatter = AxisValueFormatter { value, chartValues ->
                                val a = if (value == 0.0f) "today" else "-"+value.toInt().toString()
                                a
                            }
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
                                .height(35.dp),
                            onClick = {
                                if(viewModel.filterState.value){
                                    viewModel.onEvent(Event.FilterSaleHistory(null))
                                }else{
                                    onClick(FabRoute.ResultFab)
                                }
                            },
                            shape = CircleShape,
                            colors =  ButtonDefaults.buttonColors(
                                backgroundColor = if(viewModel.filterState.value) Color.Red else Color.White,
                            ),
                            border = BorderStroke(width = 2.dp, color = Color.Black)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text =if(viewModel.filterState.value) "نمایش تمام فروش ها" else "فیلتر کردن تاریخ فروش ها",
                                color = Color.Black,
                                fontFamily = persian_font_semi_bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (viewModel.filterState.value) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "از ${
                                    PersianDateFormat("Y/m/d").format(
                                        viewModel.filterRange?.get(
                                            FROM
                                        )
                                    )
                                }" +
                                        " تا ${
                                            PersianDateFormat("Y/m/d").format(
                                                viewModel.filterRange?.get(
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
                                        graphList.value = viewModel.mapSaleProfitByDay(
                                            viewModel.graphHistoryList(
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
                                        graphList.value = viewModel.mapSaleProfitByDay(
                                            viewModel.graphHistoryList(
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaleContent(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel(),
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
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
    ){
        items(
            count = saleHistoryList.size,
            key = { it }
        ){
            SaleItem(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                ),
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
        CustomDeleteDialog(
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
            onSuccess = {
                viewModel.onEvent(Event.DeleteSaleHistory(deleteHistory!!))
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
    viewModel: ViewModel = hiltViewModel(),
    saleList: List<InventoryEntity>,
    context: Context = LocalContext.current,
    oldHistory: History? = null,
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
        val inventoryEntity = remember {
            mutableStateOf<InventoryEntity?>(null)
        }
        LaunchedEffect(key1 = oldHistory){
            if (oldHistory!=null){
                inventoryEntity.value = viewModel.getHistoryByInventory(oldHistory.createdTimeStamp).inventory
                number = oldHistory.saleNumber
            }else{
                inventoryEntity.value = null
            }
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
                    SaleBottomSheetItem(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 1500
                            )
                        ),
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
                _text = oldHistory?.saleNumber ?: ""
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
                        if(inventoryEntity.value != null){
                            try{
                                val newInventoryEntity = inventoryEntity.value!!.copy(
                                    date = currentDate,
                                    timeStamp = System.currentTimeMillis(),
                                    number = (inventoryEntity.value!!.number.toInt() - number.toInt()).toString(),
                                )
                                val newHistory = History(
                                    id=oldHistory?.id ,
                                    createdTimeStamp = newInventoryEntity.createdTimeStamp,
                                    remainingInventory =
                                    if(newInventoryEntity.createdTimeStamp == oldHistory?.createdTimeStamp){
                                        newInventoryEntity.number.toInt() + oldHistory.saleNumber.toInt()
                                    }else{
                                        newInventoryEntity.number.toInt()
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
                                    viewModel.onEvent(Event.SaleInventory(
                                        inventoryEntity = newInventoryEntity,
                                        history = newHistory
                                    ))
                                }else{
                                    viewModel.onEvent(Event.UpdateSaleTransaction(
                                        inventoryEntity = newInventoryEntity,
                                        newHistory = newHistory,
                                        oldHistory = oldHistory))
                                }
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
                    contentDescription = "Add",
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
    viewModel: ViewModel = hiltViewModel(),
    controller: PersianDataPickerController,
    onCancel:() ->  Unit,
    onResult:(Map<String,Long>) ->  Unit,
){
    val currentDate = viewModel.getPersianDate()
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

                    viewModel.onEvent(Event.FilterSaleHistory(
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
                        text ="حاشیه سود کالا: ${item.sellPrice.toInt() - item.buyPrice.toInt()}",
                        fontFamily = persian_font_semi_bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text ="سود حاصل از فروش کالا: ${(item.sellPrice.toInt() - item.buyPrice.toInt()) * item.saleNumber.toInt()}",
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
                        contentDescription = "Add",
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
                        contentDescription = "Add",
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
    /*SaleBottomSheetItem(
        modifier = Modifier,
        item = inventoryEntity,
        contentPadding = 5.dp,
        verticalPadding = 5.dp,
        isSelected = true
    ){

    }*/
    SaleItem(modifier = Modifier.fillMaxWidth(),item = history, onDelete = {}){

    }
}