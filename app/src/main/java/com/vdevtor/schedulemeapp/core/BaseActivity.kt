package com.vdevtor.schedulemeapp.core

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(
    private val bindFactory: (LayoutInflater) -> VB
) : AppCompatActivity() {

    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindFactory(layoutInflater)
        setContentView(binding.root)
    }
}