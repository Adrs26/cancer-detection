package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.local.dao.CheckDao
import com.dicoding.asclepius.data.local.entity.Check

class CheckRepository(private val checkDao: CheckDao) {
    suspend fun insertCheck(check: Check) {
        checkDao.insertCheck(check)
    }

    suspend fun getAllCheck(): List<Check> {
        return checkDao.getAllCheck()
    }
}