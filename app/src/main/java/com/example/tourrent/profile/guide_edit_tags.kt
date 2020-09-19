package com.example.tourrent.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideEditTagsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_guide_edit_tags.*
import kotlinx.android.synthetic.main.fragment_guide_edit_tags.cycle
import kotlinx.android.synthetic.main.fragment_tourist_search.*

/**
 * A simple [Fragment] subclass.
 */
class guide_edit_tags : Fragment() {

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideEditTagsBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_edit_tags,
                container,
                false
        )

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key","")

        if (key != null) {
            calculateButton(key)

            binding.Hiking.setOnClickListener {
                addTag(key, "1")
                Hiking.visibility = View.INVISIBLE
                Nohiking.visibility = View.VISIBLE
            }
            binding.cycle.setOnClickListener {
                addTag(key, "2")
                cycle.visibility = View.INVISIBLE
                Nocycle.visibility = View.VISIBLE
            }
            binding.Horse.setOnClickListener {
                addTag(key, "3")
                Horse.visibility = View.INVISIBLE
                Nohorse.visibility = View.VISIBLE
            }
            binding.Cave.setOnClickListener {
                addTag(key, "4")
                Cave.visibility = View.INVISIBLE
                Nocave.visibility = View.VISIBLE
            }
            binding.Sail.setOnClickListener {
                addTag(key, "5")
                Sail.visibility = View.INVISIBLE
                Nosail.visibility = View.VISIBLE
            }
            binding.Dive.setOnClickListener {
                addTag(key, "6")
                Dive.visibility = View.INVISIBLE
                Nodive.visibility = View.VISIBLE
            }


            binding.Nohiking.setOnClickListener {
                removeTag(key, "1")
                Hiking.visibility = View.VISIBLE
                Nohiking.visibility = View.INVISIBLE
            }
            binding.Nocycle.setOnClickListener {
                removeTag(key, "2")
                cycle.visibility = View.VISIBLE
                Nocycle.visibility = View.INVISIBLE
            }
            binding.Nohorse.setOnClickListener {
                removeTag(key, "3")
                Horse.visibility = View.VISIBLE
                Nohorse.visibility = View.INVISIBLE
            }
            binding.Nocave.setOnClickListener {
                removeTag(key, "4")
                Cave.visibility = View.VISIBLE
                Nocave.visibility = View.INVISIBLE
            }
            binding.Nosail.setOnClickListener {
                removeTag(key, "5")
                Sail.visibility = View.VISIBLE
                Nosail.visibility = View.INVISIBLE
            }
            binding.Nodive.setOnClickListener {
                removeTag(key, "6")
                Dive.visibility = View.VISIBLE
                Nodive.visibility = View.INVISIBLE
            }
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun addTag(Key: String, Code: String){
        rootRef.child("Guide").child(Key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        var newtag = guide.tags.toString() + Code
                        newtag = newtag.toCharArray().sorted().joinToString("")
                        updateTag(Key, newtag)
                    }
                } else {
                    Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun removeTag(Key: String, Code: String){
        rootRef.child("Guide").child(Key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        var newtag = guide.tags.toString()
                        newtag = newtag.replace(Code, "")
                        updateTag(Key, newtag)
                    }
                } else {
                    Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun updateTag(Key: String, newtag: String){
        rootRef.child("Guide").child(Key).child("tags").setValue(newtag)
    }

    private fun calculateButton(key: String){
        rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        setButton(guide.tags.toString())
                    }
                } else {
                    Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun setButton(tags: String){
        if(tags.contains("1")){
            Hiking.visibility = View.INVISIBLE
            Nohiking.visibility = View.VISIBLE
        }
        else{
            Hiking.visibility = View.VISIBLE
            Nohiking.visibility = View.INVISIBLE
        }
        if(tags.contains("2")){
            cycle.visibility = View.INVISIBLE
            Nocycle.visibility = View.VISIBLE
        }
        else{
            cycle.visibility = View.VISIBLE
            Nocycle.visibility = View.INVISIBLE
        }
        if(tags.contains("3")){
            Horse.visibility = View.INVISIBLE
            Nohorse.visibility = View.VISIBLE
        }
        else{
            Horse.visibility = View.VISIBLE
            Nohorse.visibility = View.INVISIBLE
        }
        if(tags.contains("4")){
            Cave.visibility = View.INVISIBLE
            Nocave.visibility = View.VISIBLE
        }
        else{
            Cave.visibility = View.VISIBLE
            Nocave.visibility = View.INVISIBLE
        }
        if(tags.contains("5")){
            Sail.visibility = View.INVISIBLE
            Nosail.visibility = View.VISIBLE
        }
        else{
            Sail.visibility = View.VISIBLE
            Nosail.visibility = View.INVISIBLE
        }
        if(tags.contains("6")){
            Dive.visibility = View.INVISIBLE
            Nodive.visibility = View.VISIBLE
        }
        else{
            Dive.visibility = View.VISIBLE
            Nodive.visibility = View.INVISIBLE
        }
    }
}
