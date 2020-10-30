package com.example.tourrent.booking

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentBookingListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class booking_list : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager2: LinearLayoutManager
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBookingListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_booking_list,
            container,
            false
        )

        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager2 = LinearLayoutManager(activity)
        binding.pending.layoutManager = linearLayoutManager
        binding.ongoing.layoutManager = linearLayoutManager2

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        val mode = prefs.getString("Mode","")
        val format = SimpleDateFormat("dd/MM/yyyy")

        if(mode == "T") {
            val ref =
                FirebaseDatabase.getInstance().reference.child("Booking").orderByChild("tourist")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val pendingList = ArrayList<Booking>()
                    val pendingKey = ArrayList<String>()
                    val bookingList = ArrayList<Booking>()
                    val bookingKey = ArrayList<String>()
                    val date = format.parse(format.format(Date()))

                    p0.children.forEach {
                        val book = it.getValue(Booking::class.java)
                        if (book != null) {
                            if (format.parse(book.startDate.toString()).before(date) || date.toString() == book.startDate.toString()) {
                                if (book.type!!.contains("P")) {
                                    val newType = book.type!!.replace("P", "R")
                                    rootRef.child("Booking").child(it.key!!).child("type")
                                        .setValue(newType)
                                }
                            }
                            if (format.parse(book.endDate.toString()).after(date)) {
                                if (book.type!!.contains("P")) {
                                    pendingList.add(book)
                                    pendingKey.add(it.key!!)
                                } else {
                                    bookingList.add(book)
                                    bookingKey.add(it.key!!)
                                }
                            }
                        }
                    }

                    if (bookingList.isNullOrEmpty() && pendingList.isNullOrEmpty()) {
                        binding.warning.visibility = View.VISIBLE
                    } else {
                        binding.warning.visibility = View.GONE

                        binding.pending.adapter = BookingRecycleAdapter(pendingList, pendingKey)
                        binding.ongoing.adapter = BookingRecycleAdapter(bookingList, bookingKey)

                        (binding.pending.adapter as BookingRecycleAdapter).notifyDataSetChanged()
                        (binding.ongoing.adapter as BookingRecycleAdapter).notifyDataSetChanged()
                    }
                }
            })
        }
        else if (mode == "G"){
            val ref =
                FirebaseDatabase.getInstance().reference.child("Booking").orderByChild("guide")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val pendingList = ArrayList<Booking>()
                    val pendingKey = ArrayList<String>()
                    val bookingList = ArrayList<Booking>()
                    val bookingKey = ArrayList<String>()
                    val date = format.parse(format.format(Date()))

                    p0.children.forEach {
                        val book = it.getValue(Booking::class.java)
                        if (book != null) {
                            if (format.parse(book.startDate.toString()).before(date) || date.toString() == book.endDate.toString()) {
                                if (book.type!!.contains("P")) {
                                    val newType = book.type!!.replace("P", "R")
                                    rootRef.child("Booking").child(it.key!!).child("type")
                                        .setValue(newType)
                                }
                            }
                            if (format.parse(book.endDate.toString()).after(date)) {
                                if (book.type!!.contains("P")) {
                                    pendingList.add(book)
                                    pendingKey.add(it.key!!)
                                } else {
                                    bookingList.add(book)
                                    bookingKey.add(it.key!!)
                                }
                            }
                        }
                    }

                    if (bookingList.isNullOrEmpty() && pendingList.isNullOrEmpty()) {
                        binding.warning.visibility = View.VISIBLE
                    } else {
                        binding.warning.visibility = View.GONE

                        binding.pending.adapter = BookingRecycleAdapter(pendingList, pendingKey)
                        binding.ongoing.adapter = BookingRecycleAdapter(bookingList, bookingKey)

                        (binding.pending.adapter as BookingRecycleAdapter).notifyDataSetChanged()
                        (binding.ongoing.adapter as BookingRecycleAdapter).notifyDataSetChanged()
                    }
                }
            })
        }
        else{
            binding.warning.visibility = View.VISIBLE
            binding.textView37.visibility = View.VISIBLE
        }

        return binding.root
    }
}