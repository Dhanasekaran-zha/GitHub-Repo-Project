package com.ds.github_repo.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

object ColorUtil {

    fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = context.getAssets().open("GitHubColors.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json=String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}