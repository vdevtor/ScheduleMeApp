package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentRegisterBinding
import com.vdevtor.schedulemeapp.feature_login.domain.model.ProvideAccountArray
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import com.vdevtor.schedulemeapp.feature_login.presentation.util.navigateWithAnimations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val provideAccountArray : ProvideAccountArray by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onButtonsClick()
        binding.spinner.adapter = AccountTypeAdapter(requireContext(),R.layout.account_type_item,provideAccountArray())
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }

        listenToChanges()
    }


    private fun listenToChanges() {

    }

    private fun onButtonsClick(){
        binding.backArrow.setOnClickListener {
            findNavController().navigateWithAnimations(RegisterFragmentDirections.actionRegisterToLogin().actionId)
        }

        binding.registerButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
        }
    }



}