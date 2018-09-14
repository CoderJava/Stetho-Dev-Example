package com.ysn.stethodev.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ysn.stethodev.database.daos.WaktuDao
import com.ysn.stethodev.database.entities.WaktuData

@Database(entities = [(WaktuData::class)], version = 1)
abstract class WaktuDb : RoomDatabase() {

    abstract fun waktuDao(): WaktuDao

    companion object {
        private var INSTANCE: WaktuDb? = null

        fun getInstance(context: Context) : WaktuDb? {
            if (INSTANCE == null) {
                synchronized(WaktuDb::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, WaktuDb::class.java, "waktu.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}