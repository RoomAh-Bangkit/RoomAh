package com.bangkit.roomah.ui.detail

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.FileProvider
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.ActivityDetailBinding
import java.io.File

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        val path = intent.getStringExtra(EXTRA_GALLERY_ITEM)
        val result = intent.getStringExtra(EXTRA_TITLE)

        binding.apply {
            toolbarTitle.text = resources.getString(R.string.detail_title, result)
            ivGalleryDetail.setImageBitmap(BitmapFactory.decodeFile(path))

            btnShare.setOnClickListener {
                if (path != null)
                    shareImage(path)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun shareImage(path: String) {
        val shareIntent = Intent().apply {
            val uri = FileProvider.getUriForFile(
                this@DetailActivity,
                "com.bangkit.roomah",
                File(path)
            )

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.share_via)))
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    companion object {
        const val EXTRA_GALLERY_ITEM = "extra_gallery_item"
        const val EXTRA_TITLE = "extra_title"
    }
}