package com.nityapotti.unity.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nityapotti.unity.databinding.FragmentForgotPasswordBinding
import com.nityapotti.unity.viewmodels.ForgotPasswordViewModel

class ForgotPasswordFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        forgotPasswordViewModel = ViewModelProvider(activity).get(ForgotPasswordViewModel::class.java)
        binding.sendForgotPasswordEmailButtonFragment.setOnClickListener {
            sendEmail()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun sendEmail() {
        val email = binding.forgotPasswordUserInputFragment.text.toString().trim()
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.forgotPasswordUserInputFragment.error = "Please enter a valid email."
            return
        }
        forgotPasswordViewModel.forgotUserEmail.value = email
        binding.forgotPasswordUserInputFragment.setText("")
        dismiss()
    }

}