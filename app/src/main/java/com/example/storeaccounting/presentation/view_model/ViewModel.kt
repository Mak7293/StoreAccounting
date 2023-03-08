package com.example.storeaccounting.presentation.view_model

import android.app.Application
import android.database.SQLException
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

@HiltViewModel
class ViewModel@Inject constructor(
    private val inventoryUseCases: UseCases,
    private val applicationContext: Application
    ):ViewModel() {

    private val _state = mutableStateOf<ViewModelState>(ViewModelState())
    val state: State<ViewModelState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getInventoryJob: Job? = null
    private var getHistoryJob: Job? = null

    init {
        getInventory()
        getHistory()
    }
    fun onEvent(event: Event){
        when(event){
            is Event.InsertInventory ->  {
                insertInventoryToDatabase(event.inventoryEntity)
            }
            is Event.DeleteInventory ->  {
                deleteTransactionFromDatabase(event.inventoryEntity)
            }
            is Event.UpdateInventory ->  {
                updateInventory(event.inventoryEntity)
            }
            is Event.SaleInventory  ->  {
                saleInventory(event.newInventoryEntity,event.previousInventory)
            }
        }
    }

    private fun saleInventory(
        newInventoryEntity: InventoryEntity,
        previousInventoryEntity: InventoryEntity){

        viewModelScope.launch(Dispatchers.IO){
            try{
                inventoryUseCases.saleInventory(
                    newInventoryEntity = newInventoryEntity,
                    previousInventoryEntity = previousInventoryEntity)
                _eventFlow.emit(
                    UiEvent.SaveInventory
                )
            }catch (e: InvalidTransactionException){
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowToast(message = e.message!!)
                )
            }
        }
    }
    private fun updateInventory(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                inventoryUseCases.updateInventory(inventoryEntity)
                _eventFlow.emit(
                    UiEvent.UpdateInventory
                )

            }catch (e: InvalidTransactionException){
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowToast(message = e.message!!)
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowToast(message =  applicationContext.
                    resources.getString(R.string.alert_sale_inventory))
                )
            }
        }
    }
    private fun insertInventoryToDatabase(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
             try {
                 inventoryUseCases.addInventory(inventoryEntity)
                 _eventFlow.emit(
                     UiEvent.SaveInventory
                 )
            }catch (e: InvalidTransactionException){
                _eventFlow.emit(
                    UiEvent.ShowToast(message = e.message!!)
                )
            }
        }
    }
    private fun deleteTransactionFromDatabase(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                inventoryUseCases.deleteInventory(inventoryEntity)
                _eventFlow.emit(
                    UiEvent.DeleteInventory
                )
            }catch(e: Exception){
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowToast(
                        message =
                        applicationContext.resources.getString(R.string.alert_delete_inventory)
                    )
                )
            }
        }
    }
    fun getPersianDate(): PersianDate{
        return PersianDate()
    }
    fun getHistoryList(createdTimeStamp: Long): List<History> {
        val list: List<History>
        runBlocking {
            list = inventoryUseCases.getHistoryListForSpecificInventory(createdTimeStamp).history
        }
        return list
    }
    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object SaveInventory: UiEvent()
        object UpdateInventory: UiEvent()
        object DeleteInventory: UiEvent()
        object SaleInventory: UiEvent()
    }
    private fun getInventory(){
        getInventoryJob?.cancel()
        getInventoryJob = inventoryUseCases.getInventory()
            .onEach {
                _state.value  = state.value.copy(
                    inventory = it,
                )
            }
            .launchIn(viewModelScope)
    }
    private fun getHistory(){
        getHistoryJob?.cancel()
        getHistoryJob = inventoryUseCases.getHistory()
            .onEach {
                _state.value = state.value.copy(
                    history = it
                )
            }
            .launchIn(viewModelScope)
    }

}