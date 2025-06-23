package com.example.data.di

import com.example.core.domain.repository.WeatherRepository
import com.example.data.WeatherRepositoryImp
import com.example.data.datasource.LocalDataSource
import com.example.data.datasource.RemoteDataSource
import com.example.data.datasource.local.LocalDataSourceImp
import com.example.data.datasource.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

   @Binds
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: LocalDataSourceImp): LocalDataSource

    @Binds
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImp): WeatherRepository
}