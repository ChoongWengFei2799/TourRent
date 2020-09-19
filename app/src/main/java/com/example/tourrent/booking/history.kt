package com.example.tourrent.booking

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Schedule
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentHistoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_history.*
import java.text.SimpleDateFormat
import java.util.*

class history : Fragment() {

    private val args: historyArgs by navArgs()
    var rootRef = FirebaseDatabase.getInstance().reference
    private lateinit var gid : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history,
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
            binding.button2.visibility = View.VISIBLE
        }
        else if(mode == "G"){
            binding.button2.visibility = View.GONE
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.button2.setOnClickListener {
            val action = historyDirections.actionHistoryToGuideReview(gid)
            view?.findNavController()?.navigate(action)
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
                        gid = booking.guide.toString()

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

                        rootRef.child("Schedule").orderByChild("bookingId").equalTo(key).addListenerForSingleValueEvent(object :
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

                        buttongen.adapter = DateRecycleAdapter(this@history, dates, sch)

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