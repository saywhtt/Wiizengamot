package edu.born.flicility.di.components

import dagger.Subcomponent
import edu.born.flicility.di.modules.PhotoModule
import edu.born.flicility.di.scopes.PhotoScope
import edu.born.flicility.fragments.PhotoGalleryFragment

@PhotoScope
@Subcomponent(modules = [PhotoModule::class])
interface PhotoComponent {

    @Subcomponent.Builder
    interface Builder {
        fun photoModule(photoModule: PhotoModule): Builder
        fun build(): PhotoComponent
    }

    fun inject(photoGalleryFragment: PhotoGalleryFragment)
}