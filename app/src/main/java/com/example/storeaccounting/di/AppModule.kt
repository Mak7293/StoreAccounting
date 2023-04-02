package com.example.storeaccounting.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.storeaccounting.data.data_source.Database
import com.example.storeaccounting.data.repository.RepositoryImp
import com.example.storeaccounting.domain.repository.Repository
import com.example.storeaccounting.domain.use_case.*
import com.example.storeaccounting.domain.use_case.general_use_case.*
import com.example.storeaccounting.domain.use_case.inventory_use_case.*
import com.example.storeaccounting.presentation.util.Constants.USER_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database{
        return Room.databaseBuilder(
            app,
            Database::class.java,
            Database.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: Database): Repository {
        return RepositoryImp(db.transactionDao,db.creditCardDao)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }


    @Provides
    @Singleton
    fun provideInventoryUseCases(repository: Repository): InventoryUseCases {
        return InventoryUseCases(
            addInventory = AddInventory(repository),
            getInventory = GetInventory(repository),
            deleteInventory = DeleteInventory(repository),
            updateInventory = UpdateInventory(repository),
            getHistoryListForSpecificInventory = GetHistoryListForSpecificInventory(repository),
            getHistory = GetHistory(repository),
            saleInventory = SaleInventory(repository),
            updateSaleHistory = UpdateSaleHistory(repository),
            deleteSaleHistory = DeleteSaleHistory(repository)
        )
    }
    @Provides
    @Singleton
    fun provideGeneralUseCases(repository: Repository): GeneralUseCases {
        return GeneralUseCases(
            createUseCases = CreateCreditCard(repository),
            getAllCreditCardList = GetAllCreditCardList(repository),
            deleteCreditCard = DeleteCreditCard(repository),
            updateCreditCard = UpdateCreditCard(repository)
        )
    }

}