package com.example.tourrent.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Schedule
import com.example.tourrent.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_tourist_booking.*


class DateRecycleAdapter(
    private val frag: Fragment,
    private val dateList: ArrayList<String>,
    private val scheduleList: ArrayList<Schedule>
) :  RecyclerView.Adapter<DateRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.date_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = dateList[position]
        holder.itemView.setOnClickListener {
            frag.textView41.text = dateList[position]
            frag.textView35.text = ""
            val daily = java.util.ArrayList<Schedule>()
            scheduleList.forEach{
                if(it.date == frag.textView41.text){
                    daily.add(it)
                }
            }

            daily.sortBy { it.time }
            frag.textView35.text = ""
            var newtext = ""
            if(daily.isNullOrEmpty()){
                frag.textView35.text = "No Schedule Plan On this Day"
            }
            else{
                daily.forEach {
                    newtext = frag.textView35.text.toString() + "${it.time} - ${it.location}\n"
                }
                frag.textView35.text = newtext
            }
        }
    }
}