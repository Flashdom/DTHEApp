package com.itis.my.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.my.R
import com.itis.my.databinding.ItemVideoBinding
import com.itis.my.model.Video
import java.time.Instant
import java.time.ZoneId

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val differ = AsyncListDiffer(this, VideoItemCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateList(newList: List<Video>) {
        differ.submitList(newList)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video) {
            val zdt = Instant.ofEpochMilli(item.createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.tvCreatedAt.text = itemView.context.getString(
                R.string.creation_date,
                "${zdt.dayOfMonth}.${zdt.month.value}.${zdt.year}  ${zdt.hour}:${zdt.minute}:${zdt.second}"
            )
            binding.vwVideo.setVideoURI(item.videoUriString.toUri())
            binding.vwVideo.setOnClickListener {
                binding.vwVideo.start()
            }

        }
    }

    class VideoItemCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }


}