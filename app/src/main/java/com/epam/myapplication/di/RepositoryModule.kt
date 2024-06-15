package com.epam.myapplication.di

import com.epam.myapplication.data.repository.Repository
import com.epam.myapplication.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRepository(repository: RepositoryImpl): Repository
}