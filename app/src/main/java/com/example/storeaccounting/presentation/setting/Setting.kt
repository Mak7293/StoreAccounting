package com.example.storeaccounting.presentation.setting

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.R
import com.example.storeaccounting.presentation.add_edit_factor.PermissionDialog
import com.example.storeaccounting.presentation.add_edit_factor.openAppSettings
import com.example.storeaccounting.presentation.component.CustomAcceptRefuseDialog
import com.example.storeaccounting.presentation.component.CustomContentDialog
import com.example.storeaccounting.presentation.component.DefaultRadioButton
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.setting.SettingEvent
import com.example.storeaccounting.presentation.view_model.setting.SettingViewModel
import com.example.storeaccounting.ui.theme.persian_font_regular
import kotlinx.coroutines.flow.collectLatest


@Composable
fun Setting() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                all = 20.dp
            )
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ){
        content(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.secondary
                        )
                    ),
                    shape = RoundedCornerShape(
                        size = 20.dp
                    )
                )
                .border(
                    color = if (isSystemInDarkTheme()) Color.Yellow else Color(0xFFFF5722),
                    width = 1.5.dp,
                    shape = RoundedCornerShape(
                        size = 20.dp
                    )
                )
        )
    }
}

@Composable
fun content(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    var currentTheme by remember {
        mutableStateOf("")
    }
    var contactToDeveloperDialog by remember {
        mutableStateOf(false)
    }
    var inventoryDialog by remember {
        mutableStateOf(false)
    }
    var saleDialog by remember {
        mutableStateOf(false)
    }
    var signDialog by remember {
        mutableStateOf(false)
    }
    var backupDialog by remember {
        mutableStateOf(false)
    }
    var showPermissionDialog by remember {
        mutableStateOf(false)
    }
    var restoreDialog by remember {
        mutableStateOf(false)
    }
    var launcherKey = remember {
        ""
    }
    val activityLauncher= rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                Toast.makeText(context," گرفتن بک آپ نیاز به دسترسی برای مدیریت فایل ها دارد.",
                    Toast.LENGTH_SHORT).show()
            }else if(launcherKey == WRITE_EXTERNAL_STORAGE){
                backupDialog = true
            }else if(launcherKey == READ_EXTERNAL_STORAGE){
                restoreDialog = true
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted) {
                if (permissionName == WRITE_EXTERNAL_STORAGE && launcherKey == WRITE_EXTERNAL_STORAGE){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        if(Environment.isExternalStorageManager()){
                            backupDialog = true
                        }else{
                            val intent = Intent()
                            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                            val uri: Uri = android.net.Uri.fromParts("package", context.getPackageName(), null)
                            intent.data = uri
                            activityLauncher.launch(intent)
                        }
                    }else{
                        backupDialog = true
                    }
                }else if (permissionName == READ_EXTERNAL_STORAGE && launcherKey == READ_EXTERNAL_STORAGE){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        if(Environment.isExternalStorageManager()){
                            restoreDialog = true
                        }else{
                            val intent = Intent()
                            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                            val uri: Uri = android.net.Uri.fromParts("package", context.getPackageName(), null)
                            intent.data = uri
                            activityLauncher.launch(intent)
                        }
                    }else{
                        restoreDialog = true
                    }
                }
            }else{
                if (permissionName == WRITE_EXTERNAL_STORAGE && launcherKey == WRITE_EXTERNAL_STORAGE){
                    showPermissionDialog = true
                }else if (permissionName == READ_EXTERNAL_STORAGE && launcherKey == READ_EXTERNAL_STORAGE){
                    showPermissionDialog = true
                }
            }
        }
    }
    LaunchedEffect(key1 = true){
        viewModel.readCurrentThemeForDataStore().collectLatest {
            currentTheme = it
        }
    }
    RightToLeftLayout {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 5.dp,
                        horizontal = 10.dp
                    ),
                textAlign = TextAlign.Start,
                text = "تنظیمات تم:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DefaultRadioButton(
                    text = "روز",
                    selected = currentTheme == ThemeState.ThemeDay.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeDay.theme))
                    }
                )
                DefaultRadioButton(
                    text = "شب",
                    selected = currentTheme == ThemeState.ThemeNight.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeNight.theme))
                    }
                )
                DefaultRadioButton(
                    text = "گوشی",
                    selected = currentTheme == ThemeState.ThemeDefault.theme,
                    onSelect = {
                        viewModel.onEvent(SettingEvent.SaveTheme(ThemeState.ThemeDefault.theme))
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "ارتباط با توسعه دهنده:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    contactToDeveloperDialog = true

                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.Email,
                    contentDescription = "email",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "توضیحاتی درباره قسمت فروش:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    saleDialog = true
                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.Sell,
                    contentDescription = "sale",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "توضیحاتی درباره قسمت کالا و انبار:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    inventoryDialog = true
                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.Inventory,
                    contentDescription = "inventory",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "آموزش اضافه کردن امضا به فاکتور:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    signDialog = true
                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.WorkspacePremium,
                    contentDescription = "sign",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "گرفتن بک آپ:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    launcherKey = WRITE_EXTERNAL_STORAGE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        launcher.launch(
                            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,
                                MANAGE_EXTERNAL_STORAGE)
                        )
                    }else{
                        launcher.launch(
                            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                        )
                    }
                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.Backup,
                    contentDescription = "folder",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.5.dp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start,
                text = "بارگزاری بک آپ:",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = persian_font_regular,
                fontSize = 18.sp
            )
            Button(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp),
                onClick = {
                    launcherKey = READ_EXTERNAL_STORAGE
                    launcher.launch(
                        arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                    )
                },
                shape = CircleShape,
                colors =  ButtonDefaults.buttonColors(
                    backgroundColor = if(isSystemInDarkTheme()) Color.Yellow else Color(0xFF00CE08),
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .padding(end = 5.dp),
                    imageVector =  Icons.Default.RestorePage,
                    contentDescription = "folder",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.height(height = 10.dp))
            if (contactToDeveloperDialog){
                CustomContentDialog(
                    modifier = Modifier
                        .width(350.dp)
                        .height(200.dp),
                    title = "ارتباط با توسعه دهنده",
                    content = context.resources.getString(R.string.contact_to_developer),
                    setShowDialog = {
                        contactToDeveloperDialog = it
                    }
                ){
                    contactToDeveloperDialog = false
                }
            }
            if (inventoryDialog){
                CustomContentDialog(
                    modifier = Modifier
                        .width(350.dp)
                        .height(350.dp),
                    title = "کالا و انبار",
                    content = context.resources.getString(R.string.inventory_description),
                    setShowDialog = {
                        inventoryDialog = it
                    }
                ){
                    inventoryDialog = false
                }
            }
            if (saleDialog){
                CustomContentDialog(
                    modifier = Modifier
                        .width(350.dp)
                        .height(350.dp),
                    title = "فروش",
                    content = context.resources.getString(R.string.sale_description),
                    setShowDialog = {
                        saleDialog = it
                    }
                ){
                    saleDialog = false
                }
            }
            if (signDialog){
                CustomContentDialog(
                    modifier = Modifier
                        .width(350.dp)
                        .height(350.dp),
                    title = "کالا و انبار",
                    content = context.resources.getString(R.string.sign_description),
                    setShowDialog = {
                        signDialog = it
                    }
                ){
                    signDialog = false
                }
            }
            if(backupDialog){
                CustomAcceptRefuseDialog(
                    title = "گرفتن بک آپ",
                    content = context.resources.getString(R.string.backup_description),
                    positiveButtonTitle = "گرفتن بک آپ",
                    negativeButtonTitle = "خارج شدن",
                    positiveButtonColor = Color.Green,
                    negativeButtonColor = Color.Red,
                    onSuccess = {
                        viewModel.onEvent(SettingEvent.CreateBackup)
                        backupDialog = false
                    },
                    onCancel = {
                        backupDialog = false
                    },
                    setShowDialog = {
                        backupDialog = it
                    },
                    modifier = Modifier
                        .width(350.dp)
                        .height(350.dp),
                    contentModifier = Modifier.height(250.dp)
                )
            }
            if(restoreDialog){
                CustomAcceptRefuseDialog(
                    title = "بازیابی بک آپ",
                    content = context.resources.getString(R.string.restore_description),
                    positiveButtonTitle = "بازیابی بک آپ",
                    negativeButtonTitle = "خارج شدن",
                    positiveButtonColor = Color.Green,
                    negativeButtonColor = Color.Red,
                    onSuccess = {
                        viewModel.onEvent(SettingEvent.RestoreBackup)
                        restoreDialog = false
                    },
                    onCancel = {
                        restoreDialog = false
                    },
                    setShowDialog = {
                        restoreDialog = it
                    },
                    modifier = Modifier
                        .width(350.dp)
                        .height(360.dp)
                        .padding(all=5.dp),
                    contentModifier = Modifier.height(250.dp)
                )
            }
            if (showPermissionDialog){
                PermissionDialog(
                    onDismiss = {
                        showPermissionDialog = false
                    },
                    onGoToAppSettingsClick = {
                        (context as Activity).openAppSettings()
                    },
                    modifier = Modifier
                        .width(width = 300.dp)
                        .height(height = 200.dp),
                    shape = RoundedCornerShape(size = 20.dp),
                    backgroundColor = MaterialTheme.colors.background,
                    content = "این اپلیکیشن برای ذخیره فایل های بک آپ نیاز به دسترسی به حافظه داخلی تلفن همراه دارد."
                )
            }
        }
    }
}




