package com.nityapotti.unity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForgotPasswordViewModel:ViewModel() {
    var forgotUserEmail = MutableLiveData<String>()
}