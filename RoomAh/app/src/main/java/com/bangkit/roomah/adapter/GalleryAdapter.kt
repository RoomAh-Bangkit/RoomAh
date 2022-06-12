package com.bangkit.roomah.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.roomah.databinding.CardGalleryBinding

class GalleryAdapter(private val paths: ArrayList<String>)
    : RecyclerView.Adapter<GalleryAdapter.GridViewHolder>() {
    inner class GridViewHolder(
        private val binding: CardGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: String) {
            binding.ivGalleryCard.setImageBitmap(BitmapFactory.decodeFile(path))
            itemView.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            CardGalleryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        with (holder) {
            bind(paths[position])
        }
    }

    override fun getItemCount(): Int = paths.size
}