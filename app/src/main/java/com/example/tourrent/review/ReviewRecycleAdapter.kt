package com.example.tourrent.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.Dclass.Review
import com.example.tourrent.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRecycleAdapter(
    private val reviewList: ArrayList<Review>
) :  RecyclerView.Adapter<ReviewRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.gname)
        val feedback: TextView = itemView.findViewById(R.id.feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.review_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        rootRef.child("Profile").child(reviewList[position].tourist!!)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val prof = dataSnapshot.getValue(Profile::class.java)

                        holder.name.text = prof!!.name!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })

        holder.feedback.text = reviewList[position].feedback
    }
}