package com.example.storeaccounting.presentation.add_edit_credit_card

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.presentation.component.DescriptionEditText
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.util.Constants
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.general.GeneralEvent
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import com.example.storeaccounting.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

@Composable
fun AddEditCreditCardTopBar(
    parentNavController: NavController,
    editCardId: Int?,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel()
) {
    val list = viewModel.state.value.filteredCreditCard
    val editCard: CreditCard? = if(editCardId != -1 && list.isNotEmpty() ){
        list.first { it.id == editCardId }
    }else{
        null
    }
    var bankName by remember{
        mutableStateOf( "")
    }
    var irShaba by remember{
        mutableStateOf("")
    }
    var creditCardNumber by remember{
        mutableStateOf("")
    }
    var userName by remember{
        mutableStateOf("")
    }
    var expireDate by remember{
        mutableStateOf("")
    }
    var cvv2 by remember{
        mutableStateOf("")
    }
    var description by remember{
        mutableStateOf("")
    }
    if(editCard?.bankName != null){
        bankName = editCard.bankName
    }
    if(editCard?.irShaba != null){
        irShaba = editCard.irShaba
    }
    if(editCard?.cardNumber != null){
        creditCardNumber = editCard.cardNumber
    }
    if(editCard?.userName != null){
        userName = editCard.userName
    }
    if(editCard?.expireDate != null){
        expireDate = editCard.expireDate
    }
    if(editCard?.cvv2 != null){
        cvv2 = editCard.cvv2
    }
    if(editCard?.description != null){
        description = editCard.description
    }
    Log.d("listbank",bankName)
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event  ->
            when(event){
                is GeneralViewModel.GeneralUiEvent.CreateCreditCard   ->  {
                    Toast.makeText(context,"کارت با موفقیت ذخیره شد.",Toast.LENGTH_SHORT).show()
                    parentNavController.popBackStack(
                        route = NavigationRoute.Main.route,
                        inclusive = false
                    )
                }
                is GeneralViewModel.GeneralUiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message,Toast.LENGTH_SHORT).show()
                }
                is GeneralViewModel.GeneralUiEvent.UpdateCreditCard   ->  {
                    Toast.makeText(context,"کارت با موفقیت به روز رسانی شد.",Toast.LENGTH_SHORT).show()
                    parentNavController.popBackStack(
                        route = NavigationRoute.Main.route,
                        inclusive = false
                    )
                }
                else  ->  {

                }
            }
        }
    }
    Scaffold(
        topBar = {
            AddEditCreditCardTopBar(
                onBackPressed = {
                     parentNavController.popBackStack(route = NavigationRoute.Main.route, inclusive = false)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val creditCard = CreditCard(
                        bankName = bankName,
                        cardNumber = creditCardNumber,
                        cvv2 = cvv2,
                        description = description,
                        expireDate = expireDate,
                        irShaba = irShaba,
                        userName = userName
                    )
                    if(editCard == null){
                        viewModel.onEvent(GeneralEvent.CreateCreditCard(creditCard))
                    }else{
                        viewModel.onEvent(GeneralEvent.UpdateCreditCard(creditCard.copy(id = editCard.id)))
                    }

                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Default.Save,
                    contentDescription = "create",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            CreditCard(
                modifier = Modifier
                    .height(
                        height = 200.dp
                    )
                    .width(330.dp),
                lightColor = creditCardLight ,
                mediumColor = creditCardMedium ,
                darkColor = creditCardDark,
                bankName = bankName,
                irShaba = irShaba,
                creditCardNumber = creditCardNumber,
                userName = userName,
                expireDate = expireDate,
                cvv2 = cvv2
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp
                    ),
                color = MaterialTheme.colors.secondaryVariant
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                EditText(
                    hint = "نام بانک را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  bankName
                ){
                    bankName = it
                }
                EditText(
                    hint = "شماره شبا را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  irShaba
                ){
                    irShaba = it
                }
                EditText(
                    hint = "شماره حساب را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  creditCardNumber
                ){
                    creditCardNumber = it
                }
                EditText(
                    hint = "نام صاحب حساب را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  userName
                ){
                    userName = it
                }
                EditText(
                    hint = "تاریخ انقضا کارت را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  expireDate
                ){
                    expireDate = it
                }
                EditText(
                    hint = "cvv2 را وارد کنید ...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  cvv2
                ){
                    cvv2 = it
                }
                DescriptionEditText(
                    hint = "توضیحات تکمیلی را وارد کنید...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 8.dp),
                    _text =  description
                ){
                    description = it
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        height = 100.dp
                    )
                )
            }
        }
    }
}

