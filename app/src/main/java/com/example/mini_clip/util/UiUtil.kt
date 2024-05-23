package com.example.mini_clip.util

import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import android.content.Context
import com.example.mini_clip.model.UserModel
import com.example.mini_clip.model.VideoModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UiUtil {
    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    fun updateVideoData(model: VideoModel) {
        Firebase.firestore.collection("videos")
            .document(model.videoId)
            .set(model)
    }
    fun updateUserData(model: UserModel) {
        Firebase.firestore.collection("users")
            .document(model.id)
            .set(model)
    }
}