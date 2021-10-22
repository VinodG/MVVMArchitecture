package com.example.mvvmarchitecture.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.MyDialogBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CommonDialog @Inject constructor(@ActivityContext var context: Context) {
    var binding: MyDialogBinding
    private var _dialog: AlertDialog.Builder = AlertDialog.Builder(context)
    private var dialog = _dialog.create()

    init {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_dialog, null, false)
    }

    fun body(
        msg: String,
        header: String = "",
        posCallBack: (() -> Unit)? = null,
        negCallBack: (() -> Unit)? = null
    ): CommonDialog {
        if (header.isEmpty()) {
            binding.apply {
                tvHeader.visibility = if(header.isEmpty())  View.GONE else View.VISIBLE
                tvDone.visibility = if (posCallBack == null) View.GONE else View.VISIBLE
                tvCancel.visibility = if (negCallBack == null) View.GONE else View.VISIBLE
                tvMsg.text = msg
                tvHeader.text = header?:""
                tvHeader.setOnClickListener {
                    negCallBack?.invoke()
                }
                tvDone.setOnClickListener {
                    posCallBack?.invoke()
                }
                tvCancel.setOnClickListener {
                    negCallBack?.invoke()
                }
            }
        }
        return this
    }

    fun show() {
        dismiss()
        dialog =_dialog.create()
        dialog.setView(binding.root)
        dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}