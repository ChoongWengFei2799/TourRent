package com.example.tourrent.review

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.*
import com.example.tourrent.R
import com.example.tourrent.booking.BookingRecycleAdapter
import com.example.tourrent.databinding.FragmentGuideReviewBinding
import com.example.tourrent.search.SearchRecycleAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_guide_profile.*
import java.util.*
import kotlin.collections.ArrayList

class guide_review : Fragment() {

    private val args: guide_reviewArgs by navArgs()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var reviewK: String

    var rootRef = FirebaseDatabase.getInstance().reference
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Review")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideReviewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guide_review,
            container,
            false
        )

        val gkey = args.gkey

        linearLayoutManager = LinearLayoutManager(activity)
        binding.review.layoutManager = linearLayoutManager

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        val mode = prefs.getString("Mode","")

        if(mode == "T") {
            var check = false
            val ref =
                FirebaseDatabase.getInstance().reference.child("Review").orderByChild("tourist")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach{
                        val rev = it.getValue(Review::class.java)
                        if (rev != null) {
                            if(rev.guide == gkey){
                                binding.reviewt.setText(rev.feedback.toString())
                                reviewK = it.key.toString()
                                check = true
                            }
                        }
                    }

                    if(!check){
                        binding.text.text = "Write A Review"
                        binding.Redit.visibility = View.GONE
                        binding.save.visibility = View.VISIBLE
                        binding.save.text = "Submit"
                        binding.reviewt.isEnabled = true
                    }
                }
            })

            binding.Redit.setOnClickListener {
                val unchange = binding.reviewt.text.toString()
                binding.delete.visibility = View.VISIBLE
                binding.save.visibility = View.VISIBLE
                binding.Redit.visibility = View.INVISIBLE
                binding.Rcancer.visibility = View.VISIBLE
                binding.reviewt.isEnabled = true
                binding.reviewt.isFocusable = true
                binding.reviewt.requestFocus()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.reviewt, InputMethodManager.SHOW_IMPLICIT)

                binding.Rcancer.setOnClickListener {
                    binding.reviewt.setText(unchange)
                    binding.delete.visibility = View.GONE
                    binding.save.visibility = View.GONE
                    binding.Redit.visibility = View.VISIBLE
                    binding.Rcancer.visibility = View.INVISIBLE
                    binding.reviewt.isEnabled = false
                    binding.reviewt.isFocusable = false
                    binding.review.clearFocus()
                }
            }

            binding.delete.setOnClickListener {
                val ref = FirebaseDatabase.getInstance().reference.child("Review").child(reviewK)
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        p0.ref.removeValue()
                        Toast.makeText(activity, "Review Deleted", Toast.LENGTH_SHORT).show()

                        binding.text.text = "Write A Review"
                        binding.Redit.visibility = View.INVISIBLE
                        binding.Rcancer.visibility = View.INVISIBLE
                        binding.save.visibility = View.VISIBLE
                        binding.save.text = "Submit"
                        binding.reviewt.setText("")
                        binding.reviewt.clearFocus()
                        binding.reviewt.isEnabled = true
                    }
                })
            }

            binding.save.setOnClickListener {
                if(binding.reviewt.text.toString() != "") {
                    if (!check) {
                        val ref =
                            FirebaseDatabase.getInstance().reference.child("Booking")
                                .orderByChild("tourist")
                                .equalTo(key)
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                var addcheck = false
                                p0.children.forEach {
                                    val book = it.getValue(Booking::class.java)
                                    if (book != null) {
                                        if (book.guide == gkey) {
                                            addcheck = true
                                        }
                                    }
                                }

                                if (addcheck) {
                                    val rkey = myRef.push().key
                                    if (rkey != null) {
                                        val newReview =
                                            Review(key, gkey, binding.reviewt.text.toString())
                                        myRef.child(rkey).setValue(newReview)
                                        Toast.makeText(activity, "Review Added", Toast.LENGTH_SHORT)
                                            .show()

                                        binding.reviewt.isEnabled = false
                                        binding.text.text = "Your Review"
                                        binding.Redit.visibility = View.VISIBLE
                                        binding.save.visibility = View.GONE
                                        binding.save.text = "Save"
                                    }
                                } else {
                                    Toast.makeText(activity, "No Booking Found", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        })
                    }
                    else {
                        myRef.child(reviewK).child("feedback").setValue(binding.reviewt.text.toString())

                        Toast.makeText(activity, "Update Success", Toast.LENGTH_SHORT).show()

                        binding.reviewt.clearFocus()
                        binding.reviewt.isEnabled = false
                        binding.Redit.visibility = View.VISIBLE
                        binding.Rcancer.visibility = View.INVISIBLE
                        binding.save.visibility = View.GONE
                        binding.delete.visibility = View.GONE
                    }
                }
                else{
                    Toast.makeText(activity, "Review is Empty", Toast.LENGTH_SHORT).show()
                }
            }

        }
        else{
            binding.log.visibility = View.GONE
        }

        val ref = FirebaseDatabase.getInstance().reference.child("Review").orderByChild("guide").equalTo(gkey)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                val reviewList = ArrayList<Review>()
                p0.children.forEach {
                    val rev = it.getValue(Review::class.java)
                    if (rev != null) {
                        if(rev.tourist != key) {
                            reviewList.add(rev)
                        }
                    }
                }

                if(reviewList.isNullOrEmpty()){
                    binding.warning.visibility = View.VISIBLE
                }
                else {
                    binding.warning.visibility = View.GONE

                    binding.review.adapter = ReviewRecycleAdapter(reviewList)
                }
            }
        })

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }
}