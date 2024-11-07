package com.dicoding.asclepius.adapter

import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.entity.Check
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<Check, HistoryAdapter.ItemViewHolder>(
    object : DiffUtil.ItemCallback<Check>() {
        override fun areItemsTheSame(oldItem: Check, newItem: Check): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Check, newItem: Check): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        private val itemBinding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Check) {
            Glide.with(itemView)
                .load(Uri.parse(data.imageUri))
                .centerCrop()
                .into(itemBinding.ivCover)

            itemBinding.apply {
                tvResult.text = data.classification
                tvScore.text = data.confidenceScore
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tvDate.text = data.checkDate
                }
            }
        }
    }
}