package com.techniquesmyanmar.greennotes.view

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.techniquesmyanmar.greennotes.R
import com.techniquesmyanmar.greennotes.databinding.ActivityProfileBinding
import com.techniquesmyanmar.greennotes.entity.User
import com.techniquesmyanmar.greennotes.viewModel.NoteViewModel
import com.techniquesmyanmar.greennotes.viewModel.UserViewModel


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private var mProfileUri: Uri?=null
    var pDialog: ProgressDialog? = null
    lateinit var handler: Handler
    private var userId = 0
    private val viewModel : UserViewModel by viewModels()
    private val noteViewModel : NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("user_id", 0)
        getUser(userId)

        binding.ivBackProfile.setOnClickListener {
            finish()
        }
        binding.tvAccSetting.setOnClickListener {
            var intent = Intent(this, AccountSettingActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        binding.tvShareApp.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, "Android Studio Pro")
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Download green note app here https://www.mediafire.com/file/s0frxclttel64v0/green_note.apk/file"
            )
            intent.type = "text/plain"
            startActivity(intent)
        }
        binding.tvFeedback.setOnClickListener {
            var i = Intent(Intent.ACTION_SEND)
            i.type = "message/html"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("sunshineK360@gmail.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback from App")
            try {
                startActivity(Intent.createChooser(i, "Send feedback..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    applicationContext,
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.tvDeleteAcc.setOnClickListener {
            showDialog()
            handler = Handler()
            handler.postDelayed({
                deleteUser(userId)
                finishAffinity()
                System.exit(0)
            }, 5000)

        }

        binding.tvLogOut.setOnClickListener {
            launchShowDialog(it)
            handler = Handler()
            handler.postDelayed({
                finishAffinity()
                System.exit(0)
            } , 3000)

        }


    }

    private fun launchShowDialog(view: View?) {
        pDialog = ProgressDialog(this@ProfileActivity)
        pDialog!!.setTitle("Loading")
        pDialog!!.setMessage("loging out.....")
        pDialog!!.show()
    }

    private fun getUser(id: Int) {
        viewModel.getUserById(id).observe(this){
            setUpData(it!!)
        }
    }

    private fun setUpData(it: User) {
        var nameFromEmail =""
        binding.apply {
            if(it.user_name == "null" || it.user_name == ""){
                nameFromEmail = it.user_email
                tvUserName.text = nameFromEmail.replace("@gmail.com","")
            }else{
                tvUserName.text = it.user_name
            }
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

    private fun showDialog(){

        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure to delete this account?")
        builder.setMessage("This will cause to lose all of your data.")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                    Toast.makeText(this,"Deleting",Toast.LENGTH_SHORT).show()
                DialogInterface.BUTTON_NEGATIVE ->
                    Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("NO",dialogClickListener)
        builder.setPositiveButton("YES",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }

    private fun deleteUser(user_id:Int){
        viewModel.deleteUserById(user_id)
        noteViewModel.deleteUserData(user_id)
    }

}