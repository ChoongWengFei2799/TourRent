package com.example.tourrent.booking

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
        val mode = prefs.getString("Mode", "")

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if(mode == "T"){
            binding.accept.visibility = View.INVISIBLE
            binding.reject.visibility = View.INVISIBLE
            binding.cancel.visibility = View.VISIBLE

            binding.cancel.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                rootRef.child("Booking").child(bkey).child("type").setValue("C")
                                Toast.makeText(activity, "Booking Cancelled", Toast.LENGTH_SHORT).show()
                                requireActivity().onBackPressed()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                Toast.makeText(activity, "Booking Not Cancelled", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure to Cancel Booking?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }
        }
        else if(mode == "G"){
            binding.accept.visibility = View.VISIBLE
            binding.reject.visibility = View.VISIBLE
            binding.cancel.visibility = View.GONE

            binding.accept.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                rootRef.child("Booking").child(bkey).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val booking = dataSnapshot.getValue(Booking::class.java)

                                            if (booking != null) {
                                                rootRef.child("Booking").child(bkey).child("type").setValue(
                                                    booking.type!!.dropLast(
                                                        1
                                                    )
                                                )
                                                val action =
                                                    booking_listDirections.actionBookingListToTouristBooking(
                                                        bkey
                                                    )
                                                Toast.makeText(activity, "Booking Accepted", Toast.LENGTH_SHORT).show()
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
                            DialogInterface.BUTTON_NEGATIVE -> {
                                Toast.makeText(activity, "Booking Not Accepted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure to Accept Booking?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }

            binding.reject.setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                rootRef.child("Booking").child(bkey).child("type").setValue("R")
                                Toast.makeText(activity, "Booking Rejected", Toast.LENGTH_SHORT).show()
                                requireActivity().onBackPressed()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                Toast.makeText(activity, "Booking Not Rejected", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure to Reject Booking?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
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
                if (dataSnapshot.exists()) {
                    val booking = dataSnapshot.getValue(Booking::class.java)

                    if (booking != null) {
                        startdate.text = booking.startDate
                        enddate.text = booking.endDate
                        location.text = booking.location

                        if (booking.type.toString().contains("V")) {
                            switch1.isChecked = true
                        }

                        if (mode == "T") {
                            rootRef.child("Guide").child(booking.guide.toString())
                                .addListenerForSingleValueEvent(
                                    object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {
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
                        } else if (mode == "G") {
                            rootRef.child("Profile").child(booking.tourist.toString())
                                .addListenerForSingleValueEvent(
                                    object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                val prof =
                                                    dataSnapshot.getValue(Profile::class.java)
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
                if (dataSnapshot.exists()) {
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