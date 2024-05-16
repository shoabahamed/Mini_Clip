package com.example.mini_clip.util

import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import android.content.Context

object UiUtil {
    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}