package com.techniquesmyanmar.greennotes.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techniquesmyanmar.greennotes.R

class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedColor = "#62bd69"

    private lateinit var layoutBottomSheet : LinearLayout
    private lateinit var layoutDeleteNote: LinearLayout
    private lateinit var layoutCopyNote: LinearLayout
    private lateinit var layoutImage: LinearLayout
    private lateinit var layoutWebUrl: LinearLayout
    private lateinit var layoutShareNote : LinearLayout

    private lateinit var fNote1: FrameLayout
    private lateinit var fNote2: FrameLayout
    private lateinit var fNote3: FrameLayout
    private lateinit var fNote4: FrameLayout
    private lateinit var fNote5: FrameLayout
    private lateinit var fNote6: FrameLayout
    private lateinit var fNote7: FrameLayout

    private lateinit var imgNote1: ImageView
    private lateinit var imgNote2: ImageView
    private lateinit var imgNote3: ImageView
    private lateinit var imgNote4: ImageView
    private lateinit var imgNote5: ImageView
    private lateinit var imgNote6: ImageView
    private lateinit var imgNote7: ImageView


    companion object {
        var noteId = 0
        fun newInstance(id: Int): NoteBottomSheetFragment {
            var args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            noteId = id
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet, null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        val behavior = param.behavior

        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    TODO("Not yet implemented")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }

                    }
                }

            })


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutBottomSheet = view.findViewById(R.id.layoutBottomSheet)
        layoutDeleteNote = view.findViewById(R.id.layoutDeleteNote)
        layoutImage = view.findViewById(R.id.layoutImage)
        layoutCopyNote = view.findViewById(R.id.layoutCopyNote)
        layoutWebUrl = view.findViewById(R.id.layoutWebUrl)
        layoutShareNote = view.findViewById(R.id.layoutShareNote)

        fNote1 = view.findViewById(R.id.fNote1)
        fNote2 = view.findViewById(R.id.fNote2)
        fNote3 = view.findViewById(R.id.fNote3)
        fNote4 = view.findViewById(R.id.fNote4)
        fNote5 = view.findViewById(R.id.fNote5)
        fNote6 = view.findViewById(R.id.fNote6)
        fNote7 = view.findViewById(R.id.fNote7)

        imgNote1 = view.findViewById(R.id.imgNote1)
        imgNote2 = view.findViewById(R.id.imgNote2)
        imgNote3 = view.findViewById(R.id.imgNote3)
        imgNote4 = view.findViewById(R.id.imgNote4)
        imgNote5 = view.findViewById(R.id.imgNote5)
        imgNote6 = view.findViewById(R.id.imgNote6)
        imgNote7 = view.findViewById(R.id.imgNote7)


        if (noteId != -1) {
            layoutDeleteNote.visibility = View.VISIBLE

        } else {
            layoutDeleteNote.visibility = View.GONE
        }
        setListener()
    }

    private fun setListener() {
        fNote1.setOnClickListener {

            imgNote1.setImageResource(R.drawable.ic_tick)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#8ed5ec"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Blue")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fNote2.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(R.drawable.ic_tick)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#c6a477"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Coffee")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fNote3.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(R.drawable.ic_tick)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#ffb294"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "MakeUp")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fNote4.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(R.drawable.ic_tick)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#62bd69"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Green")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fNote5.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(R.drawable.ic_tick)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#fe7a15"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Orange")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        fNote6.setOnClickListener {

            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(R.drawable.ic_tick)
            imgNote7.setImageResource(0)
            selectedColor = "#cdb3d4"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Purple")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        fNote7.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(R.drawable.ic_tick)
            selectedColor = "#ff9190"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Pink")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        layoutImage.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Image")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutWebUrl.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "WebUrl")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutCopyNote.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "CopyNote")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutShareNote.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","ShareNote")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutDeleteNote.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "DeleteNote")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }



    }
}