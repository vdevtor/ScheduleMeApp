package com.vdevtor.schedulemeapp.feature_login.presentation.util

import android.text.TextUtils
import android.util.Patterns

fun String.isEmailValid() : Boolean{
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}