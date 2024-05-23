package com.example.mini_clip

class VideoListAdapter {

    fun formatLikeCount(count: Long): String {
        return when {
            count < 1000 -> count.toString()
            count < 1000000 -> "${count / 1000}k"
            count < 1000000000 -> "${count / 1000000}M"
            else -> "${count / 1000000000}B"
        }
    }
}
