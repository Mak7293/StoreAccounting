package com.example.storeaccounting.presentation.view_model.inventory_sale

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
import com.example.storeaccounting.domain.use_case.inventory_use_case.InventoryUseCases
import com.example.storeaccounting.domain.util.TransactionState
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
class InventorySaleViewModel@Inject constructor(
    private val inventoryUseCases: InventoryUseCases,
    private val applicationContext: Application
    ):ViewModel() {

    private val _state = mutableStateOf<InventorySaleViewModelState>(InventorySaleViewModelState())
    val state: State<InventorySaleViewModelState> = _state

    private val _eventFlow = MutableSharedFlow<InventorySaleUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getInventoryJob: Job? = null
    private var getHistoryJob: Job? = null

    var filterState = mutableStateOf(false)
    var filterRange: Map<String,PersianDate>? = null

    init {
        getInventory()
        getAllHistory()
    }
    fun onEvent(inventorySaleEvent: InventorySaleEvent){
        when(inventorySaleEvent){
            is InventorySaleEvent.InsertInventory ->  {
                insertInventoryToDatabase(inventorySaleEvent.inventoryEntity)
            }
            is InventorySaleEvent.DeleteInventory ->  {
                deleteTransactionFromDatabase(inventorySaleEvent.inventoryEntity)
            }
            is InventorySaleEvent.UpdateInventory ->  {
                updateInventory(inventorySaleEvent.inventoryEntity)
            }
            is InventorySaleEvent.SaleInventory  ->  {
                saleInventory(inventorySaleEvent.inventoryEntity,inventorySaleEvent.history)
            }
            is InventorySaleEvent.UpdateSaleTransaction  ->  {
                updateSaleHistory(
                    inventoryEntity = inventorySaleEvent.inventoryEntity,
                    newHistory = inventorySaleEvent.newHistory,
                    oldHistory = inventorySaleEvent.oldHistory
                )
            }
            is InventorySaleEvent.DeleteSaleHistory  ->   {
                deleteSaleHistory(inventorySaleEvent.history)
            }
            is InventorySaleEvent.FilterSaleHistory  ->   {
                filteredHistory(inventorySaleEvent.map)
            }
            is InventorySaleEvent.FilterInventory   ->   {
                filteredInventory(inventorySaleEvent.query)
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
            _eventFlow.emit(InventorySaleUiEvent.FilteredHistoryList)
        }
        filterState.value = dateRange != null

    }
    fun graphHistoryList(from: Long, until: Long): List<History>{
        var history: List<History> = listOf()
        history = _state.value.history.filter {
            it.timeStamp in from until  until &&
                    it.transaction == TransactionState.Sale.state
        }
        Log.d("resultList",history.toString())
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
            if(it == list.last()){
                result.put(key = day,value = totalProfitInDay)
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
                InventorySaleUiEvent.FilteredInventoryList
            )
        }
        Log.d("filterInventory", state.value.filteredInventory.toString())
    }
    private fun deleteSaleHistory(history: History){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                inventoryUseCases.deleteSaleHistory(history)
                _eventFlow.emit(
                    InventorySaleUiEvent.DeleteSaleHistory
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(
                        message = applicationContext.resources.getString(R.string.alert_delete_sale_inventory)
                    )
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
                    InventorySaleUiEvent.UpdateSaleHistory
                )
            }catch (e: InvalidTransactionException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(message = e.message!!)
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(
                        message = applicationContext.resources.getString(R.string.alert_update_sale_inventory)
                    )
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
                    InventorySaleUiEvent.SaleInventory
                )
            }catch (e: InvalidTransactionException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(message = e.message!!)
                )
            }
        }
    }
    private fun updateInventory(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                inventoryUseCases.updateInventory(inventoryEntity)
                _eventFlow.emit(
                    InventorySaleUiEvent.UpdateInventory
                )

            }catch (e: InvalidTransactionException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(message = e.message!!)
                )
            }catch (e: SQLException){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(
                        message = applicationContext.resources.getString(R.string.alert_sale_inventory)
                    )
                )
            }
        }
    }
    private fun insertInventoryToDatabase(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
             try {
                 inventoryUseCases.addInventory(inventoryEntity)
                 _eventFlow.emit(
                     InventorySaleUiEvent.SaveInventory
                 )
            }catch (e: InvalidTransactionException){
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(message = e.message!!)
                )
            }
        }
    }
    private fun deleteTransactionFromDatabase(inventoryEntity: InventoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                inventoryUseCases.deleteInventory(inventoryEntity)
                _eventFlow.emit(
                    InventorySaleUiEvent.DeleteInventory
                )
            }catch(e: Exception){
                e.printStackTrace()
                _eventFlow.emit(
                    InventorySaleUiEvent.ShowToast(
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
    sealed class InventorySaleUiEvent{
        data class ShowToast(val message: String): InventorySaleUiEvent()
        object SaveInventory: InventorySaleUiEvent()
        object UpdateInventory: InventorySaleUiEvent()
        object DeleteInventory: InventorySaleUiEvent()
        object SaleInventory: InventorySaleUiEvent()
        object UpdateSaleHistory: InventorySaleUiEvent()
        object DeleteSaleHistory: InventorySaleUiEvent()
        object FilteredHistoryList: InventorySaleUiEvent()
        object FilteredInventoryList: InventorySaleUiEvent()
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