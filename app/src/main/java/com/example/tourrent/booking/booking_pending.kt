package com.example.tourrent.booking

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Personalize
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentBookingPendingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_booking_pending.*

class booking_pending : Fragment() {

    private val args: booking_pendingArgs by navArgs()
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBookingPendingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_booking_pending,
            container,
            false
        )

        val bkey = args.key

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if(mode == "T"){
            binding.accept.visibility = View.INVISIBLE
            binding.reject.visibility = View.INVISIBLE
            binding.cancel.visibility = View.VISIBLE

            binding.cancel.setOnClickListener {
                rootRef.child("Booking").child(bkey).child("type").setValue("C")
                Toast.makeText(activity, "Offer Cancelled", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }
        else if(mode == "G"){
            binding.accept.visibility = View.VISIBLE
            binding.reject.visibility = View.VISIBLE
            binding.cancel.visibility = View.GONE

            binding.accept.setOnClickListener {
                rootRef.child("Booking").child(bkey).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()) {
                            val booking = dataSnapshot.getValue(Booking::class.java)

                            if (booking != null) {
                                rootRef.child("Booking").child(bkey).child("type").setValue(booking.type!!.drop(1))
                                val action = booking_pendingDirections.actionBookingPendingToTouristBooking(bkey)
                                view?.findNavController()?.popBackStack(R.id.booking_pending, true)
                                view?.findNavController()?.navigate(action)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }

            binding.reject.setOnClickListener {
                rootRef.child("Booking").child(bkey).child("type").setValue("R")
                Toast.makeText(activity, "Offer Rejected", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }

        if (mode != null) {
            setup(bkey, mode)
        }

        return binding.root
    }

    private fun setup(bkey: String, mode: String){
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

                        if(booking.type.toString().contains("R")){
                            Toast.makeText(activity, "Offer Rejected", Toast.LENGTH_LONG).show()
                            activity!!.onBackPressed()
                        }

                        if(!booking.type.toString().contains("P")){
                            Toast.makeText(activity, "Offer Accepted", Toast.LENGTH_LONG).show()
                            val action = booking_pendingDirections.actionBookingPendingToTouristBooking(bkey)
                            view?.findNavController()?.popBackStack(R.id.booking_pending, true)
                            view?.findNavController()?.navigate(action)
                        }

                        startdate.text = booking.startDate
                        enddate.text = booking.endDate
                        location.text = booking.location

                        if(booking.type.toString().contains("V")){
                            switch1.isChecked
                        }

                        if(mode == "T"){
                            rootRef.child("Guide").child(booking.guide.toString()).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        val guide = dataSnapshot.getValue(Guide::class.java)
                                        if (guide != null) {
                                            name.text = guide.name
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                }
                            })
                        }
                        else if(mode == "G"){
                            rootRef.child("Profile").child(booking.tourist.toString()).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        val prof = dataSnapshot.getValue(Profile::class.java)
                                        if (prof != null) {
                                            name.text = prof.name
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                }
                            })
                        }
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
    }
}