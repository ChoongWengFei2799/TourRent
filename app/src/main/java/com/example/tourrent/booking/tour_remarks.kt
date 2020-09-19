package com.example.tourrent.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Personalize
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTourRemarksBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tour_remarks.*

class tour_remarks : Fragment() {

    private val args: tour_remarksArgs by navArgs()
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTourRemarksBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tour_remarks,
            container,
            false
        )

        val bkey = args.key

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        rootRef.child("Booking").child(bkey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val booking = dataSnapshot.getValue(Booking::class.java)

                    if (booking != null) {
                        if(booking.type.toString().contains("C")){
                            Toast.makeText(activity, "Offer Cancelled", Toast.LENGTH_LONG).show()
                            activity!!.onBackPressed()
                        }

                        location.text = booking.location
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        rootRef.child("Personalize").child(bkey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val perso = dataSnapshot.getValue(Personalize::class.java)

                    if (perso != null) {
                        pax.text = perso.pax
                        interest.setText(perso.todo)
                        fav.setText(perso.interest)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        return binding.root
    }

}