package com.techniquesmyanmar.greennotes.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.techniquesmyanmar.greennotes.R
import com.techniquesmyanmar.greennotes.adapter.NoteAdapter
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.entity.User
import com.techniquesmyanmar.greennotes.viewModel.NoteViewModel
import com.techniquesmyanmar.greennotes.viewModel.UserViewModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList : ArrayList<Notes>
    private val viewModel by viewModels<NoteViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var fab_add : FloatingActionButton
    private lateinit var rv_note : RecyclerView
    private lateinit var img_profile: ImageView
    private var id =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = intent.getIntExtra("user_id",0)
        fab_add = findViewById(R.id.fabBtnCreateNote)
        rv_note = findViewById(R.id.recycler_view)
        img_profile = findViewById(R.id.iv_profile)
        val search = findViewById<android.widget.SearchView>(R.id.search_view)

        getUserProfile(id)

        img_profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("user_id",id)
            startActivity(intent)
        }

        Log.i("", "onCreate: ${id}")
        fab_add.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            intent.putExtra("user_id",id)
            startActivity(intent)
            //finish()
        })

        getNotes()
        noteList = arrayListOf()
        noteAdapter = NoteAdapter(noteList)



        rv_note.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            //layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
            adapter = noteAdapter

        }

        noteAdapter.setOnClickListener(object : NoteAdapter.OnItemClickListener{
            override fun onClicked(noteId: Int, color: String) {
                val intent = Intent(this@MainActivity, CreateNoteActivity::class.java)
                intent.putExtra("id",noteId)
                intent.putExtra("userIdToUpdate",id)
                intent.putExtra("color",color)
                startActivity(intent)
            }

        })



        search.setOnQueryTextListener( object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Notes>()

                for (arr in noteList){
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)

                    }
                }

                noteAdapter.setData(tempArr)
                return true
            }

        })


    }

    private fun getUserProfile(id: Int) {
        userViewModel.getUserById(id).observe(this){
            setUpUserData(it!!)
        }
    }

    private fun setUpUserData(it: User) {

        if(it.user_profile_img == "null" || it.user_profile_img ==""){
            setUpDefaultProfile()
        }else{
            img_profile.load(Uri.parse(it.user_profile_img)){
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }


    }

    private fun getNotes() {

            viewModel.getAllNotes(id).observe(this@MainActivity){// fragment if activity -> List
                if(it.isNotEmpty()) {
                    Log.i("Data", "getNotes: ${it}")
                    noteList = it as ArrayList<Notes>
                    noteAdapter.setData(noteList)
                }

        }

    }

    private fun setUpDefaultProfile(){
        img_profile.load(R.drawable.femaleprofile){
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }



}