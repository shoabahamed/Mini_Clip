package com.example.mini_clip

import com.example.mini_clip.model.VideoModel
import org.junit.Assert.assertEquals
import org.junit.Test

class VideoListAdapterTest {

    @Test
    fun testFormatLikeCount() {
        val adapter = VideoListAdapter()

        // Test cases for counts less than 1000
        assertEquals("0", adapter.formatLikeCount(0))
        assertEquals("999", adapter.formatLikeCount(999))

        // Test cases for counts between 1000 and 999999
        assertEquals("1k", adapter.formatLikeCount(1000))
        assertEquals("999k", adapter.formatLikeCount(999999))

        // Test cases for counts between 1000000 and 999999999
        assertEquals("1M", adapter.formatLikeCount(1000000))
        assertEquals("999M", adapter.formatLikeCount(999999999))

        // Test cases for counts greater than or equal to 1000000000
        assertEquals("1B", adapter.formatLikeCount(1000000000))
        assertEquals("999B", adapter.formatLikeCount(999999999999))
    }
}
