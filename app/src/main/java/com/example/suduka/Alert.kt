package com.example.suduka

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class Alert(
    val n : Int,
    val answer : (a: Int) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        println(n)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("choose number")
                .setItems(
                    Array(n) { (it + 1).toString()},
                    DialogInterface.OnClickListener { _, which ->
                        answer(which + 1)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}