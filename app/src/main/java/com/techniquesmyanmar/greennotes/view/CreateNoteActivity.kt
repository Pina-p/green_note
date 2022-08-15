package com.techniquesmyanmar.greennotes.view

import android.app.Activity
import android.content.*
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import com.techniquesmyanmar.greennotes.databinding.ActivityCreateNoteBinding
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.view.NoteBottomSheetFragment.Companion.noteId
import com.techniquesmyanmar.greennotes.viewModel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    private val viewModel by viewModels<NoteViewModel>()
    private lateinit var binding : ActivityCreateNoteBinding
    private var id:Int ?= null
    private var imageUri: Uri ?= null
    var selectedColor = "#B5CA98"
    var currentDate:String? = null


    private var selectedImagePath = ""
    private var webLink = ""
    private var user_id :Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        id = intent.getIntExtra("id",-1) //from onclick
        user_id = intent.getIntExtra("user_id",0) // from new


        selectedColor = if(id != -1){ //update
            getNote(id!!)
            intent.getStringExtra("color")!!
        } else{
            "#B5CA98"
        }
        binding.imgMore.setOnClickListener{
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(id!!)
            noteBottomSheetFragment.show(supportFragmentManager,"Note Bottom Sheet Fragment")
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )


        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())
        binding.tvDateTime.text = currentDate



        binding.ivConfirm.isClickable = true
        binding.ivConfirm.setOnClickListener{
            upsertNote()
            finish()
        }

        binding.ivBack.isClickable = true
        binding.ivBack.setOnClickListener {
            finish()
        }


        binding.imgDelete.isClickable=true
        binding.imgDelete.setOnClickListener {
            selectedImagePath = ""
            binding.layoutImage.visibility = View.GONE
        }

        binding.btnOk.setOnClickListener {
            if (binding.etWebLink.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(this,"Url is Required",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            if (noteId != -1){
                binding.tvWebLink.visibility = View.VISIBLE
                binding.layoutWebUrl.visibility = View.GONE
            }else{
                binding.layoutWebUrl.visibility = View.GONE
            }

        }

        binding.imgUrlDelete.setOnClickListener {
            webLink = ""
            binding.tvWebLink.visibility = View.GONE
            binding.imgUrlDelete.visibility = View.GONE
            binding.layoutWebUrl.visibility = View.GONE
        }


        binding.tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(binding.etWebLink.text.toString()))
            startActivity(intent)
        }

    }

    private fun checkWebUrl(){
        if (Patterns.WEB_URL.matcher(binding.etWebLink.text.toString()).matches()){
            binding.layoutWebUrl.visibility = View.GONE
            binding.etWebLink.isEnabled = false
            webLink = binding.etWebLink.text.toString()
            binding.tvWebLink.visibility = View.VISIBLE
            binding.tvWebLink.text = binding.etWebLink.text.toString()
        }else{
            Toast.makeText(this,"Url is not valid",Toast.LENGTH_SHORT).show()
        }
    }


    private fun getNote(id: Int) {
        viewModel.getSpecificNote(id).observe(this){
            setUpData(it!!)
        }
    }

    private fun setUpData(it:Notes){
        binding.apply {
            lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))
            etTitle.setText(it.title)
            etNoteDesc.setText(it.noteText)
            selectedImagePath = it.imagePath
            if (it.imagePath != "" && it.imagePath != "null"){
                selectedImagePath = it.imagePath
                imgNote.setImageURI(Uri.parse(it.imagePath))
                layoutImage.visibility = View.VISIBLE
                imgNote.visibility = View.VISIBLE
                imgDelete.visibility = View.VISIBLE
            }
            else{
                layoutImage.visibility = View.GONE
                imgNote.visibility = View.GONE
                imgDelete.visibility = View.GONE
            }

            if (it.webLink != ""){
                webLink = it.webLink!!
                tvWebLink.text = it.webLink
                layoutWebUrl.visibility = View.VISIBLE
                etWebLink.setText(it.webLink)
                imgUrlDelete.visibility = View.VISIBLE
            }else{
                imgUrlDelete.visibility = View.GONE
                layoutWebUrl.visibility = View.GONE
            }
        }
    }

    private fun upsertNote() {
        //check new user
        if(id == -1){
            id = null
        }
        // save user id for update note
        if(user_id == 0){
            user_id = intent.getIntExtra("userIdToUpdate",0)
        }
        // if user is already existed,get image path from db
        if(id != -1 && id != null){
            getImageToUpdate()
        }
        val title = binding.etTitle.text.toString().trim()
        val note_text = binding.etNoteDesc.text.toString().trim()
        val date_time = binding.tvDateTime.text.toString()

        if(title.isNullOrEmpty() && note_text.isNullOrEmpty() &&
            selectedImagePath.isNullOrEmpty() && webLink.isNullOrEmpty()){
            Toast.makeText(this,"Empty note discarded",Toast.LENGTH_SHORT).show()
            finish()
        } else{
            val note = Notes(
                id,title,note_text,date_time,selectedImagePath,webLink,selectedColor,user_id)

            viewModel.insertNotes(note)
        }

    }

    private fun getImageToUpdate(){
        viewModel.getSpecificNote(id!!).observe(this){
            selectedImagePath = it.imagePath
        }

    }

    private fun deleteNote(){
        viewModel.deleteNoteById(id!!)
    }

    private fun showDialog(){

        lateinit var dialog:AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
                DialogInterface.BUTTON_POSITIVE -> deleteNote()
            }
        }

        builder.setPositiveButton("YES",dialogClickListener)
        builder.setNegativeButton("NO",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }

    private val BroadcastReceiver : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionColor = p1!!.getStringExtra("action")

            when(actionColor!!){

                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Coffee" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "MakeUp" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Pink" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Image" -> {
                    ImagePicker.with(this@CreateNoteActivity)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                    binding.layoutWebUrl.visibility = View.GONE
                }
                "WebUrl" ->{
                    binding.layoutWebUrl.visibility = View.VISIBLE
                }
                "CopyNote" -> {
                    getImageToUpdate()
                    id = null
                    upsertNote()
                    finish()
                }
                "ShareNote" -> {
                    val intent = Intent()
                    getImageToUpdate()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_SUBJECT, binding.etTitle.text.toString())
                    intent.putExtra(
                        Intent.EXTRA_TEXT, binding.etTitle.text.toString() +
                                "\n" +binding.etNoteDesc.text.toString()
                    )

                   intent.type = "text/plain"
                    startActivity(intent)
                }
                "DeleteNote" -> {
                     showDialog()
                }


                else -> {
                    binding.layoutImage.visibility = View.GONE
                    binding.imgNote.visibility = View.GONE
                    binding.layoutWebUrl.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.lyCreateNote.setBackgroundColor(Color.parseColor(selectedColor))

                }
            }
        }

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    imageUri = fileUri
                    binding.imgNote.load(imageUri){
                        crossfade(true)
                        crossfade(1000)
                        transformations(RoundedCornersTransformation())
                    }

                    selectedImagePath = imageUri.toString()
                    binding.layoutImage.visibility = View.VISIBLE
                    binding.imgNote.visibility = View.VISIBLE
                    binding.imgDelete.visibility = View.VISIBLE

                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        }


}