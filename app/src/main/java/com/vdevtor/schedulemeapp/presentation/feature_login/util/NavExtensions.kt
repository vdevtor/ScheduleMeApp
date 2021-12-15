package com.vdevtor.schedulemeapp.presentation.feature_login.util

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.vdevtor.schedulemeapp.R

private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimations(destinationId: Int){
    this.navigate(destinationId,null, navOptions)
}