package com.example.mini_clip

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mini_clip.adapter.ProfileVideoAdapter
import com.example.mini_clip.databinding.FragmentBookmarkBinding
import com.example.mini_clip.model.VideoModel
import com.example.mini_clip.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var profileUserId: String
    private lateinit var currentUserId : String
    private lateinit var adapter: ProfileVideoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookmarkBinding.bind(view)

        profileUserId = arguments?.getString("profile_user_id") ?: ""
        currentUserId = arguments?.getString("current_user_id") ?: ""
        if(profileUserId == currentUserId){
            fetchBookmarkedVideos()
        }else{
            binding.message.visibility = View.VISIBLE
        }

    }

    private fun fetchBookmarkedVideos() {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(profileUserId)
        userRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val bookmarkList = document.get("bookMarkList") as? List<String>
                if (bookmarkList != null && bookmarkList.isNotEmpty()) {
                    setupRecyclerView(bookmarkList)
                } else {
                    context?.let { UiUtil.showToast(it, "Bookmarked videos not found") }
                }
            } else {
                context?.let { UiUtil.showToast(it, "User not found") }
            }
        }.addOnFailureListener { exception ->
            context?.let { UiUtil.showToast(it, "Failed to fetch bookmarks: ${exception.message}") }
        }
    }

    private fun setupRecyclerView(bookmarkList: List<String>) {
        val query = FirebaseFirestore.getInstance()
            .collection("videos")
            .whereIn("videoId", bookmarkList)

        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(query, VideoModel::class.java)
            .build()

        adapter = ProfileVideoAdapter(options)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter.startListening()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        if(::adapter.isInitialized){
            adapter.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            fetchBookmarkedVideos()
        }, 2000)

    }

}
