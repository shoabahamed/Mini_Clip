package com.example.mini_clip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mini_clip.adapter.ProfileVideoAdapter
import com.example.mini_clip.databinding.FragmentPostBinding
import com.example.mini_clip.model.VideoModel
import com.example.mini_clip.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class PostFragment : Fragment(R.layout.fragment_post) {
    private lateinit var binding: FragmentPostBinding
    lateinit var adapter: ProfileVideoAdapter
    private lateinit var currentUserId : String
    private lateinit var profileUserId: String
    var showDeleteButton: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostBinding.bind(view)
        profileUserId = arguments?.getString("profile_user_id") ?: ""
        currentUserId = arguments?.getString("current_user_id") ?: ""


        val trimmedProfileUserId = profileUserId.trim()
        val trimmedCurrentUserId = currentUserId.trim()

        if (trimmedCurrentUserId == trimmedProfileUserId) {
            showDeleteButton = true
        }

        setupRecyclerView()

    }

    fun setupRecyclerView()
    {
        val options= FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("uploaderId", profileUserId)
                    .orderBy("createdTime", Query.Direction.DESCENDING),
                VideoModel::class.java
            ).build()



        adapter= ProfileVideoAdapter(options, showDeleteButton=showDeleteButton)
        binding.recyclerView.layoutManager= GridLayoutManager(context, 3)
        binding.recyclerView.adapter=adapter
    }

    override fun onStart()
    {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.startListening()
    }
}
