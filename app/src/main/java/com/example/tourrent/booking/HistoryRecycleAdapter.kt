package com.example.tourrent.booking

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryRecycleAdapter(
    private val bookingList: ArrayList<Booking>,
    private val key: ArrayList<String>
) :  RecyclerView.Adapter<HistoryRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.Name)
        val location: TextView = itemView.findViewById(R.id.Location)
        val date: TextView = itemView.findViewById(R.id.date)
        val type: TextView = itemView.findViewById(R.id.type)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prefs = holder.itemView.context.getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")
        if(mode == "T") {
            rootRef.child("Guide").child(bookingList[position].guide!!)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val prof = dataSnapshot.getValue(Guide::class.java)

                            holder.name.text = prof!!.name
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        }
        else{
            rootRef.child("Profile").child(bookingList[position].guide!!)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val prof = dataSnapshot.getValue(Profile::class.java)

                            holder.name.text = prof!!.name
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        }

        holder.location.text = bookingList[position].location
        holder.date.text = "${bookingList[position].startDate} - ${bookingList[position].endDate}"

        if(bookingList[position].type!!.contains("V")){
            holder.type.text = "Virtual"
        }
        else{
            holder.type.text = "Physical"
        }

        if(bookingList[position].type!!.contains("P")) {
            holder.status.text = "Rejected"
        }
        else if(bookingList[position].type!!.contains("R")) {
            holder.status.text = "Rejected"
            holder.status.setBackgroundResource(R.drawable.round_button_primary)
            holder.status.setTextColor(Color.parseColor("#D81B60"))
            holder.status.setPadding(35,3,35,6)
        }
        else if(bookingList[position].type!!.contains("C")) {
            holder.status.text = "Cancelled"
            holder.status.setBackgroundResource(R.drawable.round_button_primary)
            holder.status.setTextColor(Color.parseColor("#D81B60"))
            holder.status.setPadding(25,3,25,3)
        }
        else{
            holder.status.text = "Ended"
        }

        holder.itemView.setOnClickListener {
            if(bookingList[position].type!!.contains("P")||bookingList[position].type!!.contains("C")||bookingList[position].type!!.contains("R")){
                val action = booking_historyDirections.actionBookingHistoryToTourRemarks(key[position])
                it.findNavController().navigate(action)
            }
            else{
                val action = booking_historyDirections.actionBookingHistoryToHistory2(key[position])
                it.findNavController().navigate(action)
            }
        }
    }
}