package com.example.storeaccounting.di

import android.app.Application
import androidx.room.Room
import com.example.storeaccounting.data.data_source.TransactionDatabase
import com.example.storeaccounting.data.repository.InventoryRepositoryImp
import com.example.storeaccounting.domain.repository.InventoryRepository
import com.example.storeaccounting.domain.use_case.*
import com.example.storeaccounting.domain.use_case.general_use_case.CreateCreditCard
import com.example.storeaccounting.domain.use_case.general_use_case.GeneralUseCases
import com.example.storeaccounting.domain.use_case.inventory_use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TransactionDatabase{
        return Room.databaseBuilder(
            app,
            TransactionDatabase::class.java,
            TransactionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: TransactionDatabase): InventoryRepository {
        return InventoryRepositoryImp(db.transactionDao,db.creditCardDao)
    }

    @Provides
    @Singleton
    fun provideInventoryUseCases(repository: InventoryRepository, app: Application): InventoryUseCases {
        return InventoryUseCases(
            addInventory = AddInventory(repository,app.resources),
            getInventory = GetInventory(repository),
            deleteInventory = DeleteInventory(repository),
            updateInventory = UpdateInventory(repository,app.resources),
            getHistoryListForSpecificInventory = GetHistoryListForSpecificInventory(repository),
            getHistory = GetHistory(repository),
            saleInventory = SaleInventory(repository,app.resources),
            updateSaleHistory = UpdateSaleHistory(repository,app.resources),
            deleteSaleHistory = DeleteSaleHistory(repository)
        )
    }
    @Provides
    @Singleton
    fun provideGeneralUseCases(repository: InventoryRepository): GeneralUseCases {
        return GeneralUseCases(
            createUseCases = CreateCreditCard(repository)
        )
    }

}