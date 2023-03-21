package com.example.storeaccounting.presentation.add_edit_factor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.util.Utility
import com.example.storeaccounting.presentation.component.FactorEditText
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import com.example.storeaccounting.ui.theme.*
import saman.zamani.persiandate.PersianDateFormat


@Composable
fun AddEditFactor(
    parentNavController: NavController,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel()
) {
    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    var rowNumber by remember {
        mutableStateOf(0)
    }
    val list = remember{
        mutableStateOf<SnapshotStateList<Int>>(mutableStateListOf())
    }
    var date by remember{
        mutableStateOf("")
    }
    var showSign by remember{
        mutableStateOf(false)
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
                    // do something
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
            SaleFactorUi(
                modifier = Modifier
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
                    .padding(all = 8.dp),
                list = list.value,
                date = date,
                showSign = showSign
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
                        date = PersianDateFormat("Y/m/d")
                            .format(viewModel.getPersianDate()).toString()
                    }else{
                        date = ""
                    }
                },
                showSign = {
                    showSign = it
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
    var totalPriceIndex by remember {
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
                .weight(0.8f),
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
                .weight(1.35f),
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
            Log.d("uri",it.toString())
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
                    color = Color.Black,
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
                        .build(),
                    placeholder = painterResource(R.drawable.ic_pick_sign),
                    error = painterResource(R.drawable.ic_error),
                    onError = {
                              Toast.makeText(context,"خطا در بارگزاری تصویر امضا، لطفا دوباره تلاش کنید.",
                                  Toast.LENGTH_SHORT).show()
                    },
                    contentDescription = "add Sign picture",
                    contentScale = ContentScale.Crop,
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