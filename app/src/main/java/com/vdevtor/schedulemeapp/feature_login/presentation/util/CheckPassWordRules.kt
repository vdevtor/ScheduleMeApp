package com.vdevtor.schedulemeapp.feature_login.presentation.util

import java.util.regex.Pattern


fun String.isPassWordStrongEnough(): Boolean {
    val passwordREGEX = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=])(?=\\S+$).{6,}$"
    )
    return passwordREGEX.matcher(this).matches()
}

fun String.isPasswordEquals(string: String): Boolean{
    return this == string && this.isNotBlank()
}

fun CharSequence.hasOneSpecialChar() : Boolean{
    val passwordREGEX = Pattern.compile(
        "^(?=.*[@#!$%^&+=]).+$"
    )
    return passwordREGEX.matcher(this).matches()
}

fun CharSequence.hasOneDigit() : Boolean{
    val passwordREGEX = Pattern.compile(
        "^(?=.*[0-9]).+$"
    )

    return passwordREGEX.matcher(this).matches()
}

fun CharSequence.hasOneUpLetter() : Boolean{
    val passwordREGEX = Pattern.compile(
        "^(?=.*[A-Z]).+$"
    )
    return passwordREGEX.matcher(this).matches()
}

fun CharSequence.hasSixChar() : Boolean{
    var length: Int
    this.let {
        length = it.length
    }
   return length >= 6
}