package com.example.tripmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tripmate.data.model.ExpenseEntity
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.model.UserEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase



@Database(entities = [UserEntity::class, TripEntity::class, ExpenseEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Connects the Database to the Queries
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Defining the migration object
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserEntity ADD COLUMN email TEXT")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Plug the migration
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

