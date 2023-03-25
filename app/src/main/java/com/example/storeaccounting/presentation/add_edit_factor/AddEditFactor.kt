package com.example.storeaccounting.presentation.add_edit_factor

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.util.CaptureFactorImageFromComposeView
import com.example.storeaccounting.domain.util.Utility
import com.example.storeaccounting.presentation.component.FactorEditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.general.GeneralEvent
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import com.example.storeaccounting.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDateFormat


@Composable
fun AddEditFactor(
    parentNavController: NavController,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel()
) {

    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event  ->
            when(event){
                is GeneralViewModel.GeneralUiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
                is GeneralViewModel.GeneralUiEvent.ShareFactor -> {
                    (context as Activity).shareFactor(event.path)
                }
                else  ->  {}
            }
        }
    }
    var rowNumber by remember {
        mutableStateOf(0)
    }
    val list = remember{
        mutableStateOf<SnapshotStateList<Int>>(mutableStateListOf())
    }
    var date = remember{
        mutableStateOf("")
    }
    var showSign = remember{
        mutableStateOf(false)
    }
    var key: String = remember{
        ""
    }
    var permissionAlertDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var jetCaptureView: MutableState<CaptureFactorImageFromComposeView>? = null
    val readAndWriteStorageResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted) {
                if (permissionName == WRITE_EXTERNAL_STORAGE
                    && key == WRITE_EXTERNAL_STORAGE){

                    scope.launch {
                        viewModel.onEvent(
                            GeneralEvent.SaveFactorInPdf(
                                jetCaptureView?.value?.capture(
                                    jetCaptureView?.value as
                                            CaptureFactorImageFromComposeView
                                )
                            )
                        )
                    }

                } else if(permissionName == READ_EXTERNAL_STORAGE
                    && key == READ_EXTERNAL_STORAGE){
                        // do something
                }
            }else{
                if (permissionName == WRITE_EXTERNAL_STORAGE
                    && key == WRITE_EXTERNAL_STORAGE){
                    permissionAlertDialog = true

                }else if(permissionName == READ_EXTERNAL_STORAGE
                    && key == READ_EXTERNAL_STORAGE){
                    permissionAlertDialog = true
                }
            }
        }
    }
    if (permissionAlertDialog){
        PermissionDialog(
            onDismiss = {
                  permissionAlertDialog = false
            },
            onGoToAppSettingsClick = {
                (context as Activity).openAppSettings()
            },
            modifier = Modifier
                .width(width = 300.dp)
                .height(height = 200.dp),
            shape = RoundedCornerShape(size = 20.dp),
            backgroundColor = MaterialTheme.colors.background
        )
    }
    Scaffold(
        topBar = {
            AddEditFactorTopBar(
                onBackPressed = {
                    parentNavController.popBackStack(route = NavigationRoute.Main.route, inclusive = false)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    key = WRITE_EXTERNAL_STORAGE
                    readAndWriteStorageResultLauncher.launch(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    )
                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Default.Share,
                    contentDescription = "create",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(all = 5.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            jetCaptureView = remember { mutableStateOf(
                CaptureFactorImageFromComposeView(
                    list = list.value,
                    date = date,
                    showSign = showSign,
                    modifier = mutableStateOf(
                        Modifier
                        .fillMaxWidth()
                        .background(
                            color = custom_blue_7,
                            shape = RoundedCornerShape(
                                size = 25.dp
                            )
                        )
                        .border(
                            width = 2.dp,
                            color = custom_blue_3,
                            shape = RoundedCornerShape(
                                size = 25.dp
                            )
                        )
                        .padding(all = 8.dp)
                    ) ,
                    context = context
                )
            )
            }
            AndroidView(modifier = Modifier.wrapContentSize(),
                factory = {
                    CaptureFactorImageFromComposeView(
                        list = list.value,
                        date = date,
                        showSign = showSign,
                        modifier = mutableStateOf(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    color = custom_blue_7,
                                    shape = RoundedCornerShape(
                                        size = 25.dp
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    color = custom_blue_3,
                                    shape = RoundedCornerShape(
                                        size = 25.dp
                                    )
                                )
                                .padding(all = 8.dp)
                        ) ,
                        context = context
                    ).apply {
                        post {
                            jetCaptureView?.value = this
                        }
                    }
                }
            )
            AddDeleteRow(
                onAdd = {
                    rowNumber++

                    list.value.add(rowNumber)
                    //list.value += SaleFactor(row = rowNumber.toString())

                },
                onDelete = {
                    if (rowNumber !=0){
                        rowNumber--
                        list.value.removeAt(list.value.size-1)
                    }
                },
                onDate = {
                    if(it){
                        date.value = PersianDateFormat("Y/m/d")
                            .format(viewModel.getPersianDate()).toString()
                    }else{
                        date.value = ""
                    }
                },
                showSign = {
                    showSign.value = it
                }
            )
        }
    }
}
@Composable
fun SaleFactorUi(
    modifier: Modifier = Modifier,
    list: List<Int>,
    date: String,
    showSign: Boolean
) {
    val totalPriceIndex by remember {
        mutableStateOf<MutableMap<Int,Double>>(mutableMapOf())
    }
    var totalPrice by remember {
        mutableStateOf(0.0)
    }
    var totalPriceInString by remember {
        mutableStateOf("")
    }
    RightToLeftLayout {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp),
                textAlign = TextAlign.Start,
                text = "فاکتور فروش",
                color = custom_blue_2,
                fontFamily = persian_font_semi_bold,
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .height(height = 39.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        modifier = Modifier
                            .size(size = 30.dp)
                            .padding(end = 5.dp),
                        imageVector = Icons.Default.Phone ,
                        contentDescription = "phone",
                        tint = custom_blue_2
                    )
                    FactorEditText(
                        _text = "",
                        modifier = Modifier
                            .fillMaxWidth(),
                        editTextModifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = custom_blue_3,
                                shape = RoundedCornerShape(size = 8.dp)
                            ),
                        keyboardOptions = KeyboardOptions.Default
                    ){

                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        textAlign = TextAlign.Start,
                        text = "تاریخ  ",
                        color = custom_blue_2,
                        fontFamily = persian_font_regular,
                        fontSize = 16.sp
                    )
                    FactorEditText(
                        _text = date,
                        modifier = Modifier
                            .fillMaxWidth(),
                        editTextModifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = custom_blue_3,
                                shape = RoundedCornerShape(size = 8.dp)
                            ),
                        keyboardOptions = KeyboardOptions.Default
                    ){

                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .height(height = 39.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    textAlign = TextAlign.Start,
                    text = "خریدار ",
                    color = custom_blue_2,
                    fontFamily = persian_font_regular,
                    fontSize = 16.sp
                )
                FactorEditText(
                    _text = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    editTextModifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = custom_blue_3,
                            shape = RoundedCornerShape(size = 8.dp)
                        ),
                    keyboardOptions = KeyboardOptions.Default
                ){

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .height(height = 39.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    textAlign = TextAlign.Start,
                    text = "آدرس ",
                    color = custom_blue_2,
                    fontFamily = persian_font_regular,
                    fontSize = 16.sp
                )
                FactorEditText(
                    _text = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    editTextModifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = custom_blue_3,
                            shape = RoundedCornerShape(size = 8.dp)
                        ),
                    keyboardOptions = KeyboardOptions.Default
                ){

                }
            }
            TableTopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 50.dp)
            )
            list.forEach {
                TableRowSection( modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 50.dp),
                    item = it
                ){ Pair ->
                    totalPriceIndex[Pair.first] = Pair.second
                    totalPrice = 0.0
                    totalPriceIndex.toList().forEach {
                        totalPrice += it.second
                    }
                    totalPriceInString = Utility.changeNumberToString(totalPrice.toBigDecimal().toLong())
                }
            }
            TableBottomSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 50.dp),
                offset = list.size,
                totalPrice = totalPrice,
                totalPriceInString = totalPriceInString
            )
            Spacer(modifier = Modifier.height(height = 10.dp))
            if(showSign){
                SignSection()
            }
        }
    }
}

