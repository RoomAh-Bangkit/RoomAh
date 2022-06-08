package com.bangkit.roomah.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.FragmentSplashBinding
import com.bangkit.roomah.ui.camera.CameraActivity

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentSplashBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isOnBoardingFinished()) {
                // Go To Another Activity
                Intent(requireContext(), CameraActivity::class.java).also { intent ->
                    startActivity(intent)
                    requireActivity().finish()
                }
                Log.d(TAG, "Go To Camera")
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                Log.d(TAG, "Go To OnBoarding")
            }
        }, SPLASH_DELAY)

        return binding.root
    }

    private fun isOnBoardingFinished() : Boolean =
        requireActivity().getSharedPreferences(
            "onBoarding",
            Context.MODE_PRIVATE
        ).getBoolean("Finished",false)

    companion object {
        private const val TAG = "SPLASH DECISION"
        private const val SPLASH_DELAY: Long = 3000
    }
}