package com.example.tripmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tripmate.data.model.ExpenseEntity
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.model.ActivityEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase



@Database(entities = [UserEntity::class, TripEntity::class, ExpenseEntity::class, ActivityEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Connects the Database to the Queries
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun activityDao(): ActivityDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Defining the migration object
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_table ADD COLUMN email TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS activity_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tripId INTEGER NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                title TEXT NOT NULL,
                notes TEXT NOT NULL
            )
        """.trimIndent())
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

