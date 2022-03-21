package com.chocowind.app.arithmeticaverage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payer")
data class Payer(
    @ColumnInfo(name = "name") var name: String,
) {
    @PrimaryKey(autoGenerate = true) var uuid: Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Payer

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}