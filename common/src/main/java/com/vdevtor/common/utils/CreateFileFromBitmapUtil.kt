package com.vdevtor.common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.vdevtor.common.core.Constants.PROFILE_PIC_FOLDER
import com.vdevtor.common.core.Constants.PROFILE_PIC_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun saveProfilePictureInternally(context: Context, bitmap: Bitmap,uid : String): Boolean {

    val myDir = File(context.filesDir, PROFILE_PIC_FOLDER)
    myDir.mkdirs()
    val imgFile = File(myDir, "${PROFILE_PIC_NAME}_$uid.jpg")
    if (imgFile.exists()) {
        imgFile.delete()
        imgFile.createNewFile()
    }
    val outputStream = FileOutputStream(imgFile)
    Log.d("output", "saveProfilePictureInternally: ${imgFile.absolutePath}")

    return try {
        context.openFileOutput("profilePicture.jpg", MODE_PRIVATE).use {
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                throw IOException("Couldn't save bitmap.")
            }
        }
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

}

@RequiresApi(Build.VERSION_CODES.P)
fun convertUriToBimap(selectedPhotoUri: Uri, context: Context): Bitmap {
    return when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(context.contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}

