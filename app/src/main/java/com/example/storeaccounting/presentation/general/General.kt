package com.example.storeaccounting.presentation.General

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.presentation.add_edit_credit_card.CreditCard
import com.example.storeaccounting.presentation.component.CustomContentDialog
import com.example.storeaccounting.presentation.component.CustomAcceptRefuseDialog
import com.example.storeaccounting.presentation.component.EditText
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.general.GeneralEvent
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import com.example.storeaccounting.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun General(
    parentNavController: NavController,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel()
) {
    val creditCardList = remember {
        mutableStateOf(viewModel.state.value.filteredCreditCard)
    }

    creditCardList.value = viewModel.state.value.filteredCreditCard
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event  ->
            when(event){
                is GeneralViewModel.GeneralUiEvent.CreateCreditCard   ->  {
                    Toast.makeText(context,"کارت با موفقیت حذف شد.", Toast.LENGTH_SHORT).show()
                }
                is GeneralViewModel.GeneralUiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
                is GeneralViewModel.GeneralUiEvent.FilteredCreditCard -> {
                    creditCardList.value = viewModel.state.value.filteredCreditCard
                }
                else  ->  {

                }
            }
        }
    }
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        AddForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            parentNavController = parentNavController,
            title = "اضافه کردن کارت اعتباری",
            route = "${NavigationRoute.AddEditCreditCard.route}/${-1}"
        )
        EditText(
            hint =  "نام صاحب کارت را جهت جست و جو وارد کنید...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            _text = "",
        ){
            text = it
            viewModel.onEvent(GeneralEvent.FilterCreditCard(it))
        }
        CreditCardList(
            modifier = Modifier
                .height(height = 250.dp)
                .fillMaxWidth(),
            list =creditCardList.value,
            parentNavController = parentNavController
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp, vertical = 5.dp
                ),
            color = MaterialTheme.colors.secondaryVariant
        )
        AddForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            parentNavController = parentNavController,
            title = "ایجاد فاکتور فروش",
            route = "${NavigationRoute.AddEditFactor.route}"
        )
    }
}

@Composable
fun AddForm(
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    title: String,
    route: String
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ){
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
                parentNavController.navigate(
                    route = route
                )
            },
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                textAlign = TextAlign.Center,
                text = title ,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreditCardList(
    modifier: Modifier = Modifier,
    list: List<CreditCard>,
    parentNavController: NavController,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel(),
) {
    val showDeleteDialog =  remember {
        mutableStateOf(false)
    }
    val showDescriptionDialog =  remember {
        mutableStateOf(false)
    }
    var creditCard by remember {
        mutableStateOf<CreditCard?>(null)
    }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        contentPadding = PaddingValues(8.dp),
    ){
        items(
            count = list.size,
            key = { it }
        ){
            CreditCardItem(
                modifier = Modifier
                    .width(width = 310.dp)
                    .fillMaxHeight()
                    .animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ),
                item = list[it],
                onEdit = {
                    parentNavController.navigate(
                        route = "${NavigationRoute.AddEditCreditCard.route}/${it.id}"
                    )
                },
                onDelete = { CreditCard ->
                    creditCard = CreditCard
                    showDeleteDialog.value = true
                },
                onDescription = { CreditCard ->
                    creditCard = CreditCard
                    if(CreditCard.description.isNotEmpty()){
                        showDescriptionDialog.value = true
                    }else{
                        Toast.makeText(context,"این کارت فاقد توضیحات میباشد.",Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }
    }
    if(showDescriptionDialog.value){
        CustomContentDialog(
            modifier = Modifier
                .width(350.dp)
                .height(500.dp),
            title = creditCard!!.userName ,
            content = creditCard!!.description ,
            setShowDialog = {
                showDescriptionDialog.value = it
            }
        ){
            showDescriptionDialog.value = false
        }
    }
    if (showDeleteDialog.value) {
        CustomAcceptRefuseDialog(
            modifier = Modifier
                .width(350.dp)
                .height(200.dp),
            setShowDialog = {
                showDeleteDialog.value = it
            },
            title = "حذف کردن کارت اعتباری",
            content = "حذف کارت اعتباری غیر قابل بازگشت می باشد، آیا مطمئن هستید که می خواهید کارت اعتباری را حذف کنید؟",
            positiveButtonTitle = "حذف کن",
            negativeButtonTitle = "خارج شدن",
            positiveButtonColor = Color.Green,
            negativeButtonColor = Color.Red,
            onSuccess = {
                viewModel.onEvent(GeneralEvent.DeleteCreditCard(creditCard!!))
                showDeleteDialog.value = false
            },
            onCancel = {
                showDeleteDialog.value = false
            }
        )
    }
}

@Composable
fun CreditCardItem(
    modifier: Modifier = Modifier,
    item: CreditCard,
    onEdit: (CreditCard) -> Unit,
    onDelete: (CreditCard) -> Unit,
    onDescription: (CreditCard) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CreditCard(
            modifier = Modifier
                .height(
                    height = 175.dp
                )
                .fillMaxWidth(),
            smallFontSize = 12.sp,
            bigFontSize = 16.sp,
            lightColor = creditCardLight ,
            mediumColor = creditCardMedium ,
            darkColor = creditCardDark,
            bankName = item.bankName,
            irShaba = item.irShaba,
            creditCardNumber = item.cardNumber,
            userName = item.userName,
            expireDate = item.expireDate,
            cvv2 = item.cvv2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
        ){
            Spacer(modifier = Modifier.width(width = 10.dp))
            Button(
                modifier = Modifier
                    .width(width = 55.dp)
                    .weight(1f),
                onClick = {
                    onEdit(item)
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BB08))
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                    ,
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(width = 5.dp))
            Button(
                modifier = Modifier
                    .width(width = 55.dp)
                    .weight(1f),
                onClick = {
                    onDelete(item)
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF0000))
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                    ,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(width = 5.dp))
            Button(
                modifier = Modifier
                    .width(width = 55.dp)
                    .weight(1f),
                onClick = {
                    onDescription(item)
                },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00B0FF))
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                    ,
                    imageVector = Icons.Default.Description,
                    contentDescription = "description",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(width = 10.dp))
        }
    }
}