package com.vdevtor.schedulemeapp.presentation.feature_login.util

import java.util.regex.Pattern


fun isPassWordStrongEnough(password: String): Boolean {
    val passwordREGEX = Pattern.compile(
        "ˆ" +
                ".{8,}" +
                "(?=.*[@#$%^&+=])" +
                "(?=.*[A-Z])" +
                "(?=\\S+$)" +
                "$"
    )
    return passwordREGEX.matcher(password).matches()
}