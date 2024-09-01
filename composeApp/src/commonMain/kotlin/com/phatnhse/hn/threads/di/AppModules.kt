package com.phatnhse.hn.threads.di

import com.phatnhse.hn.threads.AppViewModel
import com.phatnhse.hn.threads.repo.HackerNewsRepository
import com.phatnhse.hn.threads.repo.HackerNewsRepositoryImpl
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModules = module {
    viewModel<AppViewModel> {
        AppViewModel(get())
    }

    factory<HackerNewsRepository> {
        HackerNewsRepositoryImpl(
            appDatabase = get(),
            hnRemoteDataSource = get()
        )
    }
}

expect val platformModule: Module
