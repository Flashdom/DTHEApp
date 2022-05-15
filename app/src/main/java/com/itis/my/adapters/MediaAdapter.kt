package com.itis.my.adapters

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.my.R
import com.itis.my.databinding.ItemAudioBinding
import com.itis.my.databinding.ItemPhotoBinding
import com.itis.my.databinding.ItemVideoBinding
import com.itis.my.model.Media
import com.itis.my.utils.getDateString

class MediaAdapter(private val onPhotoClick: (uri: String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PHOTO_ITEM_VIEW_TYPE = 123
        private const val VIDEO_ITEM_VIEW_TYPE = 456
        private const val AUDIO_ITEM_VIEW_TYPE = 125
    }

    private val differ = AsyncListDiffer(this, MediaItemCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PHOTO_ITEM_VIEW_TYPE -> {
                PhotoViewHolder(
                    ItemPhotoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            VIDEO_ITEM_VIEW_TYPE -> {
                VideoViewHolder(
                    ItemVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                AudioViewHolder(
                    ItemAudioBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is Media.Photo -> {
                PHOTO_ITEM_VIEW_TYPE
            }
            is Media.Video -> {
                VIDEO_ITEM_VIEW_TYPE
            }
            is Media.Audio -> {
                AUDIO_ITEM_VIEW_TYPE
            }
        }
    }

    fun updateList(newList: List<Media>) {
        differ.submitList(newList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(differ.currentList[position] as Media.Photo, onPhotoClick)
            }
            is VideoViewHolder -> {
                holder.bind(differ.currentList[position] as Media.Video)
            }
            is AudioViewHolder -> {
                holder.bind(differ.currentList[position] as Media.Audio)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media.Photo, onPhotoClick: (uri: String) -> Unit) {
            binding.ivPhoto.apply {
                setImageURI(item.uriImage)
                binding.tvCreatedAt.text = itemView.context.getString(
                    R.string.creation_date,
                    getDateString(item.createdAt)
                )
                setOnClickListener {
                    onPhotoClick(item.uriImage.toString())
                }
            }
        }
    }

    class MediaItemCallback : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Media.Video) {
            binding.tvCreatedAt.text = itemView.context.getString(
                R.string.creation_date,
                getDateString(item.createdAt)
            )
            binding.vwVideo.setVideoURI(item.videoUri)
            binding.vwVideo.setOnClickListener {
                binding.vwVideo.start()
            }

        }
    }

    class AudioViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Media.Audio) {
            binding.tvCreatedAt.text = getDateString(item.createdAt)
            binding.ivPlay.setOnClickListener {
                MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(itemView.context, item.uriAudio)
                    prepare()
                    start()
                    binding.ivPlay.isEnabled = false
                }.setOnCompletionListener {
                    binding.ivPlay.isEnabled = true
                }
            }

        }
    }
}