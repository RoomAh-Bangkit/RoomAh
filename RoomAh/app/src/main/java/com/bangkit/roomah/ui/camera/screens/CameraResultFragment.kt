package com.bangkit.roomah.ui.camera.screens

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.FragmentCameraResultBinding
import com.bangkit.roomah.utils.FileHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraResultFragment : Fragment() {

    private var _binding: FragmentCameraResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var myActivity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraResultBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myActivity = (activity as AppCompatActivity)

        setUpToolbar()

        val result = CameraResultFragmentArgs.fromBundle(arguments as Bundle).result
        val getFile = CameraResultFragmentArgs.fromBundle(arguments as Bundle).picture
        FileHandler.copyImage(
            requireActivity().application,
            getFile,
            resources.getString(R.string.image_name, result, getFile.name)
        )

        val isBackCamera = CameraResultFragmentArgs.fromBundle(
            arguments as Bundle
        ).isBackCamera

        val photo = FileHandler.rotateBitmap(
            BitmapFactory.decodeFile(getFile.path),
            isBackCamera
        )

        binding.ivSavedPicture.setImageBitmap(photo)
        binding.tvResult.text = result
    }

    private fun setUpToolbar() {
        myActivity.setSupportActionBar(binding.toolbar)
        myActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        myActivity.supportActionBar?.setDisplayShowHomeEnabled(true)
        myActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            myActivity.onBackPressed()
        }
    }
}