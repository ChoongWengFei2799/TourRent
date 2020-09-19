package com.example.tourrent.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Chatroom
import com.example.tourrent.R

class ChatroomRecycleAdapter (
    private val chatroomList: ArrayList<Chatroom>,
    private val key: ArrayList<String>,
    private val mode: String
    ) :  RecyclerView.Adapter<ChatroomRecycleAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.Name)
            val last: TextView = itemView.findViewById(R.id.lasttime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.chatroom_list_item_row, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return chatroomList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(mode == "T") {
                holder.name.text = chatroomList[position].guide
            }
            else{
                holder.name.text = chatroomList[position].tourist
            }

            holder.last.text = chatroomList[position].lastupdate

            holder.itemView.setOnClickListener {

            }
        }
    }