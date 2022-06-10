package com.bangkit.roomah.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object FileHandler {

    private const val FILENAME_FORMAT = "yy-MM-dd-HH-mm-ss"
    private const val SUFFIX = ".jpg"

    private val timeStamp: String =
        SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis())

//    fun uriToFile(selectedImg: Uri, context: Context): File {
//        val contentResolver: ContentResolver = context.contentResolver
//        val myFile = createCustomTempFile(context)
//
//        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
//        val outputStream: OutputStream = FileOutputStream(myFile)
//        val buf = ByteArray(1024)
//
//        var len: Int
//        while (inputStream.read(buf).also { len = it } > 0)
//            outputStream.write(buf, 0, len)
//
//        outputStream.close()
//        inputStream.close()
//
//        return myFile
//    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)

            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size

            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}