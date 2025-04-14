package com.example.datn.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.datn.R
import com.example.datn.databinding.CustomSnackbarBinding
import com.example.datn.models.message.Message
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {
//    172.20.10.4
//    192.168.52.52
    val url = "http://192.168.1.101:3000"
    @SuppressLint("RestrictedApi")
    fun showCustomSnackbar(
        view: View,
        message: Message,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, "", duration)

        val inflater = LayoutInflater.from(view.context)
        val binding: CustomSnackbarBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_snackbar, null, false)

        binding.tvMessage.text = message.message
        binding.tvName.text = message.senderName
        Glide.with(view).load(url+message.senderImage).into(binding.imgAvatar)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Xóa padding mặc định
        snackbarLayout.addView(binding.root, 0)

        binding.llMessage.setOnClickListener {
            action?.invoke()
            snackbar.dismiss()
        }
        snackbar.show()
    }
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

    fun playTingTingSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.tingting)
        mediaPlayer.start()
    }

    fun formatTime(): String {
        val date = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(date)
    }
    fun formatTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date ?: Date())
    }
    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        return (calendar.get(Calendar.MONTH) + 1).toString() // Lưu ý: Tháng bắt đầu từ 0, nên +1
    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR).toString()
    }
    fun resizeImageFile(file: File, maxSize: Int = 4096, maxFileSize: Int = 2 * 1024 * 1024): File {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val rotatedBitmap = fixImageRotation(file, bitmap)
        var resizedBitmap = resizeBitmap(rotatedBitmap, maxSize)

        val resizedFile = File(file.parent, "resized_${file.name}")
        var quality = 80 // Bắt đầu với chất lượng nén 90%

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