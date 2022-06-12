package com.bangkit.roomah.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.roomah.databinding.CardGalleryBinding
import com.bangkit.roomah.ui.detail.DetailActivity

class GalleryAdapter(private val paths: ArrayList<String>, private val result: String)
    : RecyclerView.Adapter<GalleryAdapter.GridViewHolder>() {
    inner class GridViewHolder(
        private val binding: CardGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: String) {
            binding.ivGalleryCard.setImageBitmap(BitmapFactory.decodeFile(path))

            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_GALLERY_ITEM, path)
                intentDetail.putExtra(DetailActivity.EXTRA_TITLE, result)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivGalleryCard, "image")
                    )
                itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
            }
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