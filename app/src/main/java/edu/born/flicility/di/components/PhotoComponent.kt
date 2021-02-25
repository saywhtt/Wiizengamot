package edu.born.flicility.di.components

import dagger.Subcomponent
import edu.born.flicility.di.modules.PhotoModule
import edu.born.flicility.di.scopes.PhotoScope
import edu.born.flicility.fragments.PhotoListFragment
import edu.born.flicility.fragments.PhotoPagerFragment
import edu.born.flicility.fragments.PhotoSearchFragment

@PhotoScope
@Subcomponent(modules = [PhotoModule::class])
interface PhotoComponent {

    @Subcomponent.Builder
    interface Builder {
        fun photoModule(photoModule: PhotoModule): Builder
        fun build(): PhotoComponent
    }

    fun inject(photoListFragment: PhotoListFragment)
    fun inject(photoSearchFragment: PhotoSearchFragment)
    fun inject(photoPagerFragment: PhotoPagerFragment)
}