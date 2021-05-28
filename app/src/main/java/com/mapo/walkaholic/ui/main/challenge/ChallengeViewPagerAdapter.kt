package com.mapo.walkaholic.ui.main.challenge

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mapo.walkaholic.ui.main.challenge.mission.ChallengeDetailMissionListener

class ChallengeViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentSize: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragmentSize

    override fun createFragment(position: Int): Fragment {
        return ChallengeDetailFragment(position)
    }
}