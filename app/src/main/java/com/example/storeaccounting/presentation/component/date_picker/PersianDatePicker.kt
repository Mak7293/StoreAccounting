package com.example.storeaccounting.presentation.component.date_picker

import android.widget.NumberPicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.storeaccounting.presentation.component.RightToLeftLayout
import com.razaghimahdi.compose_persian_date.core.PersianDataPickerController
import com.example.storeaccounting.presentation.component.date_picker.Tools.changeDividerColor
import com.example.storeaccounting.presentation.component.date_picker.Tools.toPersianNumber

@Composable
internal fun PersianDataPicker(
    spacerColor: Color = Color.DarkGray,
    textColor: Int = Color.DarkGray.toArgb(),
    textSize: Float = (14.sp).value,
    controller: PersianDataPickerController,
    modifier: Modifier = Modifier,
    onDateChanged: ((year: Int, month: Int, day: Int) -> Unit)? = null
){

    controller.initDate()
    RightToLeftLayout {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeCustomNumberPicker(
                modifier = Modifier.wrapContentWidth(),
                formatter = { i -> i.toString().toPersianNumber() },
                onValueChangedListener = { picker, oldVal, newVal ->
                    controller.updateFromCustomNumberPicker(newDay = newVal)
                    if (onDateChanged != null) {
                        onDateChanged(
                            controller.selectedYear,
                            controller.selectedMonth,
                            controller.selectedDay
                        )
                    }

                },
                minValue = 1,
                maxValue = controller.maxDay,
                selectedValue = controller.selectedDay,
            )

            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .width(4.dp)
            )

            ComposeCustomNumberPicker(
                modifier = Modifier.wrapContentWidth(),
                formatter = { i -> i.toString().toPersianNumber() },
                onValueChangedListener = { picker, oldVal, newVal ->
                    controller.updateFromCustomNumberPicker(newMonth = newVal)
                    if (onDateChanged != null) {
                        onDateChanged(
                            controller.selectedYear,
                            controller.selectedMonth,
                            controller.selectedDay
                        )
                    }
                },
                minValue = 1,
                maxValue = if (controller.maxMonth > 0) controller.maxMonth else 12,
                selectedValue = controller.selectedMonth,
                displayedValues = if (controller.displayMonthNames) Constants.persianMonthNames else null
            )

            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .width(4.dp)
            )

            ComposeCustomNumberPicker(
                modifier = Modifier.wrapContentWidth(),
                formatter = { i -> i.toString().toPersianNumber() },
                onValueChangedListener = { picker, oldVal, newVal ->
                    controller.updateFromCustomNumberPicker(newYear = newVal)
                    if (onDateChanged != null) {
                        onDateChanged(
                            controller.selectedYear,
                            controller.selectedMonth,
                            controller.selectedDay
                        )
                    }
                },
                minValue = controller.minYear,
                maxValue = controller.maxYear,
                selectedValue = controller.selectedYear
            )

        }

    }

}

@Composable
fun ComposeCustomNumberPicker(
    modifier: Modifier,
    formatter: NumberPicker.Formatter,
    onValueChangedListener: NumberPicker.OnValueChangeListener,
    minValue: Int,
    maxValue: Int,
    selectedValue: Int,
    dividerColor: Color = MaterialTheme.colors.primaryVariant,
    displayedValues: Array<String>? = null,
    textStyle: TextStyle = LocalTextStyle.current
) {


    GlobalStyle.textSize = (24.sp).value
    GlobalStyle.textColor = MaterialTheme.colors.primaryVariant.toArgb()


    /* NumberPicker(
         value = selectedValue,
         range = minValue..maxValue,
         onValueChange = {
             pickerValue = it
         }
     )*/

    AndroidView(
        modifier = modifier,
        factory = { context ->

            val numberPicker = CustomNumberPicker(context = context)
             changeDividerColor(
                 numberPicker,
                 dividerColor.toArgb()
             )
            numberPicker.setFormatter(formatter)
            numberPicker.minValue = minValue
            numberPicker.maxValue = maxValue
            numberPicker.value = selectedValue
            numberPicker.setOnValueChangedListener(onValueChangedListener)
            if (displayedValues != null) numberPicker.displayedValues = displayedValues

            numberPicker
        },
        update = { numberPicker ->
            numberPicker.minValue = minValue
            numberPicker.maxValue = maxValue
            numberPicker.value = selectedValue
        }
    )
}
