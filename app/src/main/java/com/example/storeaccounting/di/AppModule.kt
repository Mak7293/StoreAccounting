package com.example.storeaccounting.di

import android.app.Application
import androidx.room.Room
import com.example.storeaccounting.data.data_source.TransactionDatabase
import com.example.storeaccounting.data.repository.TransactionRepositoryImp
import com.example.storeaccounting.domain.repository.TransactionRepository
import com.example.storeaccounting.domain.use_case.AddTransaction
import com.example.storeaccounting.domain.use_case.DeleteTransaction
import com.example.storeaccounting.domain.use_case.GetInventoryTransaction
import com.example.storeaccounting.domain.use_case.TransactionUseCases
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
    fun provideNoteRepository(db: TransactionDatabase): TransactionRepository {
        return TransactionRepositoryImp(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: TransactionRepository,app: Application): TransactionUseCases{
        return TransactionUseCases(
            addTransaction = AddTransaction(repository,app.resources),
            getInventoryTransaction = GetInventoryTransaction(repository),
            deleteTransaction = DeleteTransaction(repository)
        )
    }

}