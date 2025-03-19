package com.example.datn.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AlertDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {
    fun showDialog(
        context: Context,
        message: String,
        positiveText: String = "OK",
        positiveAction: (() -> Unit)? = null,
        negativeText: String? = null,
        negativeAction: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setPositiveButton(positiveText) { dialogInterface, _ ->
                positiveAction?.invoke()
                dialogInterface.dismiss()
            }

        if (!negativeText.isNullOrEmpty()) {
            builder.setNegativeButton(negativeText) { dialogInterface, _ ->
                negativeAction?.invoke()
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }
    fun formatDate(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatTime(): String {
        val date = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(date)
    }
    fun resizeImageFile(file: File, maxSize: Int = 4096, maxFileSize: Int = 2 * 1024 * 1024): File {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val rotatedBitmap = fixImageRotation(file, bitmap)
        var resizedBitmap = resizeBitmap(rotatedBitmap, maxSize)

        val resizedFile = File(file.parent, "resized_${file.name}")
        var quality = 90 // Bắt đầu với chất lượng nén 90%

        do {
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val byteArray = outputStream.toByteArray()
            outputStream.close()

            if (byteArray.size <= maxFileSize) {
                FileOutputStream(resizedFile).use { it.write(byteArray) }
                return resizedFile
            }

            // Nếu kích thước file vẫn lớn, giảm chất lượng nén
            quality -= 10
        } while (quality > 10) // Tránh giảm chất lượng quá thấp gây mất hình ảnh

        return resizedFile
    }


    // Hàm resize ảnh (giữ tỉ lệ gốc)
    fun resizeBitmap(image: Bitmap, maxSize: Int): Bitmap {
        val width = image.width
        val height = image.height
        val ratio: Float = width.toFloat() / height.toFloat()

        val newWidth: Int
        val newHeight: Int
        if (ratio > 1) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true)
    }
    // Hàm kiểm tra và sửa hướng xoay ảnh
    fun fixImageRotation(file: File, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap // Không cần xoay
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}