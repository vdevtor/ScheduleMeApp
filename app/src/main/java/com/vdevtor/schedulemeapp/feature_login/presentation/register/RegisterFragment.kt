package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.common.core.BaseFragment
import com.vdevtor.common.utils.MaskEditUtil
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.databinding.FragmentRegisterBinding
import com.vdevtor.schedulemeapp.feature_login.domain.model.ProvideAccountArray
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthStateInfo
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import com.vdevtor.schedulemeapp.feature_login.presentation.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val provideAccountArray: ProvideAccountArray by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onButtonsClick()
        observeEditTextPassword()
        applyMask()
        binding.spinner.adapter =
            AccountTypeAdapter(requireContext(), R.layout.account_type_item, provideAccountArray())
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        listenToChanges()
        uiEventListener()
    }


    private fun listenToChanges() {
        lifecycleScope.launchWhenResumed {
            authViewModel.state.collectLatest {
                when (it) {
                    is AuthStateInfo.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "me conectei", Toast.LENGTH_SHORT).show()
                    }

                    is AuthStateInfo.Loading -> {
                        binding.registerButton.visibility = View.INVISIBLE
                        binding.passwordValidationsBox.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE

                    }

                    is AuthStateInfo.AuthError -> {
                        binding.progressBar.visibility = View.GONE
                        binding.passwordValidationsBox.visibility = View.VISIBLE
                        binding.registerButton.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun onButtonsClick() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateWithAnimationsPopUp(RegisterFragmentDirections.actionRegisterToLogin().actionId)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordET.text.toString()
            val name = binding.name.text.toString()
            authViewModel.validateEmail(email)
            authViewModel.validateName(name)
            authViewModel.validatePassword(password, confirmPassword)
            Log.d("cul", "onButtonsClick: ${authViewModel.count}")
            if (authViewModel.count == 3) {
                authViewModel.registerWithCredentials(email, password)
            }

        }
    }



    private fun observeEditTextPassword() {

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.hasOneSpecialChar()) {
                        binding.special.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.special.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasOneDigit()) {
                        binding.digit.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.digit.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasOneUpLetter()) {
                        binding.upLetter.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.upLetter.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasSixChar()) {
                        binding.numberofletters.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.numberofletters.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.hasOneSpecialChar()) {
                        binding.special.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.special.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasOneDigit()) {
                        binding.digit.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.digit.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasOneUpLetter()) {
                        binding.upLetter.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.upLetter.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )

                    if (it.hasSixChar()) {
                        binding.numberofletters.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else binding.numberofletters.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                }
            }

        })




        binding.confirmPasswordET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.confirmPassword.isEndIconVisible = true
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    private fun applyMask() {
        binding.phone.addTextChangedListener(
            MaskEditUtil.mask(
                binding.phone,
                MaskEditUtil.FORMAT_PHONE
            )
        )
    }

    private fun uiEventListener() {
        lifecycleScope.launch {
            authViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AuthViewModel.UiEvent.PasswordError -> {
                        binding.progressBar.visibility = View.GONE
                        binding.passwordValidationsBox.visibility = View.VISIBLE
                        binding.registerButton.visibility = View.VISIBLE

                    }
                    is AuthViewModel.UiEvent.EmailError -> {
                        binding.progressBar.visibility = View.GONE
                        binding.passwordValidationsBox.visibility = View.VISIBLE
                        binding.email.error = getString(R.string.invalid_email)
                        binding.email.setText("")
                    }

                    AuthViewModel.UiEvent.EmailBlank -> {
                        binding.progressBar.visibility = View.GONE
                        binding.passwordValidationsBox.visibility = View.VISIBLE
                        binding.email.error = getString(R.string.invalid_email_blank)
                        binding.email.setText("")
                    }

                    is AuthViewModel.UiEvent.NameError -> {
                        binding.name.error = getString(R.string.enter_your_full_name)
                    }
                    is AuthViewModel.UiEvent.PassWordWeak -> {

                        binding.confirmPassword.isEndIconVisible = false
                        binding.confirmPasswordET.error = getString(R.string.invalid_password)
                        observeEditTextPassword()
                    }
                    is AuthViewModel.UiEvent.PassWordDifferent -> {
                        binding.confirmPassword.isEndIconVisible = false
                        binding.confirmPasswordET.error =
                            getString(R.string.invalid_password_must_equal)
                        observeEditTextPassword()
                    }

                    is AuthViewModel.UiEvent.ShowSnackbar -> {
                        binding.progressBar.visibility = View.GONE
                        binding.passwordValidationsBox.visibility = View.VISIBLE
                        binding.registerButton.visibility = View.VISIBLE
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

}