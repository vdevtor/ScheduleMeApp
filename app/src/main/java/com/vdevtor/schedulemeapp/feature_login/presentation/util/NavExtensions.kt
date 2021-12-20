package com.vdevtor.schedulemeapp.feature_login.presentation.util

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.vdevtor.schedulemeapp.R

private val navOptionsPopUp = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    //.setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .setPopUpTo(R.id.nav_host_login,true)
    .build()

private val navOptionsKeepStack = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    //.setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateWithAnimationsPopUp(destinationId: Int){
    this.navigate(destinationId,null, navOptionsPopUp)
}

fun NavController.navigateWithAnimationsKeepStack(destinationId: Int){
    this.navigate(destinationId,null, navOptionsKeepStack)
}