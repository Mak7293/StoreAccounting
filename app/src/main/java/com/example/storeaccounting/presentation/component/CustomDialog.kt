package com.example.storeaccounting.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    positiveButtonTitle: String,
    negativeButtonTitle: String,
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
                    modifier = Modifier.fillMaxSize().background(
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
                        color = Color.DarkGray
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
                            modifier = Modifier.width(120.dp).padding(5.dp),
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red
                            ),
                            onClick = {
                                onSuccess()
                            }
                        ) {
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
                            modifier = Modifier.width(120.dp).padding(5.dp),
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF00CF09)
                            ),
                            onClick = {
                                onCancel()
                            }
                        ) {
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