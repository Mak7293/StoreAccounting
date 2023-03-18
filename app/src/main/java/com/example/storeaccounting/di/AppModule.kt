package com.example.storeaccounting.di

import android.app.Application
import androidx.room.Room
import com.example.storeaccounting.data.data_source.TransactionDatabase
import com.example.storeaccounting.data.repository.RepositoryImp
import com.example.storeaccounting.domain.repository.Repository
import com.example.storeaccounting.domain.use_case.*
import com.example.storeaccounting.domain.use_case.general_use_case.*
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
    fun provideRepository(db: TransactionDatabase): Repository {
        return RepositoryImp(db.transactionDao,db.creditCardDao)
    }

    @Provides
    @Singleton
    fun provideInventoryUseCases(repository: Repository, app: Application): InventoryUseCases {
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
    fun provideGeneralUseCases(repository: Repository): GeneralUseCases {
        return GeneralUseCases(
            createUseCases = CreateCreditCard(repository),
            getAllCreditCardList = GetAllCreditCardList(repository),
            deleteCreditCard = DeleteCreditCard(repository),
            updateCreditCard = UpdateCreditCard(repository)
        )
    }

}