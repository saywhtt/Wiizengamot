package edu.born.flicility.di.modules

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import edu.born.flicility.FlickrFetchr
import edu.born.flicility.network.API.BASE_URL
import edu.born.flicility.network.PhotoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideFlickrFetchr(): FlickrFetchr {
        return FlickrFetchr()
    }

    @Provides
    @Singleton
    fun provideMainHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson().newBuilder()
               // .registerTypeAdapter(PhotoWrapper::class.java, PhotoWrapperDeserializer())
                .create()
    }

    @Provides
    @Singleton
    fun provideOkHttClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providePhotoService(retrofit: Retrofit): PhotoService {
        return retrofit.create(PhotoService::class.java)
    }
}