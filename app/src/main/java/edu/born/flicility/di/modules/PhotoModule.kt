package edu.born.flicility.di.modules

import dagger.Module
import dagger.Provides
import edu.born.flicility.di.scopes.PhotoScope
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.presenters.impl.ConcurrencyPhotoPresenterImpl
import edu.born.flicility.presenters.impl.PhotoListPresenterImpl
import edu.born.flicility.presenters.impl.PhotoSearchPresenterImpl
import edu.born.flicility.views.PhotoListView
import javax.inject.Named

@Module
class PhotoModule {
    @Provides
    @PhotoScope
    @Named("photoListFragment")
    fun providePhotoListPresenter(photoService: PhotoService): PhotoListPresenter<PhotoListView> {
        return PhotoListPresenterImpl(photoService)
    }

    @Provides
    @PhotoScope
    @Named("photoPagerActivity")
    fun providePhotoPagerPresenter(photoService: PhotoService): PhotoListPresenter<PhotoListView> {
        return PhotoListPresenterImpl(photoService)
    }

    @Provides
    @PhotoScope
    fun providePhotoSearchPresenter(photoService: PhotoService): PhotoSearchPresenter<PhotoListView> {
        return PhotoSearchPresenterImpl(photoService)
    }

    @Provides
    @PhotoScope
    fun providePhotoPresenter(photoService: PhotoService): PhotoPresenter {
        return ConcurrencyPhotoPresenterImpl(photoService)
    }
}