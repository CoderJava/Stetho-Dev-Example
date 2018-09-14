package com.ysn.stethodev.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ysn.stethodev.database.entities.WaktuData

@Dao
interface WaktuDao {

    @Query("SELECT * FROM waktudata")
    fun getAll(): List<WaktuData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waktuData: WaktuData)

}