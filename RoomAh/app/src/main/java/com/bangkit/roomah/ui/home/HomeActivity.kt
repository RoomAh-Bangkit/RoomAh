package com.bangkit.roomah.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.roomah.R
import com.bangkit.roomah.adapter.GalleryAdapter
import com.bangkit.roomah.databinding.ActivityHomeBinding
import com.bangkit.roomah.utils.animateVisibility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        val defaultRB = findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)
        fetchImages(defaultRB.text.toString())

        binding.radioGroup.setOnCheckedChangeListener { p0, _ ->
            val checkedRB = findViewById<RadioButton>(p0.checkedRadioButtonId)
            fetchImages(checkedRB.text.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Fetching image from local storage
     */
    private fun fetchImages(foldername: String) {
        setLoadingState(true)

        var path: String = application.filesDir.path
        application.externalMediaDirs.firstOrNull()?.let {
            path = File(it, resources.getString(R.string.folder_name, foldername)).path
        }

        lifecycleScope.launchWhenStarted {
            launch {
                homeViewModel.fetchAllData(path).collect { response ->
                    response.onSuccess {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.fetch_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        val paths = ArrayList<String>()
                        for (p in it) paths.add(p)
                        showRecyclerView(paths)

                        showEmptyImage(paths.isEmpty())
                        setLoadingState(false)
                    }

                    response.onFailure {
                        setLoadingState(false)
                        Log.d("On Failure Fetch Local Images", it.message ?: "null")

                        Snackbar.make(
                            binding.root,
                            getString(R.string.fetch_failure),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Show recycler view
     *
     * @param paths List Uri to file
     */
    private fun showRecyclerView(paths: ArrayList<String>) {
        val rvGallery = binding.rvGallery

        rvGallery.layoutManager = GridLayoutManager(this, 5)
        rvGallery.adapter = GalleryAdapter(paths)
    }

    /**
     * Set up Top App Bar
     */
    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Set up visibility to image and text as empty indicator
     *
     * @param isEmptyData is fetched data empty
     */
    private fun showEmptyImage(isEmptyData: Boolean) {
        binding.emptyImage.visibility = if (isEmptyData) View.VISIBLE else View.GONE
        binding.emptyTitle.visibility = if (isEmptyData) View.VISIBLE else View.GONE
    }

    /**
     * Set up Loading view on Background process
     *
     * @param isLoading is still fetching data from local gallery
     */
    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            rbBathroom.isEnabled = !isLoading
            rbBedroom.isEnabled = !isLoading
            rbDinning.isEnabled = !isLoading
            rbLivingroom.isEnabled = !isLoading
            rbKitchen.isEnabled = !isLoading

            viewLoading.animateVisibility(isLoading)
        }
    }
}