package com.vdevtor.schedulemeapp.feature_feed.presentation

import com.vdevtor.common.core.BaseActivity
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.databinding.ActivityFeedBinding

class FeedActivity : BaseActivity<ActivityFeedBinding>(
    ActivityFeedBinding::inflate,
    { R.id.navHostFeedFragment }
    ){

    override fun onStart() {
        super.onStart()
    }
}