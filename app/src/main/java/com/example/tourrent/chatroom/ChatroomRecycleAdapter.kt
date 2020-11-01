package com.example.tourrent.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Chatroom
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatroomRecycleAdapter (
    private val chatroomList: ArrayList<Chatroom>,
    private val mode: String
    ) :  RecyclerView.Adapter<ChatroomRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

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
                rootRef.child("Guide").child(chatroomList[position].guide!!)
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

                holder.itemView.setOnClickListener {
                    rootRef.child("Chatroom").orderByChild("tourist").equalTo(chatroomList[position].tourist).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach{
                                val chat = it.getValue(Chatroom::class.java)
                                if (chat != null) {
                                    if(chat.guide == chatroomList[position].guide){
                                        val chatkey = it.key
                                        val action = chatroom_listDirections.actionChatroomListToTouristChatroom(chatkey, chatroomList[position].guide)
                                        holder.itemView.findNavController().navigate(action)
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
            else{
                rootRef.child("Profile").child(chatroomList[position].tourist!!)
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

                holder.itemView.setOnClickListener {
                    rootRef.child("Chatroom").orderByChild("guide").equalTo(chatroomList[position].guide).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach{
                                val chat = it.getValue(Chatroom::class.java)
                                if (chat != null) {
                                    if(chat.guide == chatroomList[position].guide){
                                        val chatkey = it.key
                                        val action = chatroom_listDirections.actionChatroomListToTouristChatroom(chatkey, chatroomList[position].tourist)
                                        holder.itemView.findNavController().navigate(action)
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

            holder.last.text = chatroomList[position].lastupdate
        }
    }