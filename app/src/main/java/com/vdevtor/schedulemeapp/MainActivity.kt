package com.vdevtor.schedulemeapp

import com.vdevtor.schedulemeapp.core.BaseActivity
import com.vdevtor.schedulemeapp.databinding.ActivityMainBinding
import com.vdevtor.schedulemeapp.feature_login.presentation.login_account.LoginFragmentDirections
import com.vdevtor.schedulemeapp.feature_login.util.navigateWithAnimations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate,
    { R.id.navHostLoginFragment }
) {

    override fun onStart() {
        super.onStart()
    }
}