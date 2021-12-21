package com.vdevtor.common.core

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(
    private val bindFactory: (LayoutInflater) -> VB,
    private val navHost: () -> Int

) : AppCompatActivity() {

    lateinit var binding: VB
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindFactory(layoutInflater)
        setContentView(binding.root)
        navController = NavHostController(this)
    }
}