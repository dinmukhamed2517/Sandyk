package kz.sdk.sandykprot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudentEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao



    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }


}