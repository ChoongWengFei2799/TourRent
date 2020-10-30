package com.example.tourrent.chatroom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Chatroom
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentChatroomListBinding
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class chatroom_list : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChatroomListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chatroom_list,
            container,
            false
        )

        linearLayoutManager = LinearLayoutManager(activity)
        binding.recycleView.layoutManager = linearLayoutManager

        binding.warning.visibility = View.GONE
        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        val mode = prefs.getString("Mode","")
        if(mode == "T") {
            val ref =
                FirebaseDatabase.getInstance().reference.child("Chatroom").orderByChild("tourist")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val chatroomList = ArrayList<Chatroom>()
                    p0.children.forEach {
                        val chatroom = it.getValue(Chatroom::class.java)
                        chatroomList.add(chatroom!!)
                    }

                    if(chatroomList.isNullOrEmpty()){
                        binding.warning.visibility = View.VISIBLE
                    }
                    else {
                        binding.warning.visibility = View.GONE
                        chatroomList.sortByDescending { it.lastupdate }

                        binding.recycleView.adapter = ChatroomRecycleAdapter(chatroomList, mode)
                        binding.recycleView.adapter?.notifyDataSetChanged()
                    }
                }
            })
        }
        else if(mode == "G"){
            val ref =
                FirebaseDatabase.getInstance().reference.child("Chatroom").orderByChild("guide")
                    .equalTo(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val chatroomList = ArrayList<Chatroom>()
                    p0.children.forEach {
                        val chatroom = it.getValue(Chatroom::class.java)
                        chatroomList.add(chatroom!!)
                    }

                    if(chatroomList.isNullOrEmpty()){
                        binding.warning.visibility = View.VISIBLE
                    }
                    else {
                        binding.warning.visibility = View.GONE
                        chatroomList.sortByDescending { it.lastupdate }

                        binding.recycleView.adapter = ChatroomRecycleAdapter(chatroomList, mode)
                        binding.recycleView.adapter?.notifyDataSetChanged()
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