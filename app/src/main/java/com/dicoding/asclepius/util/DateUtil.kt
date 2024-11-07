package com.dicoding.asclepius.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateToEnglishFormat(dateString: String = LocalDate.now().toString()): String {
        val date = LocalDate.parse(
            dateString,
            DateTimeFormatter.ofPattern("yyyy-[M][MM]-[d][dd]")
        )

        val englishDateFormat = DateTimeFormatter.ofPattern(
            "dd MMMM yyyy",
            Locale.ENGLISH
        )
        return date.format(englishDateFormat)
    }
}