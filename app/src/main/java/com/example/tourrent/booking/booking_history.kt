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
import com.example.tourrent.databinding.FragmentBookingHistoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class booking_history : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBookingHistoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_booking_history,
            container,
            false
        )

        linearLayoutManager = LinearLayoutManager(activity)
        binding.historyView.layoutManager = linearLayoutManager

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        val mode = prefs.getString("Mode","")
        val format = SimpleDateFormat("dd/MM/yyyy")

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if(mode == "T") {
            val ref =
                FirebaseDatabase.getInstance().reference.child("Booking").orderByChild("tourist")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val bookingList = ArrayList<Booking>()
                    val bookingKey = ArrayList<String>()
                    val date = format.parse(format.format(Date()))

                    p0.children.forEach {
                        val book = it.getValue(Booking::class.java)
                        if (book != null) {
                            if (format.parse(book.endDate.toString()).before(date)) {
                                bookingList.add(book)
                                bookingKey.add(it.key!!)
                            }
                        }
                    }

                    if (bookingList.isNullOrEmpty()) {
                        binding.warning.visibility = View.VISIBLE
                    } else {
                        binding.warning.visibility = View.GONE
                        binding.historyView.adapter?.notifyDataSetChanged()
                        binding.historyView.adapter = HistoryRecycleAdapter(bookingList, bookingKey)
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
                    val bookingList = ArrayList<Booking>()
                    val bookingKey = ArrayList<String>()
                    val date = format.parse(format.format(Date()))

                    p0.children.forEach {
                        val book = it.getValue(Booking::class.java)
                        if (book != null) {
                            if (format.parse(book.endDate.toString()).before(date)) {
                                bookingList.add(book)
                                bookingKey.add(it.key!!)
                            }
                        }
                    }

                    if (bookingList.isNullOrEmpty()) {
                        binding.warning.visibility = View.VISIBLE
                    } else {
                        binding.warning.visibility = View.GONE
                        binding.historyView.adapter?.notifyDataSetChanged()
                        binding.historyView.adapter = HistoryRecycleAdapter(bookingList, bookingKey)
                    }
                }
            })
        }
        else{
            binding.warning.visibility = View.VISIBLE
        }

        binding.historyView.layoutManager = linearLayoutManager

        return binding.root
    }
}