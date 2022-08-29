package com.techniquesmyanmar.greennotes.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techniquesmyanmar.greennotes.databinding.NoteItemListBinding
import com.techniquesmyanmar.greennotes.entity.Notes

class NoteAdapter(
    private var noteList : ArrayList<Notes>

): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    var listener:OnItemClickListener? = null

    fun setData(arrNotesList: List<Notes>){
        noteList = arrNotesList as ArrayList<Notes>
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    inner class NoteHolder(private val binding: NoteItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Notes) {
            binding.tvTitle.text = note.title
            binding.tvDesc.text = note.noteText
            binding.tvDateTime.text = note.dateTime
            binding.tvWebLink.text = note.webLink

            binding.cardView.setCardBackgroundColor(Color.parseColor(noteList[position].color))

            if (noteList[position].imagePath != ""){
                binding.imgNote.setImageURI(Uri.parse(noteList[position].imagePath))
                binding.imgNote.visibility = View.VISIBLE
            }else{
                binding.imgNote.visibility = View.GONE
            }

            if (noteList[position].webLink != ""){
                binding.tvWebLink.text = noteList[position].webLink
                binding.tvWebLink.visibility = View.VISIBLE
            }else{
                binding.tvWebLink.visibility = View.GONE
            }

            binding.cardView.setOnClickListener {
                listener!!.onClicked(noteList[position].id!!,noteList[position].color)

            }
//this is written by KPSA for test
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            NoteItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    interface OnItemClickListener{
        fun onClicked(noteId:Int,color:String)
    }

}

