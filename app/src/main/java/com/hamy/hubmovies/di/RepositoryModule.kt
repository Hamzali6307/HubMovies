package com.hamy.hubmovies.di

import com.hamy.hubmovies.data.repository.MovieRepositoryImpl
import com.hamy.hubmovies.features.movies.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideMovieRepository(repo: MovieRepositoryImpl): MovieRepository
}