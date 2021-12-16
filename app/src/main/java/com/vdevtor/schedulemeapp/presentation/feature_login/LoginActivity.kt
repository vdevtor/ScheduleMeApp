package com.vdevtor.schedulemeapp.presentation.feature_login

import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.core.BaseActivity
import com.vdevtor.schedulemeapp.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    ActivityLoginBinding::inflate,
    { R.id.navHostLoginFragment }
) {

    override fun onStart() {
        super.onStart()

    }
}