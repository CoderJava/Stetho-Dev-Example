package com.ysn.stethodev.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class WaktuData(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "waktu") var waktu: String
)