package com.example.datn.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

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
}