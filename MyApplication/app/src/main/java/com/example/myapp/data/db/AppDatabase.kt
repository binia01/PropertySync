package com.example.myapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.local.UserDao
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.User

@Database(
    entities = [User::class, Property::class], // <-- List all your @Entity classes here
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun propertyDao(): PropertyDAO
}
