package com.vdevtor.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun verifyGalleryPermissions(context: Context): Array<String> {
    return if (isReadStoragePermissionAlreadyGranted(context) && isCameraPermissionAlreadyGranted(
            context
        )
    ) {
        arrayOf()
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }
}

fun isReadStoragePermissionAlreadyGranted(context: Context): Boolean {
    return (ContextCompat
        .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED)
}

fun isCameraPermissionAlreadyGranted(context: Context): Boolean {
    return (ContextCompat
        .checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED)
}
