package com.example.tourrent.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Schedule
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentScheduleBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class edit_schedule : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private val args: edit_scheduleArgs by navArgs()
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScheduleBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_schedule,
        container,
        false
    )
        val bkey = args.key

        linearLayoutManager = LinearLayoutManager(activity)
        binding.scheduleView.layoutManager = linearLayoutManager
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val sch = ArrayList<Schedule>()
        rootRef.child("Schedule").orderByChild("bookingId").equalTo(bkey).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach{
                    val schedule = it.getValue(Schedule::class.java)
                    if (schedule != null) {
                        sch.add(schedule)
                    }
                }

                if(sch.isNullOrEmpty()){
                    binding.text123.text = "No Schedule Currently"
                }
                else{
                    val nsch = sch.sortedWith(compareBy({it.date}, {it.time}))
                    binding.scheduleView.adapter = ScheduleRecycleAdapter(nsch.toCollection(ArrayList()), bkey)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        binding.schscroll.setOnClickListener {
            if(binding.addcard.visibility == View.GONE) {
                binding.addcard.visibility = View.VISIBLE
            }
            else{
                binding.date.setText("")
                binding.time.setText("")
                binding.location.setText("")
                binding.addcard.visibility = View.GONE
            }
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val sdf = SimpleDateFormat("d/M/yyyy")

            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                binding.date.setText("$dayOfMonth/${monthOfYear+1}/$year")

            }, year, month, day)

            val sdate= sdf.parse(args.sDate)
            val edate = sdf.parse(args.eDate)

            val c1 = Calendar.getInstance()
            val c2 = Calendar.getInstance()
            c1.time = sdate
            c2.time = edate

            dpd.datePicker.minDate = c1.timeInMillis
            dpd.datePicker.maxDate = c2.timeInMillis
            dpd.show()
        }

        binding.time.setOnClickListener {
            val c = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireActivity(), android.R.style.Theme_Holo_Light_NoActionBar, OnTimeSetListener { _, hourOfDay, minute ->

                var newhour = "$hourOfDay"
                var newmin = "$minute"

                if(hourOfDay <= 9){
                    newhour = "0$hourOfDay"
                }
                if(minute <= 9){
                    newmin = "0$minute"
                }

                binding.time.setText("$newhour:$newmin")

            }, mHour, mMinute, true)

            timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.show()
        }

        binding.add.setOnClickListener {
            if(binding.date.text.toString() == "" || binding.time.text.toString() == "" || binding.location.text.toString() == "")   {
                Toast.makeText(activity,"Please Fill In All Fields", Toast.LENGTH_SHORT).show()
            }
            else{
                var check = true
                rootRef.child("Schedule").orderByChild("bookingId").equalTo(bkey).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach{
                            val sch = it.getValue(Schedule::class.java)
                            if(sch != null){
                                if(sch.date!! == binding.date.text.toString() && sch.time!! == binding.time.text.toString()){
                                    check = false
                                }
                            }
                        }

                        if(!check){
                            Toast.makeText(activity,"Time Slot Occupied", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val id = rootRef.child("Schedule").push().key
                            val nsch = Schedule(binding.date.text.toString(), binding.time.text.toString(), binding.location.text.toString(), bkey)
                            if (id != null) {
                                rootRef.child("Schedule").child(id).setValue(nsch)
                                Toast.makeText(activity,"Schedule Added", Toast.LENGTH_SHORT).show()
                                val sch = ArrayList<Schedule>()
                                rootRef.child("Schedule").orderByChild("bookingId").equalTo(bkey).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        dataSnapshot.children.forEach{
                                            val schedule = it.getValue(Schedule::class.java)
                                            if (schedule != null) {
                                                sch.add(schedule)
                                            }
                                        }

                                        if(sch.isNullOrEmpty()){
                                            binding.text123.text = "No Schedule Currently"
                                        }
                                        else{
                                            val nsch = sch.sortedWith(compareBy({it.date}, {it.time}))
                                            binding.scheduleView.adapter = ScheduleRecycleAdapter(nsch.toCollection(ArrayList()), bkey)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Failed to read value
                                    }
                                })
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }
        }

        return binding.root
    }

}