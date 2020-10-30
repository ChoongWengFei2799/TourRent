package com.example.tourrent.booking

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Schedule
import com.example.tourrent.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ScheduleRecycleAdapter(
private val scheduleList: ArrayList<Schedule>,
private val key: String
) :  RecyclerView.Adapter<ScheduleRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val loc: TextView = itemView.findViewById(R.id.loc)
        val delete: ImageView = itemView.findViewById(R.id.imageView16)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = scheduleList[position].date
        holder.time.text = scheduleList[position].time
        holder.loc.text = scheduleList[position].location

        holder.delete.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            rootRef.child("Schedule").orderByChild("bookingId").equalTo(key).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.children.forEach{ p0->
                                        val sch = p0.getValue(Schedule::class.java)
                                        if(sch != null){
                                            if(sch.time == scheduleList[position].time && sch.date == scheduleList[position].date){
                                                p0.ref.removeValue()
                                                Toast.makeText(it.context,"Schedule Removed", Toast.LENGTH_SHORT).show()

                                                scheduleList.remove(scheduleList[position])
                                                notifyDataSetChanged()
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                }
                            })
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            Toast.makeText(it.context, "Schedule Not Removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure to Remove Schedule?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }
    }
}