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
import com.example.tripmate.data.model.DocumentEntity
import com.example.tripmate.data.model.NotificationEntity
import com.example.tripmate.data.model.PollEntity
import com.example.tripmate.data.model.TripParticipantEntity


@Database(entities =
    [UserEntity::class,
        TripEntity::class,
        ExpenseEntity::class,
        ActivityEntity::class,
        TripParticipantEntity::class,
        DocumentEntity::class,
        PollEntity::class,
        NotificationEntity::class
    ],
    version = 22, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    // Connects the Database to the Queries
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun activityDao(): ActivityDao
    abstract fun tripParticipantDao(): TripParticipantDao
    abstract fun documentDao(): DocumentDao
    abstract fun pollDao(): PollDao
    abstract fun notificationDao(): NotificationDao


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

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE expense_table ADD COLUMN date TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE expense_table ADD COLUMN notes TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE expense_table ADD COLUMN category TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE expense_table ADD COLUMN paidBy TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS trip_participant_table (
                tripId INTEGER NOT NULL,
                userId INTEGER NOT NULL,
                PRIMARY KEY(tripId, userId)
            )
        """.trimIndent())
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS document_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tripId INTEGER NOT NULL,
                fileName TEXT NOT NULL,
                fileUri TEXT NOT NULL,
                uploadedAt INTEGER NOT NULL,
                FOREIGN KEY(tripId) REFERENCES trip_table(id) ON DELETE CASCADE
            )
        """.trimIndent())

                database.execSQL("""
            CREATE INDEX index_document_table_tripId
            ON document_table(tripId)
        """.trimIndent())
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            CREATE TABLE IF NOT EXISTS poll_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tripId INTEGER NOT NULL,
                question TEXT NOT NULL,
                option1 TEXT NOT NULL,
                option2 TEXT NOT NULL,
                option3 TEXT,
                option4 TEXT,
                createdAt INTEGER NOT NULL,
                FOREIGN KEY(tripId) REFERENCES trip_table(id) ON DELETE CASCADE
            )
        """.trimIndent())

                database.execSQL("""
            CREATE INDEX index_poll_table_tripId
            ON poll_table(tripId)
        """.trimIndent())
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            CREATE TABLE IF NOT EXISTS notification_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tripId INTEGER NOT NULL,
                message TEXT NOT NULL,
                type TEXT NOT NULL,
                status TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                FOREIGN KEY(tripId) REFERENCES trip_table(id) ON DELETE CASCADE
            )
        """.trimIndent())

                database.execSQL("""
            CREATE INDEX index_notification_table_tripId
            ON notification_table(tripId)
        """.trimIndent())
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN bio TEXT NOT NULL DEFAULT ''
        """.trimIndent())

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN profileImageUri TEXT
        """.trimIndent())
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE trip_table 
            ADD COLUMN country TEXT NOT NULL DEFAULT ''
        """.trimIndent())

            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0
        """.trimIndent())

            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN gender TEXT NOT NULL DEFAULT ''
        """.trimIndent())

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN age INTEGER NOT NULL DEFAULT 0
        """.trimIndent())

                database.execSQL("""
            ALTER TABLE user_table 
            ADD COLUMN region TEXT NOT NULL DEFAULT ''
        """.trimIndent())
            }
        }

        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE user_table
            ADD COLUMN notificationsEnabled INTEGER NOT NULL DEFAULT 1
        """)

                database.execSQL("""
            ALTER TABLE user_table
            ADD COLUMN privacyStatus TEXT NOT NULL DEFAULT 'Public'
        """)
            }
        }

        val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            ALTER TABLE trip_table
            ADD COLUMN userId INTEGER NOT NULL DEFAULT 0
        """.trimIndent())
            }
        }

        val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // 1. Create new table with correct schema
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS trip_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                userId TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                date TEXT NOT NULL,
                country TEXT NOT NULL
            )
        """.trimIndent())

                database.execSQL("""
            INSERT INTO trip_table_new (id, userId, name, description, date, country)
            SELECT id, userId, name, description, date, country
            FROM trip_table
        """.trimIndent())

                database.execSQL("DROP TABLE trip_table")

                database.execSQL("ALTER TABLE trip_table_new RENAME TO trip_table")
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
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13,
                        MIGRATION_13_14,
                        MIGRATION_14_15
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

