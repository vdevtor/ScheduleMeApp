package com.vdevtor.schedulemeapp.feature_login.presentation.util

fun String.validateName() : Boolean = this.isNotBlank()

fun String.validatePhone() : Boolean = this.length >= 13
