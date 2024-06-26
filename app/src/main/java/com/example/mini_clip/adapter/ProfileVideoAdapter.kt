package com.example.mini_clip.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mini_clip.PostFragment
import com.example.mini_clip.SingleVideoPlayerActivity
import com.example.mini_clip.databinding.ProfileVideoItemRowBinding
import com.example.mini_clip.model.VideoModel
import com.example.mini_clip.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ProfileVideoAdapter(options:FirestoreRecyclerOptions<VideoModel>,
                          private val showDeleteButton: Boolean = false)
    :FirestoreRecyclerAdapter<VideoModel, ProfileVideoAdapter.VideoViewHolder>(options)
{

    inner class VideoViewHolder(private val binding: ProfileVideoItemRowBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(video: VideoModel)
        {
            Glide.with(binding.thumbnailImageView.context)
                .load(video.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable both memory and disk caching
                .thumbnail(0.1f) // Load a smaller thumbnail first
                .apply(RequestOptions().centerCrop()) // Center crop transformation
                .into(binding.thumbnailImageView)


            binding.thumbnailImageView.setOnClickListener{
                val intent=Intent(binding.thumbnailImageView.context,SingleVideoPlayerActivity::class.java)
                intent.putExtra("videoId", video.videoId)
                intent.putExtra("showDeleteButton", showDeleteButton.toString())
                binding.thumbnailImageView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding=ProfileVideoItemRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return VideoViewHolder(binding)

    }

    override fun onBindViewHolder(holder: VideoViewHolder, Position: Int, model: VideoModel) {
        holder.bind(model)
    }


}
