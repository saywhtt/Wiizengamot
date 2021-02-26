package edu.born.flicility.di.components

import dagger.Component
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.di.modules.AppModule
import edu.born.flicility.service.PollWorkerService
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun photoComponentBuilder(): PhotoComponent.Builder
    fun inject(photoAdapter: PhotoAdapter)
    fun inject(pollWorkerService: PollWorkerService)
}