package com.dicoding.asclepius.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.Check

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCheck(check: Check)

    @Query("SELECT * FROM `check`")
    suspend fun getAllCheck(): List<Check>
}