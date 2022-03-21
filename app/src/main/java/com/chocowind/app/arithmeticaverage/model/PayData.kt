package com.chocowind.app.arithmeticaverage.model

import androidx.room.*

@Entity(
    tableName = "payData",
    foreignKeys = [ForeignKey(
        entity = Payer::class,
        parentColumns = arrayOf("uuid"),
        childColumns = arrayOf("payer"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PayData(
    @ColumnInfo(name = "payer") var payer: Int,
    @ColumnInfo(name = "item") var item: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "remark") var remark: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}