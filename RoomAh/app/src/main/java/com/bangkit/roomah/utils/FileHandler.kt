package com.bangkit.roomah.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.bangkit.roomah.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object FileHandler {

    private const val FILENAME_FORMAT = "yy-MM-dd-HH-mm-ss"
    private const val SUFFIX = ".jpg"

    private val timeStamp: String =
        SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, SUFFIX, storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)

        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0)
            outputStream.write(buf, 0, len)

        outputStream.close()
        inputStream.close()

        return myFile
    }

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

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()

        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

    fun initFolders(application: Application) {
        listOf("Bathroom", "Bedroom", "Dining Room", "Kitchen", "Living Room").forEach() { name ->
            application.externalMediaDirs.firstOrNull()?.let {
                File(it, application.resources.getString(R.string.folder_name, name)).apply { mkdirs() }
            }
        }
    }

    fun copyImage(application: Application, file: File, foldername: String) {
        application.externalMediaDirs.firstOrNull()?.let {
            file.copyTo(File(it, application.resources.getString(R.string.folder_name, foldername)), true)
        }
    }
}