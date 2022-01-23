package com.ds.github_repo.base

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.ds.github_repo.R

open class BaseActivity : AppCompatActivity() {
    private var dialog: Dialog? = null

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
    }

    fun showProgress() {
        hideProgress()
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }



    fun hideProgress() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

}
