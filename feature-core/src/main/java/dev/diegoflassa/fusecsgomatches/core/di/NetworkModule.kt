package dev.diegoflassa.fusecsgomatches.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fusecsgomatches.core.data.adapters.InstantJsonAdapter
import dev.diegoflassa.fusecsgomatches.core.data.adapters.MatchStatusJsonAdapter
import dev.diegoflassa.fusecsgomatches.core.data.adapters.UriJsonAdapter
import dev.diegoflassa.fusecsgomatches.core.data.config.IConfig
import dev.diegoflassa.fusecsgomatches.core.data.network.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(config: IConfig): AuthInterceptor {
        // In a real app, you'd fetch this token securely.
        // For this example, ensure you have a way to provide the actual token.
        // If your token is in pandascore.properties, you'd load it here.
        val token = config.pandascoreKey // Replace with actual token retrieval logic
        return AuthInterceptor(token)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(config: IConfig, okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(UriJsonAdapter())
            .add(InstantJsonAdapter())
            .add(MatchStatusJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(config.pandascoreApi)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
