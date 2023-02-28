package com.example.storeaccounting.presentation.view_model

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import java.util.InvalidPropertiesFormatException
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val applicationContext: Application):ViewModel() {

    private val _state = mutableStateOf<ViewModelState>(ViewModelState())
    val state: State<ViewModelState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: Event){
        when(event){
            is Event.InsertInventory  ->  {
                insertTransactionToDatabase(event.transaction)
            }
            is Event.DeleteInventory  ->  {

            }
            is Event.UpdateInventory  ->  {

            }

        }

    }
    private fun insertTransactionToDatabase(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
             try {
                transactionUseCases.addTransaction(transaction = transaction)
                 _eventFlow.emit(
                     UiEvent.SaveNote
                 )
            }catch (e: InvalidTransactionException){
                _eventFlow.emit(
                    UiEvent.ShowToast(message = e.message!!)
                )
            }
        }
    }
    fun getPersianDate(): PersianDate{
        return PersianDate()
    }
    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}