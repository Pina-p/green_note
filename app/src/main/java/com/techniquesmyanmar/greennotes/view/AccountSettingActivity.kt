package com.techniquesmyanmar.greennotes.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import com.techniquesmyanmar.greennotes.R
import com.techniquesmyanmar.greennotes.databinding.ActivityAccountSettingBinding
import com.techniquesmyanmar.greennotes.entity.User
import com.techniquesmyanmar.greennotes.viewModel.UserViewModel
import java.util.concurrent.Executor

class AccountSettingActivity:AppCompatActivity() {
    private lateinit var binding : ActivityAccountSettingBinding
    private var mProfileUri: Uri?=null
    private val viewModel : UserViewModel by viewModels()
    private var userId = 0

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId",0)

        getUser(userId)

        lifecycleScope.launchWhenCreated {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint authentication")
                .setSubtitle("Identify using fingerprint to change password")
                .setNegativeButtonText("Cancel")
                .build()
            executor = ContextCompat.getMainExecutor(this@AccountSettingActivity)
            biometricPrompt = BiometricPrompt(this@AccountSettingActivity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence,
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                            "$errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult,
                    ) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                        binding.etPwd.isEnabled = true
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }

                })

        }

        binding.tvChangePwd.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSaveChanges.setOnClickListener{
            update()
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.ivUserprofile.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    mProfileUri = fileUri
                    binding.ivUserprofile.load(fileUri){
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun getUser(id: Int) {
        viewModel.getUserById(id).observe(this){
            if(it != null)
             setUpData(it)
        }
    }

    private fun setUpData(it: User) {
        var nameFromEmail = ""
        binding.apply {
            if(it.user_name == "null" || it.user_name == ""){
                nameFromEmail = it.user_email
                etName.setText(nameFromEmail.replace("@gmail.com",""))
            }else{
                etName.setText(it.user_name)
            }
            etEmail.setText(it.user_email)
            etPwd.setText(it.user_pwd)

            if(it.user_profile_img == "null" || it.user_profile_img ==""){
                ivUserprofile.load(R.drawable.femaleprofile){
                    crossfade(true)
                    crossfade(1000)
                    transformations(CircleCropTransformation())
                }
            }else{
                mProfileUri = Uri.parse(it.user_profile_img)
                ivUserprofile.load(Uri.parse(it.user_profile_img)){
                    crossfade(true)
                    crossfade(1000)
                    transformations(CircleCropTransformation())
                }
            }

        }
    }

    private fun update(){
        var name = binding.etName.text.toString().trim()
        var mail = binding.etEmail.text.toString().trim()
        var pwd = binding.etPwd.text.toString().trim()

        val user = User(userId,name,mProfileUri.toString(),pwd,mail)
        viewModel.insertUser(user)
    }
}