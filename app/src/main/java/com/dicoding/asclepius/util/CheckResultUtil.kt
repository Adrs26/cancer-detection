package com.dicoding.asclepius.util

import android.net.Uri
import com.dicoding.asclepius.ml.CancerClassification

object CheckResultUtil {
    var imageUri: Uri? = null
    var outputCancerClassification: CancerClassification.Outputs? = null
    var isNewData: Boolean = true
}