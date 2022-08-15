package com.techniquesmyanmar.greennotes.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.techniquesmyanmar.greennotes.R
import com.techniquesmyanmar.greennotes.databinding.ActivitySignInSignUpBinding
import com.techniquesmyanmar.greennotes.entity.User
import com.techniquesmyanmar.greennotes.viewModel.UserViewModel


class SignInSignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInSignUpBinding
    private var id : Int?= null
    private lateinit var userList : ArrayList<User>
    private val viewModel by viewModels<UserViewModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singUp.setOnClickListener {
            switchToSignUpLayout()
        }
        binding.logIn.setOnClickListener {
            switchToSignInLayout()
        }
        binding.singIn.setOnClickListener {
            getUserAndSignInCheck("")
        }

        binding.signUp.setOnClickListener {
            var email = binding.eMails.text.toString().trim()
            var pwd = binding.passwordss.text.toString().trim()
            var confirm = binding.passwords01.text.toString().trim()


            if (email.isNullOrEmpty() && pwd.isNullOrEmpty() && confirm.isNullOrEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }else if (!isValidEmail(email)) {
                Toast.makeText(this, "Enter correct email", Toast.LENGTH_SHORT).show()
            }
            else if(pwd == ""){
                Toast.makeText(
                    this,
                    "Password can't be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }else if( pwd.length < 6){
                Toast.makeText(
                    this,
                    "Password should contain at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (pwd != confirm) {
                Toast.makeText(
                    this,
                    "Password and confrim password must be the same",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val user = User(id, "", "", pwd, email.lowercase())
                viewModel.insertUser(user)
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                clearEditText()
                switchToSignInLayout()
            }
        }
    }

    private fun getUserAndSignInCheck(s: String){

        var userId = 0
        viewModel.getAllUsers(s).observe(this){
            userList =  it as ArrayList<User>
            var email = binding.eMail.text.toString().trim()
            var pwd = binding.passwords.text.toString().trim()

            if(email.isNullOrEmpty() &&
                pwd.isNullOrEmpty()){
                Toast.makeText(this,"Fill All Fields",Toast.LENGTH_SHORT).show()
            }else{
                for(user in userList){
                    if(user.user_email.equals(email)&&
                        user.user_pwd.equals(pwd)){
                        userId = user.id!!
                        break
                    }
                }
                if (userId != 0){
                    Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user_id",userId)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Invalid email or password.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun switchToSignInLayout(){
        binding.singUp.background = null
        binding.singUp.setTextColor(resources.getColor(R.color.greenColor,null))
        binding.logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
        binding.singUpLayout.visibility = View.GONE
        binding.logInLayout.visibility = View.VISIBLE
        binding.singIn.visibility = View.VISIBLE
        binding.signUp.visibility = View.GONE
        binding.logIn.setTextColor(resources.getColor(R.color.textColor,null))
        clearEditText()
    }

    private fun switchToSignUpLayout(){
        binding.singUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
        binding.singUp.setTextColor(resources.getColor(R.color.textColor,null))
        binding.logIn.background = null
        binding.singUpLayout.visibility = View.VISIBLE
        binding.singIn.visibility = View.GONE
        binding.logInLayout.visibility = View.GONE
        binding.signUp.visibility = View.VISIBLE
        binding.logIn.setTextColor(resources.getColor(R.color.greenColor,null))
        clearEditText()
    }

    private fun clearEditText(){
        binding.eMail.setText("")
        binding.passwords.setText("")
        binding.eMails.setText("")
        binding.passwordss.setText("")
        binding.passwords01.setText("")
    }
}