package com.example.myapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.local.UserDao
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.User

@Database(
    entities = [User::class, Property::class, AppointmentEntity::class], // <-- List all your @Entity classes here
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun propertyDao(): PropertyDAO
    abstract fun appointmentDao(): AppointmentDao
}
