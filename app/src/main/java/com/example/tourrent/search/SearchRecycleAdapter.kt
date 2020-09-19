package com.example.tourrent.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.google.firebase.storage.FirebaseStorage

class SearchRecycleAdapter(
    private val commentList: ArrayList<Guide>,
    private val key: ArrayList<String>
) :  RecyclerView.Adapter<SearchRecycleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.Name)
        val location: TextView = itemView.findViewById(R.id.location)
        val price: TextView = itemView.findViewById(R.id.price)
        val pic: ImageView = itemView.findViewById(R.id.pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.search_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = commentList[position].name
        holder.location.text = commentList[position].location
        holder.price.text = "RM${commentList[position].price}/ Day"

        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(key[position]).downloadUrl.addOnSuccessListener {
                Glide.with(holder.itemView.context).load(it).into(holder.pic)
        }

        holder.itemView.setOnClickListener {
            val action = search_listDirections.actionSearchListToTouristGuide(key[position])
            it.findNavController().navigate(action)
        }
    }
}