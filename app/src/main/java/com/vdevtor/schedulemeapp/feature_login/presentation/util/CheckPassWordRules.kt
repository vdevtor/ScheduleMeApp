package com.vdevtor.schedulemeapp.feature_login.presentation.util

import java.util.regex.Pattern


fun String.isPassWordStrongEnough(): Boolean {
    val passwordREGEX = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=])(?=\\S+$).{8,}$"
    )
    return passwordREGEX.matcher(this).matches()
}