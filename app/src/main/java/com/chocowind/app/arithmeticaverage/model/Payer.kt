package com.chocowind.app.arithmeticaverage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payer")
data class Payer(
    @ColumnInfo(name = "name") var name: String,
) {
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0
}