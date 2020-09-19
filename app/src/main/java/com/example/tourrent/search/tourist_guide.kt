package com.example.tourrent.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristGuideBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_tourist_guide.*

class tourist_guide : Fragment() {

    private val args: tourist_guideArgs by navArgs()
    private val storageRef = FirebaseStorage.getInstance().reference
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristGuideBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_tourist_guide,
        container,
        false
    )
        val key = args.key

        setup(key)

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.review.setOnClickListener {
            val action = tourist_guideDirections.actionTouristGuideToGuideReview(key)
            view?.findNavController()?.navigate(action)
        }

        binding.book.setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
            val mode = prefs.getString("Mode","")

            if(mode != "T"){
                Toast.makeText(activity, "Please Login To Continue", Toast.LENGTH_SHORT).show()
            }
            else {
                val action = tourist_guideDirections.actionTouristGuideToTourPersonalise(
                    key,
                    binding.location.text.toString()
                )
                it.findNavController().navigate(action)
            }
        }

        return binding.root
    }

    private fun setup(key: String){
        storageRef.child(key).downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(usericon)
        }

        var lantext = ""
        rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val prof = dataSnapshot.getValue(Guide::class.java)

                    if(prof!!.tags!!.contains("A")){
                        lantext += "English, "
                    }
                    if(prof.tags!!.contains("B")){
                        lantext += "Malay, "
                    }
                    if(prof.tags!!.contains("C")){
                        lantext += "Chinese, "
                    }
                    if(prof.tags!!.contains("D")){
                        lantext += "Hindi, "
                    }
                    if(prof.tags!!.contains("E")){
                        lantext += "Spanish, "
                    }
                    if(prof.tags!!.contains("F")){
                        lantext += "French, "
                    }
                    if(prof.tags!!.contains("G")){
                        lantext += "Arabic, "
                    }
                    if(prof.tags!!.contains("H")){
                        lantext += "Russian, "
                    }
                    if(prof.tags!!.contains("I")){
                        lantext += "Thai, "
                    }
                    if(prof.tags!!.contains("J")){
                        lantext += "Japanese, "
                    }
                    langtext.text = lantext.dropLast(2)

                    var tagtext = ""
                    if(prof.tags!!.contains("1")){
                        tagtext += "Hiking, "
                    }
                    if(prof.tags!!.contains("2")){
                        tagtext += "Cycling, "
                    }
                    if(prof.tags!!.contains("3")){
                        tagtext += "Horse-Riding, "
                    }
                    if(prof.tags!!.contains("4")){
                        tagtext += "Spelunking, "
                    }
                    if(prof.tags!!.contains("5")){
                        tagtext += "Sailing, "
                    }
                    if(prof.tags!!.contains("6")){
                        tagtext += "Diving, "
                    }

                    if(tagtext == ""){
                        tagtext += "None"
                        stagtext.text = tagtext
                    }
                    else {
                        stagtext.text = tagtext.dropLast(2)
                    }

                    price.text = "RM${prof.price}/ Day"
                    location.text = prof.location
                    textView33.text = prof.name
                    biotext.text = prof.bio
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