@Composable
fun TableTopSection(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                ),
            )
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                ),
                color = custom_blue_2
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.Center,
            text = "شماره",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "شرح کالا",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.Center,
            text = "تعداد",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(modifier = Modifier
            .weight(1f),
            textAlign = TextAlign.Center,
            text = "قیمت واحد",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(modifier = Modifier

            .weight(1f),
            textAlign = TextAlign.Center,
            text = "قیمت کل",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
    }
}
@Composable
fun TableRowSection(
    modifier: Modifier = Modifier,
    item: Int,
    context: Context = LocalContext.current,
    onTotalPrice:(Pair<Int,Double>)  -> Unit
){
    var basePrice by remember {
        mutableStateOf(0.0)
    }
    var number by remember {
        mutableStateOf(0.0)
    }
    var totalPrice by remember {
        mutableStateOf(0.0)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .offset(y = (-2 * item).dp)
            .background(
                color = if (exception) Color.Red else Color.White,
            )
            .border(
                width = 2.dp,
                color = custom_blue_2
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Text(
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.Center,
            text = item.toString(),
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        FactorEditText(
            hint = "...",
            _text = "",
            modifier = Modifier
                .height(height = 38.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default
        ){

        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        FactorEditText(
            hint = ".",
            _text = "",
            modifier = Modifier
                .height(height = 38.dp)
                .weight(0.5f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        ){
            try{
                number = it.toDouble()
                totalPrice = basePrice * number
                onTotalPrice(Pair(item,totalPrice))
                exception = false
            }catch (e: java.lang.NumberFormatException){
                Toast.makeText(context,"عدد وارد  شده دارای فرمت صحیح نمی باشد.",Toast.LENGTH_SHORT).show()
                exception = true
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        FactorEditText(
            hint = "...",
            _text = "",
            modifier = Modifier
                .height(height = 38.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        ){
            try{
                basePrice = it.toDouble()
                totalPrice = basePrice * number
                onTotalPrice(Pair(item,totalPrice))
                exception = false
            }catch (e: java.lang.NumberFormatException){
                Toast.makeText(context,"عدد وارد  شده دارای فرمت صحیح نمی باشد.",Toast.LENGTH_SHORT).show()
                exception = true
            }

        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = totalPrice.toBigDecimal().toPlainString(),
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TableBottomSection(
    modifier: Modifier = Modifier,
    offset: Int ,
    totalPrice: Double,
    totalPriceInString: String
){
    Row(
        modifier = modifier
            .offset(y = ((-2 * offset) - 2).dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .border(
                width = 2.dp,
                color = custom_blue_2,
                shape = RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            modifier = Modifier
                .weight(0.5f),
            textAlign = TextAlign.Center,
            text = " مبلغ به حروف:",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        FactorEditText(
            _text = totalPriceInString,
            modifier = Modifier
                .height(height = 38.dp)
                .weight(1.65f),
            editTextModifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .border(
                    width = 2.dp,
                    color = custom_blue_3,
                    shape = RoundedCornerShape(size = 8.dp)
                ),
                keyboardOptions = KeyboardOptions.Default
        ){

        }
        Text(
            modifier = Modifier
                .weight(0.2f),
            textAlign = TextAlign.Center,
            text = " تومان ",
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(width = 5.dp))
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = custom_blue_2,
        )
        Text(
            modifier = Modifier
                .weight(0.78f)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = totalPrice.toBigDecimal().toPlainString(),
            color = Color.Black,
            fontFamily = persian_font_regular,
            fontSize = 14.sp
        )
    }
}

@Composable
fun AddDeleteRow(
    context: Context = LocalContext.current,
    onAdd:() -> Unit,
    onDelete:() -> Unit,
    onDate:(Boolean) -> Unit,
    showSign:(Boolean)  ->  Unit
){
    var rowNumber by remember {
        mutableStateOf(0)
    }
    var dateCheck by remember{
        mutableStateOf(false)
    }
    var signCheck by remember{
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(
                modifier = Modifier
                    .clickable {
                    dateCheck = !dateCheck
                    onDate(dateCheck)
                },
                textAlign = TextAlign.Center,
                text ="به روز کردن تاریخ فاکتور",
                color = MaterialTheme.colors.primaryVariant,
                fontFamily = persian_font_semi_bold,
                fontSize = 16.sp
            )
            Checkbox(
                checked = dateCheck,
                onCheckedChange = {
                    dateCheck = !dateCheck
                    onDate(dateCheck)
                },
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colors.primary)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Text(modifier = Modifier
                .clickable {
                    signCheck = !signCheck
                    showSign(signCheck)
                },
                textAlign = TextAlign.Center,
                text ="اضافه کردن امضا به فاکتور",
                color = MaterialTheme.colors.primaryVariant,
                fontFamily = persian_font_semi_bold,
                fontSize = 16.sp
            )
            Checkbox(

                checked = signCheck,
                onCheckedChange = {
                    signCheck = !signCheck
                    showSign(signCheck)
                },
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colors.primary)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.width(width = 18.dp))
            Button(
                modifier = Modifier
                    .width(width = 55.dp)
                    .weight(1f),
                onClick = {
                    onDelete()
                    rowNumber--
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF0000))
            ){
                Text(
                    modifier = Modifier
                        .weight(0.75f),
                    textAlign = TextAlign.Center,
                    text = "حذف کردن ردیف",
                    color = Color.Black,
                    fontFamily = persian_font_semi_bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(width = 18.dp))
            Button(
                modifier = Modifier
                    .width(width = 55.dp)
                    .weight(1f),
                onClick = {
                    if (rowNumber >= 10){
                        Toast.makeText(context,
                            "ظرفیت هر فاکتور تنها 10 ردیف کالا می باشد.",Toast.LENGTH_LONG).show()
                    } else{
                        onAdd()
                        rowNumber++
                    }
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BB08))
            ){
                Text(
                    modifier = Modifier
                        .weight(0.75f),
                    textAlign = TextAlign.Center,
                    text = "اضافه کردن ردیف",
                    color = Color.Black,
                    fontFamily = persian_font_semi_bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(width = 18.dp))
        }
    }
}

@Composable
fun SignSection(
    context: Context = LocalContext.current,
) {
    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null){
            result.value = it
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    size = 10.dp
                )
            )
            .border(
                color = custom_blue_2,
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                width = 2.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 20.dp),
            textAlign = TextAlign.Center,
            text = "امضا:",
            color = Color.Black,
            fontFamily = persian_font_semi_bold,
            fontSize = 16.sp
        )
        Box(
            modifier = Modifier
                .padding(start = 100.dp)
                .width(width = 90.dp)
                .height(height = 90.dp)
                .border(
                    width = 1.5.dp,
                    color = if(result.value == null) Color.Black else Color.Transparent,
                    shape = RoundedCornerShape(
                        size = 15.dp
                    )
                )
                .align(Alignment.CenterStart)
                .clickable {
                    launcher.launch("*/*")
                },
            contentAlignment = Alignment.Center
        ){
            if(result.value == null){
                Image(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.AdsClick ,
                    contentDescription = "add Sign picture"
                )
            }else{
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(result.value)
                        .crossfade(true)
                        .allowHardware(false)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_pick_sign),
                    error = painterResource(R.drawable.ic_error),
                    onError = {
                              Toast.makeText(context,"خطا در بارگزاری تصویر امضا، لطفا دوباره تلاش کنید.",
                                  Toast.LENGTH_SHORT).show()
                    },
                    contentDescription = "add Sign picture",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size(70.dp)
                )
            }
        }
    }
}
@Composable
fun AddEditFactorTopBar(
    onBackPressed: ()-> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "ساخت فاکتور",
                fontWeight = FontWeight.SemiBold)
        },
        backgroundColor = MaterialTheme.colors.onSurface,
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed()
                }
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}
@Composable
fun PermissionDialog(
    onDismiss: ()  -> Unit,
    onGoToAppSettingsClick: ()  -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape,
    backgroundColor: Color
){
    RightToLeftLayout {
        AlertDialog(
            onDismissRequest = onDismiss,
            shape = shape,
            backgroundColor = backgroundColor,
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Divider(color = MaterialTheme.colors.secondaryVariant)
                    Text(
                        text = "تغییر دسترسی به حافظه",
                        textAlign = TextAlign.Center,
                        fontFamily = persian_font_semi_bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onGoToAppSettingsClick()
                            }
                            .padding(5.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "نیاز به دسترسی به حافظه تلفن همراه",
                    textAlign = TextAlign.Center,
                    fontFamily = persian_font_semi_bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onGoToAppSettingsClick()
                        }
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                )
            },
            text = {
                Text(
                    text = "این اپلیکیشن برای ذخیره و انتشار فاکتور نیاز به دسترسی به حافظه داخلی تلفن همراه دارد.",
                    textAlign = TextAlign.Center,
                    fontFamily = persian_font_regular,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onGoToAppSettingsClick()
                        }
                        .padding(horizontal = 5.dp)
                )
            },
            modifier = modifier
        )
    }
}
fun Activity.shareFactor(result: String){
    MediaScannerConnection.scanFile(this, arrayOf(result),null){
            path,uri ->
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri)
        startActivity(Intent.createChooser(shareIntent,"share"))
    }
}
fun Activity.openAppSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package",packageName, null)
    ).also(::startActivity)
}
//@Preview(showBackground = true)
@Composable
fun Preview() {
    SaleFactorUi(
        modifier = Modifier
            .fillMaxSize(),
        list = emptyList(),
        date = "",
        showSign = true
    )
}