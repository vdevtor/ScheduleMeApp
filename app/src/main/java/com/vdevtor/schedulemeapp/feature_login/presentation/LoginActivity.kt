package com.vdevtor.schedulemeapp.feature_login.presentation

import com.vdevtor.common.core.BaseActivity
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    ActivityLoginBinding::inflate,
    { R.id.navHostLoginFragment }
) {

    override fun onStart() {
        super.onStart()
    }
}