package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.common.core.BaseFragment
import com.vdevtor.common.utils.MaskEditUtil
import com.vdevtor.common.utils.convertUriToBimap
import com.vdevtor.common.utils.verifyGalleryPermissions
import com.vdevtor.data.local.entity.AppUserModelDto
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.databinding.FragmentRegisterBinding
import com.vdevtor.schedulemeapp.feature_login.domain.model.InputModel
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
    private var permissionsHandler: Array<String> = arrayOf()
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onButtonsClick()
        observeEditTextPassword()
        applyMask()
        binding.spinner.adapter =
            AccountTypeAdapter(requireContext(), R.layout.account_type_item, provideAccountArray())
        listenToChanges()
        uiEventListener()
        mediaEventListener()
    }

    private val launchPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    private fun checkPermissions(): Boolean {
        return if (permissionsHandler.isEmpty()) true else {
            launchPermissions.launch(permissionsHandler)
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val getPictureFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it as Uri
            binding.photo.setImageURI(it)
        }

    private val getPictureFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            imageBitmap = it as Bitmap
            binding.photo.setImageBitmap(it)
        }

    private fun listenToChanges() {
        lifecycleScope.launchWhenResumed {
            authViewModel.state.collectLatest {
                when (it) {
                    is AuthStateInfo.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (binding.uploadingPhoto.isVisible) binding.uploadingPhoto.visibility =
                            View.GONE
                        authViewModel.clearState()
                        //TO DO GO TO NEXT ACTIVITY
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
                        authViewModel.clearState()
                    }
                    else -> Unit
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun onButtonsClick() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateWithAnimationsPopUp(RegisterFragmentDirections.actionRegisterToLogin().actionId)
        }

        binding.registerButton.setOnClickListener {
            val inputs = getInputs()
            authViewModel.validateEmail(inputs.email)
            authViewModel.validateName(inputs.name)
            authViewModel.validatePassword(inputs.password, inputs.confirmPassword)
            Log.d("cul", "onButtonsClick: ${authViewModel.count}")
            if (authViewModel.count >= 3) {
                authViewModel.count = 0
                authViewModel.registerWithCredentials(
                    AppUserModelDto(
                        name = inputs.name,
                        email = inputs.email,
                        phone = inputs.phone,
                        accountType = inputs.accountType,
                        jobOffered = "",
                        userUID = "",
                        profilePicPath = ""

                    ), inputs.password, imageUri != null
                )
            }
        }

        binding.addPhoto.setOnClickListener {
            permissionsHandler = verifyGalleryPermissions(requireContext())
            if (checkPermissions()) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.select_an_image))
                    .setMessage(getString(R.string.choose_your_option))
                    .setPositiveButton(getString(R.string.camera)) { dialog, _ ->
                        dialog.dismiss()
                        getPictureFromCamera.launch()
                    }
                    .setNegativeButton(getString(R.string.gallery)) { dialog, _ ->
                        dialog.dismiss()
                        getPictureFromGallery.launch("image/*")
                    }.create()
                    .show()

            } else
                Snackbar.make(
                    binding.root,
                    "Make Sure To enable Camera Permissions",
                    Snackbar.LENGTH_LONG
                ).show()
        }
    }

    private fun getInputs(): InputModel {
        return InputModel(
            email = binding.email.text.toString(),
            password = binding.passwordEditText.text.toString(),
            confirmPassword = binding.confirmPasswordET.text.toString(),
            phone = binding.phone.text.toString(),
            name = binding.name.text.toString(),
            accountType = if (binding.spinner.selectedItem.toString()
                    .contains("Personal")
            ) "personal" else "business"
        )
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
                    is AuthViewModel.UiEvent.PhotoUploadSuccess -> {

                    }
                    is AuthViewModel.UiEvent.UploadingPhoto -> {
                        binding.uploadingPhoto.visibility = View.VISIBLE
                    }

                    is AuthViewModel.UiEvent.UploadingPhotoFailed -> {
                        authViewModel.setStateError(event.message)
                        binding.uploadingPhoto.visibility = View.GONE
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun mediaEventListener() {
        lifecycleScope.launch {
            authViewModel.mediaFlow.collectLatest { event ->
                when (event) {
                    is AuthViewModel.MediaEvent.SavePhotoInternally -> {
                        if (imageBitmap != null) {
                            imageBitmap?.let {
                                authViewModel.saveProfilePhotoInternally(
                                    requireContext(),
                                    it,
                                    event.uiid
                                )
                            }
                        } else {
                            imageUri?.let {
                                authViewModel.saveProfilePhotoInternally(
                                    requireContext(),
                                    convertUriToBimap(it, requireContext()), event.uiid
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}