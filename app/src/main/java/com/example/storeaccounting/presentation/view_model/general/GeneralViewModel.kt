package com.example.storeaccounting.presentation.view_model.general

import android.app.Application
import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.media.MediaScannerConnection
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.use_case.general_use_case.GeneralUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel@Inject constructor(
    private val applicationContext: Application,
    private val generalUseCases: GeneralUseCases
): ViewModel(){

    private var appDir: String = ""
    private val _state = mutableStateOf<GeneralViewModelState>(GeneralViewModelState())
    val state: State<GeneralViewModelState> = _state


    private val _eventFlow = MutableSharedFlow<GeneralUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getAllCreditCard()
    }

    private var getCreditCardJob: Job? = null
    fun onEvent(event: GeneralEvent){
        when(event){
            is GeneralEvent.CreateCreditCard   ->   {
                createCreditCard(event.creditCard)
            }
            is GeneralEvent.UpdateCreditCard   ->   {
                updateCreditCard(event.creditCard)
            }
            is GeneralEvent.DeleteCreditCard   ->  {
                deleteCreditCard(event.creditCard)
            }
            is GeneralEvent.FilterCreditCard   ->  {
                filterCreditCardByUser(event.query)
            }
            is GeneralEvent.SaveFactorInPdf   ->  {
                applicationContext.saveFactorInPdf(event.bitmap)
            }
        }
    }
    private fun Application.createFactorDirectoryAndCheckIt():Boolean{
        val dir: File = File("${getExternalFilesDir(null)}/Saved Pdf")
        appDir = dir.toString()
        if(!dir.exists()){
            return dir.mkdir()
        }else{
            return true
        }
    }
    private fun Application.saveFactorInPdf(bitmap: Bitmap?){
        viewModelScope.launch {
            if (createFactorDirectoryAndCheckIt()){
                var result = ""
                withContext(Dispatchers.IO){
                    if(bitmap != null){
                        try{
                            val document = PdfDocument()
                            val pageInfo =
                                PageInfo.Builder(bitmap.width, bitmap.height,1)
                                    .create()
                            val page = document.startPage(pageInfo)
                            val canvas = page.canvas

                            val paint = Paint()
                            canvas.drawPaint(paint)

                            canvas.drawBitmap(bitmap, 0f, 0f, null)
                            document.finishPage(page)
                            val file = File( appDir + File.separator +
                                    "factor_image" + System.currentTimeMillis()/1000 + ".pdf" )
                            val fo = FileOutputStream(file)
                            document.writeTo(fo)
                            document.close()
                            fo.close()
                            result = file.absolutePath
                            if(result.isNotEmpty()){
                                Log.d("path",result)
                                _eventFlow.emit(
                                    GeneralUiEvent.ShowToast(message = " فاکتور با موفقیت ذخیره شد: $result")
                                )
                                _eventFlow.emit(
                                    GeneralUiEvent.ShareFactor(result)
                                )
                            }else{
                                _eventFlow.emit(
                                    GeneralUiEvent.ShowToast(message = "خطا در ذخیره فاکتور، لطفا دوباره تلاش کنید.")
                                )
                            }
                        }catch(e: Exception){
                            result = ""
                            e.printStackTrace()
                        }
                    }
                }
            }else{
                _eventFlow.emit(
                    GeneralUiEvent.ShowToast(message = "خطا در ساخت دایرکتوری")
                )
            }
        }
    }

    private fun filterCreditCardByUser(query: String){
        if (query.isEmpty()){
            state.value.filteredCreditCard = state.value.creditCard
        }else{
            state.value.filteredCreditCard = state.value.creditCard.filter {
                it.userName.contains(
                    query.trim(), ignoreCase = true
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            _eventFlow.emit(
                GeneralUiEvent.FilteredCreditCard
            )
        }
    }
    private fun createCreditCard(creditCard: CreditCard){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                generalUseCases.createUseCases(creditCard)
                _eventFlow.emit(
                    GeneralUiEvent.CreateCreditCard
                )
            }catch(e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    GeneralUiEvent.ShowToast(message = "خطا در ثبت کارت در دیتابیس، لطفا دوباره تلاش کنید.")
                )
            }
        }
    }
    private fun deleteCreditCard(creditCard: CreditCard){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                generalUseCases.deleteCreditCard(creditCard)
                _eventFlow.emit(
                    GeneralUiEvent.DeleteCreditCard
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    GeneralUiEvent.ShowToast(message = "خطا در حذف کارت از دیتابیس، لطفا دوباره تلاش کنید.")
                )

            }
        }
    }
    private fun updateCreditCard(creditCard: CreditCard){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                generalUseCases.updateCreditCard(creditCard)
                _eventFlow.emit(
                    GeneralUiEvent.UpdateCreditCard
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    GeneralUiEvent.ShowToast(message = "خطا در به روز رسانی کارت ، لطفا دوباره تلاش کنید.")
                )
            }
        }
    }
    sealed class GeneralUiEvent{
        data class ShowToast(val message: String): GeneralUiEvent()
        object CreateCreditCard: GeneralUiEvent()
        object DeleteCreditCard: GeneralUiEvent()
        object UpdateCreditCard: GeneralUiEvent()
        object FilteredCreditCard: GeneralUiEvent()
        data class ShareFactor(val path: String): GeneralUiEvent()

    }
    fun getPersianDate(): PersianDate {
        return PersianDate()
    }
    private fun getAllCreditCard(){
        getCreditCardJob?.cancel()
        getCreditCardJob = generalUseCases.getAllCreditCardList()
            .onEach {
                _state.value = state.value.copy(
                    creditCard = it,
                    filteredCreditCard = it
                )
            }
            .launchIn(viewModelScope)
    }



}