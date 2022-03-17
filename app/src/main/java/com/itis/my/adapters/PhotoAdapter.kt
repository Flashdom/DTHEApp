package com.itis.my.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.my.R
import com.itis.my.databinding.ItemPhotoBinding
import com.itis.my.model.Photo
import java.time.Instant
import java.time.ZoneId

class PhotoAdapter(private val onPhotoClick: (uri: String) -> Unit) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val differ = AsyncListDiffer(this, PhotoItemCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateList(newList: List<Photo>) {
        differ.submitList(newList)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(differ.currentList[position], onPhotoClick)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Photo, onPhotoClick: (uri: String) -> Unit) {
            val zdt = Instant.ofEpochMilli(item.createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.ivPhoto.apply {
                setImageURI(item.uriImage)
                binding.tvCreatedAt.text = itemView.context.getString(
                    R.string.creation_date,
                    "${zdt.dayOfMonth}.${zdt.month.value}.${zdt.year}  ${zdt.hour}:${zdt.minute}:${zdt.second}"
                )
                setOnClickListener {
                    onPhotoClick(item.uriImage.toString())
                }
            }
        }
    }

    class PhotoItemCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }


}