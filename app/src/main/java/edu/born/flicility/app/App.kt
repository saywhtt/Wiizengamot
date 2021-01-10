package edu.born.flicility.app

import android.app.Application
import edu.born.flicility.di.components.DaggerAppComponent
import edu.born.flicility.di.modules.AppModule
import edu.born.flicility.di.components.PhotoComponent
import edu.born.flicility.di.modules.PhotoModule

class App : Application() {

    val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

    private var photoComponent: PhotoComponent? = null

    fun plusPhotoComponent(): PhotoComponent {
        if (photoComponent == null)
            photoComponent = appComponent.photoComponentBuilder().photoModule(PhotoModule()).build()
        return photoComponent!!
    }

    fun clearPhotoComponent() {
        photoComponent = null
    }
}