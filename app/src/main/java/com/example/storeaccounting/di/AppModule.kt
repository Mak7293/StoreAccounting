package com.example.storeaccounting.di

import android.app.Application
import androidx.room.Room
import com.example.storeaccounting.data.data_source.TransactionDatabase
import com.example.storeaccounting.data.repository.InventoryRepositoryImp
import com.example.storeaccounting.domain.repository.InventoryRepository
import com.example.storeaccounting.domain.use_case.*
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
    fun provideNoteDatabase(app: Application): TransactionDatabase{
        return Room.databaseBuilder(
            app,
            TransactionDatabase::class.java,
            TransactionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: TransactionDatabase): InventoryRepository {
        return InventoryRepositoryImp(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: InventoryRepository, app: Application): UseCases{
        return UseCases(
            addInventory = AddInventory(repository,app.resources),
            getInventory = GetInventory(repository),
            deleteInventory = DeleteInventory(repository),
            updateInventory = UpdateInventory(repository,app.resources),
            getHistoryListForSpecificInventory = GetHistoryListForSpecificInventory(repository)
        )
    }

}