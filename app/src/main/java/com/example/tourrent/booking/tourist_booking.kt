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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Schedule
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristBookingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tourist_booking.*
import java.text.SimpleDateFormat
import java.util.*

class tourist_booking : Fragment() {

    private val args: tourist_bookingArgs by navArgs()
    var rootRef = FirebaseDatabase.getInstance().reference
    var touristKey = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTouristBookingBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tourist_booking,
            container,
            false
        )

        val bkey = args.key

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.buttongen.layoutManager = layoutManager

        setup(bkey)

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")

        if(mode == "T"){
            binding.svt.visibility = View.INVISIBLE
        }
        else if(mode == "G"){
            binding.jvt.visibility = View.INVISIBLE
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.button2.setOnClickListener {
            val action = tourist_bookingDirections.actionTouristBookingToTourRemarks(bkey)
            view?.findNavController()?.navigate(action)
        }

        binding.contact.setOnClickListener {
            val action = tourist_bookingDirections.actionTouristBookingToTouristChatroom(null, touristKey)
            view?.findNavController()?.navigate(action)
        }

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

        binding.edit.setOnClickListener {
            val action = tourist_bookingDirections.actionTouristBookingToEditSchedule(bkey, binding.startdate.text.toString(), binding.enddate.text.toString())
            view?.findNavController()?.navigate(action)
        }

        binding.svt.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_tourist_booking_to_createLiveStream)
        }

        return binding.root
    }

    private fun setup(key: String){
        val sdf = SimpleDateFormat("d/M/yyyy")
        val dates = ArrayList<String>()
        val sch = ArrayList<Schedule>()
            rootRef.child("Booking").child(key).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        val booking = dataSnapshot.getValue(Booking::class.java)

                        if (booking != null) {
                            startdate.text = booking.startDate
                            textView41.text = booking.startDate
                            enddate.text = booking.endDate
                            touristKey = booking.tourist.toString()

                            if(!booking.type!!.contains("V")){
                                jvt.visibility = View.INVISIBLE
                                svt.visibility = View.INVISIBLE
                            }

                            val sdate= sdf.parse(booking.startDate.toString())
                            val edate = sdf.parse(booking.endDate.toString())

                            val c = Calendar.getInstance()
                            val c2 = Calendar.getInstance()
                            c.time = sdate
                            c2.time = edate

                            while(!c.after(c2) || c == c2) {
                                dates.add(sdf.format(c.timeInMillis))
                                c.add(Calendar.DATE, 1)
                            }

                            rootRef.child("Schedule").orderByChild("bookingId").equalTo(key).addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.children.forEach{
                                        val schedule = it.getValue(Schedule::class.java)
                                        if (schedule != null) {
                                            sch.add(schedule)
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                }
                            })

                            buttongen.adapter = DateRecycleAdapter(this@tourist_booking, dates, sch)

                            val daily = ArrayList<Schedule>()
                            sch.forEach{
                                if(it.date == startdate.text){
                                    daily.add(it)
                                }
                            }
                            if(daily.isNullOrEmpty()){
                                textView35.text = "No Schedule Plan On this Day"
                            }
                            else{
                                daily.sortBy {it.time}
                                var newtext = ""
                                daily.forEach{
                                    newtext += textView35.text.toString() + "${it.time} - ${it.location}\n"
                                }
                                textView35.text = newtext
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
    }
}
