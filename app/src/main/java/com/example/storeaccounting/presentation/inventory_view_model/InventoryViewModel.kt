package com.example.storeaccounting.presentation.inventory_view_model

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel@Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val applicationContext: Application):ViewModel() {

    private val _state = mutableStateOf<InventoryViewModelState>(InventoryViewModelState())
    val state: State<InventoryViewModelState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getInventoryJob: Job? = null

    init {
        getInventory()
    }
    fun onEvent(event: InventoryEvent){
        when(event){
            is InventoryEvent.InsertInventory  ->  {
                insertTransactionToDatabase(event.transaction)
            }
            is InventoryEvent.DeleteInventory  ->  {
                deleteTransactionFromDatabase(event.transaction)
            }
            is InventoryEvent.UpdateInventory  ->  {

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
    private fun deleteTransactionFromDatabase(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionUseCases.deleteTransaction(transaction)
            }catch(e: Exception){
                e.printStackTrace()
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
    private fun getInventory(){
        getInventoryJob?.cancel()
        getInventoryJob = transactionUseCases.getInventoryTransaction()
            .onEach {
                _state.value  = state.value.copy(
                    inventory = it,
                )
            }
            .launchIn(viewModelScope)
    }
}