@Composable
fun CreditCard(
    modifier: Modifier = Modifier,
    lightColor: Color,
    mediumColor: Color,
    darkColor: Color,
    bankName: String = "",
    irShaba: String = "",
    creditCardNumber: String = "",
    userName: String = "",
    expireDate: String = "",
    cvv2: String = "",
    smallFontSize: TextUnit = 16.sp,
    bigFontSize: TextUnit = 20.sp
){
    BoxWithConstraints(
        modifier = modifier
            .padding(7.5.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(15.dp)
            )
            .background(darkColor)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        //Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, height * 0.4f)

        val mediumColoredPath = Path().apply {
            // start of path
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            this.standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            this.standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            this.standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            this.standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            this.lineTo(width.toFloat() + 100f, height.toFloat() + 100f )
            this.lineTo(-100f, height.toFloat() + 100f )
            this.close()
        }
        //Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, height * 0.8f)

        val lightColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            this.standardQuadFromTo(lightPoint1, lightPoint2)
            this.standardQuadFromTo(lightPoint2, lightPoint3)
            this.standardQuadFromTo(lightPoint3, lightPoint4)
            this.standardQuadFromTo(lightPoint4, lightPoint5)
            this.lineTo(width.toFloat() + 100f, height.toFloat() + 100f )
            this.lineTo(-100f, height.toFloat() + 100f )
            this.close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ){
            drawPath(
                path = mediumColoredPath,
                color = mediumColor
            )
            drawPath(
                path = lightColoredPath,
                color = lightColor
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
        ) {
            Text(
                text = if (bankName.isEmpty()) "نام بانک" else bankName,
                fontFamily = persian_font_semi_bold,
                fontSize = smallFontSize,
                modifier = Modifier
                    .align(Alignment.TopEnd),
                color = Color.Black
            )
            Text(
                text = if (irShaba.isEmpty()) "IR00  0000  0000  0000  0000  0000  00" else irShaba,
                fontFamily = persian_font_semi_bold,
                fontSize = smallFontSize,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-28).dp),
                color = Color.Black
            )
            Text(
                text = if (creditCardNumber.isEmpty()) "XXXX-XXXX-XXXX-XXXX" else creditCardNumber,
                fontFamily = persian_font_semi_bold,
                fontSize = bigFontSize,
                modifier = Modifier
                    .align(Alignment.Center),
                color = Color.Black
            )
            Text(
                text = if (userName.isEmpty()) "نام و نام خانوادگی" else userName,
                fontFamily = persian_font_semi_bold,
                fontSize = smallFontSize,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 25.dp),
                color = Color.Black
            )
            Text(
                text = if (expireDate.isEmpty()) "تاریخ انقضا" else "تاریخ انقضا: $expireDate",
                fontFamily = persian_font_semi_bold,
                fontSize = smallFontSize,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                color = Color.Black
            )
            Text(
                text =if (cvv2.isEmpty()) "cvv2" else "cvv2: $cvv2",
                fontFamily = persian_font_semi_bold,
                fontSize = smallFontSize,
                modifier = Modifier
                    .align(Alignment.BottomStart),
                color = Color.Black
            )
        }
    }
}
fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    this.quadraticBezierTo(
        from.x,
        from.y,
        abs(from.x + to.x) / 2f,
        abs(from.y + to.y) / 2f
    )
}
@Composable
fun AddEditCreditCardTopBar(
    onBackPressed: ()-> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "ایجاد کارت اعتباری",
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
    CreditCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                height = 200.dp
            )
            .padding(
                horizontal = 20.dp
            ),
        lightColor = creditCardLight ,
        mediumColor = creditCardMedium ,
        darkColor = creditCardDark
    )
}
