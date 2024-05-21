import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mini_clip.ProfileActivity
import com.example.mini_clip.R
import com.example.mini_clip.databinding.VideoItemRowBinding
import com.example.mini_clip.model.UserModel
import com.example.mini_clip.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VideoListAdapter(
    options: FirestoreRecyclerOptions<VideoModel>
) : FirestoreRecyclerAdapter<VideoModel, VideoListAdapter.VideoViewHolder>(options) {

    private lateinit var currentUserId: String
    private var currentUserModel: UserModel? = null
    private val videoUpdateMap = mutableMapOf<String, VideoModel>()
    private val userUpdateMap = mutableMapOf<String, UserModel>()

    init {
        // Fetch current user data once during adapter initialization
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        Firebase.firestore.collection("users")
            .document(currentUserId)
            .get().addOnSuccessListener { documentSnapshot ->
                currentUserModel = documentSnapshot?.toObject(UserModel::class.java)
                notifyDataSetChanged() // Notify the adapter that data has changed so it can re-bind views
            }
    }

    inner class VideoViewHolder(private val binding: VideoItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindVideo(videoModel: VideoModel) {
            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener { documentSnapshot ->
                    val userModel = documentSnapshot?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username

                        // Bind profile pic
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_profile)
                            )
                            .into(binding.profileIcon)

                        binding.userDetailLayout.setOnClickListener {
                            val intent = Intent(binding.userDetailLayout.context, ProfileActivity::class.java)
                            intent.putExtra("profile_user_id", id)
                            binding.userDetailLayout.context.startActivity(intent)
                        }
                    }
                }

            binding.bookmarkIcon.setOnClickListener {
                if (currentUserModel?.bookMarkList?.contains(videoModel.videoId) == true) {
                    currentUserModel?.bookMarkList?.remove(videoModel.videoId)
                    binding.bookmarkIcon.clearColorFilter()
                } else {
                    binding.bookmarkIcon.setColorFilter(
                        ContextCompat.getColor(binding.bookmarkIcon.context, R.color.my_primary),
                        PorterDuff.Mode.SRC_IN
                    )
                    currentUserModel?.bookMarkList?.add(videoModel.videoId)
                }
                currentUserModel?.let {
                    userUpdateMap[currentUserId] = it
                }
            }

            checkVideoDetailsStatus(binding, videoModel)

            binding.likeDetails.setOnClickListener {
                if (videoModel.likeList.contains(currentUserId)) {
                    // If user has already liked, remove like
                    videoModel.likeList.remove(currentUserId)
                    binding.likeIcon.clearColorFilter()
                } else {
                    // If user has not liked, add like and remove dislike
                    binding.likeIcon.setColorFilter(
                        ContextCompat.getColor(binding.likeIcon.context, R.color.my_primary),
                        PorterDuff.Mode.SRC_IN
                    )
                    binding.dislikeIcon.clearColorFilter()
                    videoModel.likeList.add(currentUserId)

                    // Remove dislike if user disliked previously
                    if (videoModel.dislikeList.contains(currentUserId)) {
                        videoModel.dislikeList.remove(currentUserId)
                        binding.dislikeIcon.clearColorFilter()
                    }
                }

                videoUpdateMap[videoModel.videoId] = videoModel
                setUI(binding, videoModel)
            }

            binding.dislikeDetails.setOnClickListener {
                if (videoModel.dislikeList.contains(currentUserId)) {
                    videoModel.dislikeList.remove(currentUserId)
                    binding.dislikeIcon.clearColorFilter()
                } else {
                    binding.dislikeIcon.setColorFilter(
                        ContextCompat.getColor(binding.dislikeIcon.context, R.color.my_primary),
                        PorterDuff.Mode.SRC_IN
                    )
                    binding.likeIcon.clearColorFilter()
                    videoModel.dislikeList.add(currentUserId)

                    // Remove like if user liked previously
                    if (videoModel.likeList.contains(currentUserId)) {
                        videoModel.likeList.remove(currentUserId)
                        binding.likeIcon.clearColorFilter()
                    }
                }

                videoUpdateMap[videoModel.videoId] = videoModel
                setUI(binding, videoModel)
            }

            setUI(binding, videoModel)

            binding.progressBar.visibility = View.VISIBLE
            // Bind video
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true
                }
                // Play pause
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                    } else {
                        start()
                        binding.pauseIcon.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        holder.bindVideo(model)
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val model = getItem(position)
            videoUpdateMap[model.videoId]?.let { updatedModel ->
                updateVideoData(updatedModel)
                videoUpdateMap.remove(model.videoId)
            }
            userUpdateMap[currentUserId]?.let { updatedUser ->
                updateUserData(updatedUser)
                userUpdateMap.remove(currentUserId)
            }
        }
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

    fun setUI(binding: VideoItemRowBinding, model: VideoModel) {
        binding.captionView.text = model.title
        binding.likeCount.text = formatLikeCount(model.likeList.size)
        binding.dislikeCount.text = formatLikeCount(model.dislikeList.size)

        // Check if the current user has liked the video
        if (model.likeList.contains(currentUserId)) {
            binding.likeIcon.setColorFilter(
                ContextCompat.getColor(binding.likeIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.likeIcon.clearColorFilter()
        }

        // Check if the current user has disliked the video
        if (model.dislikeList.contains(currentUserId)) {
            binding.dislikeIcon.setColorFilter(
                ContextCompat.getColor(binding.dislikeIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.dislikeIcon.clearColorFilter()
        }

        // Check if the current user has bookmarked the video
        if (currentUserModel?.bookMarkList?.contains(model.videoId) == true) {
            binding.bookmarkIcon.setColorFilter(
                ContextCompat.getColor(binding.bookmarkIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.bookmarkIcon.clearColorFilter()
        }
    }

    fun formatLikeCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 1000000 -> "${count / 1000}k"
            count < 1000000000 -> "${count / 1000000}M"
            else -> "${count / 1000000000}B"
        }
    }

    fun updateAllChanges() {
        videoUpdateMap.forEach { (_, videoModel) ->
            updateVideoData(videoModel)
        }
        videoUpdateMap.clear()

        userUpdateMap.forEach { (_, userModel) ->
            updateUserData(userModel)
        }
        userUpdateMap.clear()
    }

    fun checkVideoDetailsStatus(binding: VideoItemRowBinding, videoModel: VideoModel) {
        // Check if the current user has liked the video
        if (videoModel.likeList.contains(currentUserId)) {
            binding.likeIcon.setColorFilter(
                ContextCompat.getColor(binding.likeIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.likeIcon.clearColorFilter()
        }

        // Check if the current user has disliked the video
        if (videoModel.dislikeList.contains(currentUserId)) {
            binding.dislikeIcon.setColorFilter(
                ContextCompat.getColor(binding.dislikeIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.dislikeIcon.clearColorFilter()
        }

        // Check if the current user has bookmarked the video
        if (currentUserModel?.bookMarkList?.contains(videoModel.videoId) == true) {
            binding.bookmarkIcon.setColorFilter(
                ContextCompat.getColor(binding.bookmarkIcon.context, R.color.my_primary),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.bookmarkIcon.clearColorFilter()
        }
    }
}
