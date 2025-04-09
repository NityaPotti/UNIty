package com.nityapotti.unity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nityapotti.unity.ui.fragments.DmFragment
import com.nityapotti.unity.ui.fragments.ExploreFragment
import com.nityapotti.unity.ui.fragments.ProfileFragment
import com.nityapotti.unity.ui.fragments.SuggestedFragment

class NavigationMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation_menu)

        val exploreFragment = ExploreFragment()
        val suggestedFragment = SuggestedFragment()
        val dmFragment = DmFragment()
        val profileFragment = ProfileFragment()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        makeCurrentFragment(exploreFragment)

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_explore -> makeCurrentFragment(exploreFragment)
                R.id.ic_suggested -> makeCurrentFragment(suggestedFragment)
                R.id.ic_dm -> makeCurrentFragment(dmFragment)
                R.id.ic_profile -> makeCurrentFragment(profileFragment)
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}