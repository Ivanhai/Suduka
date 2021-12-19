package com.example.suduka

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class Alert(
    val answer : (a: Int) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("choose number")
                .setItems(
                    arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9"),
                    DialogInterface.OnClickListener { _, which ->
                        answer(which + 1)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}