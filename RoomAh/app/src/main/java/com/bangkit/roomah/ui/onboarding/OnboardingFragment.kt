package com.bangkit.roomah.ui.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.roomah.R
import com.bangkit.roomah.data.model.OnBoarding
import com.bangkit.roomah.databinding.FragmentOnboardingBinding
import com.bumptech.glide.Glide

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var data: OnBoarding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(SECTION_NUMBER, 0)

        if (index != null) {
            data = getOnBoardingItem(index)

            binding.apply {
                onboardingTitle.text = data.title
                onboardingSubtitle.text = data.subtitle
                btnNext.text = if (index == 2)
                    requireContext().getString(R.string.onboarding_move)
                else
                    requireContext().getString(R.string.onboarding_next)

                Glide.with(requireContext())
                    .load(data.image)
                    .into(onboardingImage)

                btnSkip.setOnClickListener {
                    finishOnBoarding()
                }

                val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
                btnNext.setOnClickListener {
                    if (index == 2) {
                        finishOnBoarding()
                    } else {
                        viewPager?.currentItem = index + 1
                    }
                }
            }
        }
    }

    private fun finishOnBoarding() {
        setFinishedOnBoarding()

        // Go To Another Activity
//        Intent(requireContext(), CameraActivity::class.java).also { intent ->
//            startActivity(intent)
//            requireActivity().finish()
//        }
    }

    private fun setFinishedOnBoarding() {
        val pref = requireActivity().getSharedPreferences(
            "onBoarding",
            Context.MODE_PRIVATE
        ).edit()

        pref.putBoolean("Finished", true)
        pref.apply()
    }

    private fun getOnBoardingItem(pos: Int) : OnBoarding {

        val onboardingTitle = resources.getStringArray(R.array.onboarding_titles)
        val onboardingSubtitle = resources.getStringArray(R.array.onboarding_subtitles)
        val onboardingImage = resources.obtainTypedArray(R.array.onboarding_images)

        val onboarding = OnBoarding(
            onboardingTitle[pos],
            onboardingSubtitle[pos],
            onboardingImage.getResourceId(pos, pos)
        )

        onboardingImage.recycle()
        return onboarding
    }

    companion object {
        const val SECTION_NUMBER = "section_number"
    }
}