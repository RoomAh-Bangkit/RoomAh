package com.bangkit.roomah.ui.camera.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.FragmentCameraValidateBinding
import com.bangkit.roomah.ui.camera.CameraViewModel
import com.bangkit.roomah.utils.FileHandler
import com.bangkit.roomah.utils.animateVisibility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.properties.Delegates

@AndroidEntryPoint
class CameraValidateFragment : Fragment() {

    private var _binding: FragmentCameraValidateBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context
    private lateinit var getFile: File

    private var isBackCamera by Delegates.notNull<Boolean>()
    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraValidateBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFile = CameraValidateFragmentArgs.fromBundle(arguments as Bundle).picture
        isBackCamera = CameraValidateFragmentArgs.fromBundle(arguments as Bundle).isBackCamera

        val result = FileHandler.rotateBitmap(
            BitmapFactory.decodeFile(getFile.path),
            isBackCamera
        )

        binding.ivCameraResult.setImageBitmap(result)
        setUpActions()
    }

    /**
     * Set up Button OnClick
     */
    private fun setUpActions() {
        binding.apply {
            btnProceed.setOnClickListener {
                classifyImage()
            }

            btnCancel.setOnClickListener {
                findNavController().navigate(R.id.action_cameraValidateFragment_to_cameraXFragment)
            }
        }
    }

    /**
     * Classifying image captured or uploaded from gallery
     */
    private fun classifyImage() {
        setLoadingState(true)

        lifecycleScope.launchWhenStarted {
            launch {
                val file = FileHandler.reduceFileImage(getFile)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    requestImageFile
                )

                cameraViewModel.classifyImage(imageMultipart).collect { response ->
                    response.onSuccess {
                        Toast.makeText(
                            safeContext,
                            getString(R.string.upload_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        val toResultFragment = CameraValidateFragmentDirections.actionCameraValidateFragmentToCameraResultFragment(
                                getFile, it.result, isBackCamera
                            )
                        findNavController().navigate(toResultFragment)
                        setLoadingState(false)
                    }

                    response.onFailure {
                        setLoadingState(false)
                        Log.d("On Failure API", it.message ?: "null")

                        Snackbar.make(
                            binding.root,
                            getString(R.string.upload_failure),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Set up Loading view on Background process
     *
     * @param isLoading is still sending request data to server
     */
    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            btnProceed.isEnabled = !isLoading
            btnCancel.isEnabled = !isLoading

            viewLoading.animateVisibility(isLoading)
        }
    }
}