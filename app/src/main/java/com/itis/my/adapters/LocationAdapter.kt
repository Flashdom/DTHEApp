package com.itis.my.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.my.R
import com.itis.my.databinding.ItemLocationBinding
import com.itis.my.model.Location
import java.time.Instant
import java.time.ZoneId

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val differ = AsyncListDiffer(this, LocationItemCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateList(newList: List<Location>) {
        differ.submitList(newList)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Location) {
            val zdt = Instant.ofEpochMilli(item.createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.tvLocationTitle.text =
                itemView.context.getString(R.string.location_title, adapterPosition + 1)
            binding.tvCreatedAt.text = itemView.context.getString(
                R.string.creation_date,
                "${zdt.dayOfMonth}.${zdt.month.value}.${zdt.year}  ${zdt.hour}:${zdt.minute}:${zdt.second}"
            )
            binding.tvLocationText.text = item.text
        }
    }

    class LocationItemCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }

}