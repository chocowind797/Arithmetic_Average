package com.chocowind.app.arithmeticaverage.db

import androidx.room.*
import com.chocowind.app.arithmeticaverage.model.PayData
import com.chocowind.app.arithmeticaverage.model.Payer

@Dao
interface PayDao {
    // 查詢單筆
    @Query("SELECT * FROM payer WHERE name = :name")
    fun getPayer(name: String): Payer

    @Query("SELECT * FROM payer WHERE uuid = :uuid")
    fun getPayer(uuid: Int): Payer

    @Query("SELECT * FROM payData WHERE id = :id")
    fun getPayData(id: Int): PayData

    @Query("SELECT * FROM payData WHERE payer = :payer")
    fun getPayDataByPayer(payer: Int): List<PayData>

    // 查詢所有 PayData
    @Query("SELECT * FROM payer")
    fun getAllPayers(): List<Payer>

    @Query("SELECT * FROM payData")
    fun getAllPayDatas(): List<PayData>

    // 新增(含多筆批次)
    @Insert
    fun insertPayers(vararg payer: Payer)

    @Insert
    fun insertPayDatas(vararg payData: PayData)

    // 修改
    @Update
    fun updatePayer(payer: Payer)

    @Update
    fun updatePayData(payData: PayData)

    // 刪除
    @Delete
    fun deletePayer(payer: Payer)

    @Delete
    fun deletePayData(payData: PayData)

    // 刪除
    @Query("DELETE FROM payer WHERE uuid = :uuid")
    fun deletePayer(uuid: Int)

    @Query("DELETE FROM payer")
    fun deleteAllPayer()

    @Query("DELETE FROM payData WHERE id = :id")
    fun deletePayData(id: Int)

    // 加總 price
    @Query("select sum(price) as total from payData")
    fun getTotal(): Int

    // 照人加總 price
    @Query("select sum(price) as total from payData where payer = :payer")
    fun getTotalByPayer(payer : Int): Int
}