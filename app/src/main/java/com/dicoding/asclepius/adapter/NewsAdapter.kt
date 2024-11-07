package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.model.Articles
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.util.DateUtil.convertDateToEnglishFormat

class NewsAdapter : ListAdapter<Articles, NewsAdapter.ItemViewHolder>(
    object : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        private val itemBinding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Articles) {
            Glide.with(itemView)
                .load(data.urlToImage)
                .centerCrop()
                .into(itemBinding.ivCover)

            itemBinding.apply {
                tvTitle.text = data.title
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tvDate.text = convertDateToEnglishFormat(data.publishedAt.substring(0, 10))
                }
                tvDescription.text = data.description
            }

            itemBinding.itemContainer.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    this.data = Uri.parse(data.url)
                }
                if (intent.resolveActivity(itemView.context.packageManager) != null) {
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}