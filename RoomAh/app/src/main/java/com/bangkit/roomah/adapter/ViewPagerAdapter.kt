package com.bangkit.roomah.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.roomah.ui.onboarding.OnboardingFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        val fragment = OnboardingFragment()
        fragment.arguments = Bundle().apply {
            putInt(OnboardingFragment.SECTION_NUMBER, position)
        }
        return fragment
    }
    override fun getItemCount(): Int = 3
}