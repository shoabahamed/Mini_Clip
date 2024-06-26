package com.example.mini_clip.model

import com.google.firebase.Timestamp

data class VideoModel(
    var videoId: String = "",
    var title: String = "",
    var url: String = "",
    var uploaderId: String = "",
    var createdTime: Timestamp = Timestamp.now(),
    var likeList : MutableList<String> = mutableListOf(),
    var dislikeList : MutableList<String> = mutableListOf(),
)
