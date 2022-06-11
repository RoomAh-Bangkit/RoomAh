package com.bangkit.roomah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.CardGalleryBinding
import com.bangkit.roomah.ui.home.FileSelectionListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class GalleryAdapter(private val listFiles: ArrayList<File>, private val listener: FileSelectionListener) : RecyclerView.Adapter<GalleryAdapter.GridViewHolder>() {
    inner class GridViewHolder(
        private val binding: CardGalleryBinding,
        private val listener: FileSelectionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(f: File) {
            val context = itemView.context

            Glide.with(context)
                .load(f)
                .apply(RequestOptions()
                    .placeholder(R.drawable.bg_shadow_gradient)
                    .centerCrop())
                .into(binding.ivGalleryCard)

            itemView.setOnClickListener { listener.onFileSelected(f) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            CardGalleryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        with (holder) {
            bind(listFiles[position])
        }
    }

    override fun getItemCount(): Int = listFiles.size
}