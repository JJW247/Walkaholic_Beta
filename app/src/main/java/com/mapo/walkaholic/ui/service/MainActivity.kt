package com.mapo.walkaholic.ui.service

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapo.walkaholic.R
import com.mapo.walkaholic.databinding.ActivityMainBinding
import com.mapo.walkaholic.ui.base.BaseActivity
import com.mapo.walkaholic.ui.global.GlobalApplication
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fm = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        when (item.itemId) {
            R.id.action_main -> {
                binding.mainToolbar.visibility = View.VISIBLE
                binding.mainBtnPrev.visibility = View.GONE
                fm.popBackStackImmediate("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                var dashboardFragment = DashboardFragment()
                transaction.replace( R.id.main_content, dashboardFragment, "dashboard")
                transaction.addToBackStack("dashboard")
            }
            R.id.action_theme -> {
                binding.mainToolbar.visibility = View.VISIBLE
                binding.mainBtnPrev.visibility = View.VISIBLE
                fm.popBackStackImmediate("theme", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                var themeFragment = ThemeFragment()
                transaction.replace(R.id.main_content, themeFragment, "theme")
                transaction.addToBackStack("theme")
            }
            R.id.action_challenge -> {
                binding.mainToolbar.visibility = View.VISIBLE
                binding.mainBtnPrev.visibility = View.VISIBLE
                fm.popBackStackImmediate("challenge", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                var challengeFragment = ChallengeFragment()
                transaction.replace(R.id.main_content, challengeFragment, "challenge")
                transaction.addToBackStack("challenge")
            }
            R.id.action_map -> {
                binding.mainToolbar.visibility = View.GONE
                fm.popBackStackImmediate("map", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                var mapFragment = MapFragment()
                transaction.replace(R.id.main_content, mapFragment, "map")
                transaction.addToBackStack("map")
            }
        }
        transaction.commit()
        transaction.isAddToBackStackAllowed
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        when (Build.VERSION.SDK_INT) {
            in (Build.VERSION_CODES.KITKAT until (Build.VERSION_CODES.M)) -> {
                @Suppress("DEPRECATION")
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            in (Build.VERSION_CODES.M)..Build.VERSION_CODES.R -> {
                @Suppress("DEPRECATION")
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.TRANSPARENT
            }
        }
        GlobalApplication.activityList.add(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        bottom_navigation.setOnNavigationItemSelectedListener(this)

        val fm = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction()
        binding.mainToolbar.visibility = View.VISIBLE
        binding.mainBtnPrev.visibility = View.VISIBLE
        fm.popBackStackImmediate("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val dashboardFragment = DashboardFragment()
        transaction.replace(R.id.main_content, dashboardFragment, "dashboard")
        transaction.addToBackStack("dashboard")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        transaction.isAddToBackStackAllowed

        binding.mainBtnPrev.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        updateBottomNavigation(bottomNavigationView)
    }

    private fun updateBottomNavigation(navigation: BottomNavigationView) {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("dashboard")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("theme")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("challenge")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("map")

        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.action_main).isChecked = true }
        if(tag2 != null && tag2.isVisible) {navigation.menu.findItem(R.id.action_theme).isChecked = true }
        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.action_challenge).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation.menu.findItem(R.id.action_map).isChecked = true }
    }

    override fun onDestroy() {
        GlobalApplication.activityList.remove(this)
        super.onDestroy()
    }
}