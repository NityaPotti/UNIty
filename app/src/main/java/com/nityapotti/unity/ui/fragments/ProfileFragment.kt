package com.nityapotti.unity.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.nityapotti.unity.MainActivity
import com.nityapotti.unity.R
import com.nityapotti.unity.views.LoginActivity
import com.nityapotti.unity.views.SettingsActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsIcon = view.findViewById<ImageView>(R.id.topRightSettingsIcon)
        settingsIcon?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }


        val btnLogOut = view.findViewById<Button>(R.id.btnLogOut)
        // If the ID doesn’t exist in fragment_profile.xml, this will be null → check your XML.
        btnLogOut?.setOnClickListener {
            Toast.makeText(requireContext(), "Logging out…", Toast.LENGTH_SHORT).show()

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            }
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("param1", param1)
                putString("param2", param2)
            }
        }
    }
}
