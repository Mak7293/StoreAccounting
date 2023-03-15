package com.example.storeaccounting.presentation.view_model

import android.app.Application
import android.database.SQLException
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeaccounting.R
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject
import com.example.storeaccounting.presentation.util.Constants.FROM
import com.example.storeaccounting.presentation.util.Constants.UNTIL
import kotlinx.coroutines.flow.*

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

    var filterState = mutableStateOf(false)
    var filterRange: Map<String,PersianDate>? = null

    init {
        getInventory()
        getAllHistory()
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
                saleInventory(event.inventoryEntity,event.history)
            }
            is Event.UpdateSaleTransaction  ->  {
                updateSaleHistory(
                    inventoryEntity = event.inventoryEntity,
                    newHistory = event.newHistory,
                    oldHistory = event.oldHistory
                )
            }
            is Event.DeleteSaleHistory  ->   {
                deleteSaleHistory(event.history)
            }
            is Event.FilterSaleHistory  ->   {
                filteredHistory(event.map)
            }
            is Event.FilterInventory   ->   {
                filteredInventory(event.query)
            }
        }
    }
    private fun filteredHistory(dateRange: Map<String,PersianDate> ?){
        filterRange = dateRange
        val from: Long? = dateRange?.get(FROM)?.toDate()?.time
        val until: Long? = dateRange?.get(UNTIL)?.toDate()?.time
        if (from != null && until != null){
            _state.value.filteredHistory = _state.value.history.filter {
                it.timeStamp in from until until ||
                        it.timeStamp in until until from
            }
        }else{
            _state.value.filteredHistory = _state.value.history
        }
        viewModelScope.launch(Dispatchers.IO) {
            _eventFlow.emit(UiEvent.FilteredHistoryList)
        }
        filterState.value = true

    }
    fun graphHistoryList(from: Long, until: Long): List<History>{
        var history: List<History> = listOf()
        history = _state.value.history.filter {
            it.timeStamp in from until  until
        }
        return history
    }
    fun mapSaleProfitByDay(list: List<History>):MutableMap<Int,Number>{
        val result: MutableMap<Int,Number> = mutableMapOf()
        var iDate = ""
        var totalProfitInDay: Float = 0.0f
        var day: Int = 0
        list.forEach {
            if(it.date != iDate){
                iDate = it.date
                result.put(key = day,value = totalProfitInDay)
                totalProfitInDay = 0.0f
                day++
            }
            if (it.date == iDate){
                totalProfitInDay += ((it.sellPrice.toLong() - it.buyPrice.toLong()) * it.saleNumber.toLong())
            }
        }
        result.remove(0)
        return result
    }
    private fun filteredInventory(query: String){
        if (query.isEmpty()){
            state.value.filteredInventory = state.value.inventory
        }else{
            state.value.filteredInventory = state.value.inventory.filter {
                it.title.contains(
                    query.trim(), ignoreCase = true
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            _eventFlow.emit(
                UiEvent.FilteredInventoryList
            )
        }
        Log.d("filterInventory", state.value.filteredInventory.toString())
    }
    private fun deleteSaleHistory(history: History){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                inventoryUseCases.deleteSaleHistory(history)
                _eventFlow.emit(
                    UiEvent.DeleteSaleHistory
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    UiEvent.ShowToast(message =  applicationContext.
                    resources.getString(R.string.alert_delete_sale_inventory))
                )
            }
        }
    }
    private fun updateSaleHistory(
        inventoryEntity: InventoryEntity,
        newHistory: History,
        oldHistory: History
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                inventoryUseCases.updateSaleHistory(
                    inventoryEntity = inventoryEntity,
                    newHistory = newHistory,
                    oldHistory = oldHistory
                )
                _eventFlow.emit(
                    UiEvent.UpdateSaleHistory
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
                    resources.getString(R.string.alert_update_sale_inventory))
                )
            }
        }
    }

    private fun saleInventory(
        inventoryEntity: InventoryEntity,
        history: History
    ){
        viewModelScope.launch(Dispatchers.IO){
            try{
                inventoryUseCases.saleInventory(
                    inventoryEntity = inventoryEntity,
                    history = history
                )
                _eventFlow.emit(
                    UiEvent.SaleInventory
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

    suspend fun getHistoryByInventory(createdTimeStamp: Long):InventoryWithHistory{
        return inventoryUseCases.getHistoryListForSpecificInventory(createdTimeStamp)
    }
    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object SaveInventory: UiEvent()
        object UpdateInventory: UiEvent()
        object DeleteInventory: UiEvent()
        object SaleInventory: UiEvent()
        object UpdateSaleHistory: UiEvent()
        object DeleteSaleHistory: UiEvent()
        object FilteredHistoryList: UiEvent()
        object FilteredInventoryList: UiEvent()
    }
    private fun getInventory(){
        getInventoryJob?.cancel()
        getInventoryJob = inventoryUseCases.getInventory()
            .onEach {
                _state.value  = state.value.copy(
                    inventory = it,
                    filteredInventory = it
                )
            }
            .launchIn(viewModelScope)

    }
    private fun getAllHistory(){
        getHistoryJob?.cancel()
        getHistoryJob = inventoryUseCases.getHistory()
            .onEach {
                _state.value = state.value.copy(
                    history = it,
                    filteredHistory = it
                )
            }
            .launchIn(viewModelScope)
    }

}