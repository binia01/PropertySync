package com.example.propSync.di

import com.example.myapp.data.api.AppointmentService
import com.example.myapp.data.api.AuthApiService
import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.repository.AppointmentRepository
import com.example.myapp.data.repository.AppointmentRepositoryImpl
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.data.repository.AuthRepositoryImpl
import com.example.myapp.data.repository.PropertyRepoImpl
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import com.example.myapp.data.repository.UserRepositoryImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindPropertyRepository(propertyRepository: PropertyRepoImpl): PropertyRepository

    @Binds
    abstract fun bindAppointmentRepository(appointmentRepository: AppointmentRepositoryImpl): AppointmentRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthApiService(): AuthApiService{
            return Retrofit.Builder()
                .baseUrl("http://192.168.8.7:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideUserApiService(): UserService{
            return Retrofit.Builder()
                .baseUrl("http://192.168.8.7:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserService::class.java)
        }

        @Provides
        @Singleton
        fun providePropertyApiService(): PropertyService{
            return Retrofit.Builder()
                .baseUrl("http://192.168.8.7:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PropertyService::class.java)
        }

        @Provides
        @Singleton
        fun provideAppointmentApiService(): AppointmentService{
            return Retrofit.Builder()
                .baseUrl("http://192.168.8.7:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppointmentService::class.java)
        }

        @Provides
        @Singleton  //  Only one instance of Gson will be created.
        fun provideGson(): Gson {
            return Gson()
        }

    }
}
