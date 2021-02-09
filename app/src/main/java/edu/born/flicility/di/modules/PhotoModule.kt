package edu.born.flicility.di.modules

import dagger.Module
import dagger.Provides
import edu.born.flicility.di.scopes.PhotoScope
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.*
import edu.born.flicility.presenters.impl.PhotoListPresenterImpl
import edu.born.flicility.presenters.impl.ConcurrencyPhotoPresenterImpl
import edu.born.flicility.presenters.impl.PhotoSearchPresenterImpl

@Module
class PhotoModule {
    @Provides
    @PhotoScope
    fun providePhotoListPresenter(photoService: PhotoService): PhotoListPresenter {
        return PhotoListPresenterImpl(photoService)
    }

    @Provides
    @PhotoScope
    fun providePhotoSearchPresenter(photoService: PhotoService): PhotoSearchPresenter {
        return PhotoSearchPresenterImpl(photoService)
    }

    @Provides
    @PhotoScope
    fun providePhotoPresenter(photoService: PhotoService): PhotoPresenter {
        return ConcurrencyPhotoPresenterImpl(photoService)
    }
}