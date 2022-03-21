package com.chocowind.app.arithmeticaverage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chocowind.app.arithmeticaverage.model.PayData
import com.chocowind.app.arithmeticaverage.model.Payer

@Database(entities = [PayData::class, Payer::class], version = 1)
abstract class PayDatabase : RoomDatabase() {
    abstract fun payDao(): PayDao

    companion object {
        private var INSTANCE: PayDatabase? = null

        fun getDatabase(context: Context): PayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        PayDatabase::class.java,
                        "PayDatabase"
                    ).allowMainThreadQueries()
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}