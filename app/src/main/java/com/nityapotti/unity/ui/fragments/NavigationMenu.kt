package com.nityapotti.unity.ui.fragments
import com.nityapotti.unity.views.SuggestedFragment

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nityapotti.unity.R
import com.nityapotti.unity.views.ChatFragment

class NavigationMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation_menu)

        val roommateFinderFragment = RoommateFinderFragment()
        val suggestedFragment = SuggestedFragment()
        val dmFragment = ChatFragment()
        val profileFragment = ProfileFragment()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        makeCurrentFragment(roommateFinderFragment)

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_explore -> makeCurrentFragment(roommateFinderFragment)
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