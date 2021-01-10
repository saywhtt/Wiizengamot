package edu.born.flicility.di.components

import dagger.Component
import edu.born.flicility.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun photoComponentBuilder(): PhotoComponent.Builder
}