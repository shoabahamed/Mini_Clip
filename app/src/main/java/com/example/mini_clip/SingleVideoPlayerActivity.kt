package com.example.mini_clip

import VideoListAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mini_clip.databinding.ActivitySingleVideoPlayerBinding
import com.example.mini_clip.model.VideoModel
import com.example.mini_clip.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SingleVideoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivitySingleVideoPlayerBinding
    lateinit var videoId: String
    lateinit var adapter: VideoListAdapter
    var showDeleteButton:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySingleVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId=intent.getStringExtra("videoId")!!
        showDeleteButton = intent.getStringExtra("showDeleteButton").toBoolean()
        setupViewPager()

        }

    fun setupViewPager()
    {
        val options=FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("videoId",videoId),
                VideoModel::class.java
            ).build()
        adapter=VideoListAdapter(options, showDeleteButton)
        binding.videPager.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.updateAllChanges()
        adapter.startListening()
    }


}