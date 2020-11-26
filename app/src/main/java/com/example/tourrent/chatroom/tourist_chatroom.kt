package com.example.tourrent.chatroom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Chat
import com.example.tourrent.Dclass.Chatroom
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristChatroomBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class tourist_chatroom : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    var rootRef = FirebaseDatabase.getInstance().reference
    var chatRef = FirebaseDatabase.getInstance().getReference("Chat")
    var chatroomRef = FirebaseDatabase.getInstance().getReference("Chatroom")
    private val args: tourist_chatroomArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristChatroomBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tourist_chatroom,
            container,
            false
        )

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.chatview.layoutManager = linearLayoutManager.apply {
            stackFromEnd = true
        }

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        val mode = prefs.getString("Mode", "")

        var chatroomkey = args.roomID
        val guidekey = args.guideID

        if (guidekey != null) {
            if(mode == "T") {
                rootRef.child("Chatroom").orderByChild("tourist").equalTo(key)
                    .addListenerForSingleValueEvent(
                        object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                dataSnapshot.children.forEach {
                                    val chatroomm = it.getValue(Chatroom::class.java)
                                    if (chatroomm != null) {
                                        if (guidekey!! == chatroomm.guide) {
                                            chatroomkey = it.key

                                            val chatList = ArrayList<Chat>()
                                            val chatKey = ArrayList<String>()

                                            rootRef.child("Guide").child(guidekey)
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            val guide =
                                                                dataSnapshot.getValue(Guide::class.java)
                                                            if (guide != null) {
                                                                binding.oppositename.text =
                                                                    guide.name

                                                                binding.chatview.adapter = ChatRecycleAdapter(chatList, chatKey, guide.name.toString())
                                                            }
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        // Failed to read value
                                                    }
                                                })

                                            rootRef.child("Chat").orderByChild("chatroom").equalTo(chatroomkey).addChildEventListener(
                                                object : ChildEventListener {
                                                    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                                                        val newChat = dataSnapshot.getValue(Chat::class.java)

                                                        if (newChat != null) {
                                                            chatList.add(newChat)
                                                            chatKey.add(dataSnapshot.key!!)
                                                            binding.chatview.adapter?.notifyDataSetChanged()
                                                        }
                                                    }

                                                    override fun onChildChanged(
                                                        snapshot: DataSnapshot,
                                                        previousChildName: String?
                                                    ) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onChildRemoved(snapshot: DataSnapshot) {
                                                        val skey: String = snapshot.key!!
                                                        val index: Int = chatKey.indexOf(skey)

                                                        chatList.removeAt(index)
                                                        chatKey.removeAt(index)
                                                        binding.chatview.adapter?.notifyDataSetChanged()
                                                    }

                                                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })
            }
            else{
                rootRef.child("Chatroom").orderByChild("guide").equalTo(key)
                    .addListenerForSingleValueEvent(
                        object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                dataSnapshot.children.forEach {
                                    val chatroomm = it.getValue(Chatroom::class.java)
                                    if (chatroomm != null) {
                                        if (guidekey == chatroomm.tourist) {
                                            chatroomkey = it.key

                                            val chatList = ArrayList<Chat>()
                                            val chatKey = ArrayList<String>()

                                            rootRef.child("Profile").child(guidekey)
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            val prof =
                                                                dataSnapshot.getValue(Profile::class.java)
                                                            if (prof != null) {
                                                                binding.oppositename.text =
                                                                    prof.name

                                                                binding.chatview.adapter = ChatRecycleAdapter(chatList, chatKey, prof.name.toString())
                                                            }
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        // Failed to read value
                                                    }
                                                })

                                            rootRef.child("Chat").orderByChild("chatroom").equalTo(chatroomkey).addChildEventListener(
                                                object : ChildEventListener {
                                                    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                                                        val newChat = dataSnapshot.getValue(Chat::class.java)

                                                        if (newChat != null) {
                                                            chatList.add(newChat)
                                                            chatKey.add(dataSnapshot.key!!)
                                                            binding.chatview.adapter?.notifyDataSetChanged()
                                                            binding.chatview.scrollToPosition(binding.chatview.adapter?.itemCount?.minus(1)!!)
                                                        }
                                                    }

                                                    override fun onChildChanged(
                                                        snapshot: DataSnapshot,
                                                        previousChildName: String?
                                                    ) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onChildRemoved(snapshot: DataSnapshot) {
                                                        val skey: String = snapshot.key!!
                                                        val index: Int = chatKey.indexOf(skey)

                                                        chatList.removeAt(index)
                                                        chatKey.removeAt(index)
                                                        binding.chatview.adapter?.notifyDataSetChanged()
                                                    }

                                                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })
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

        if (chatroomkey != null){
            val chatList = ArrayList<Chat>()
            val chatKey = ArrayList<String>()

            rootRef.child("Chatroom").child(chatroomkey!!)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val chatr = dataSnapshot.getValue(Chatroom::class.java)
                            if (chatr != null) {
                                if(mode == "T") {
                                    rootRef.child("Guide").child(chatr.guide!!)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    val guide =
                                                        dataSnapshot.getValue(Guide::class.java)
                                                    if (guide != null) {
                                                        binding.oppositename.text =
                                                            guide.name

                                                        binding.chatview.adapter =
                                                            ChatRecycleAdapter(
                                                                chatList,
                                                                chatKey,
                                                                guide.name.toString()
                                                            )
                                                    }
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                // Failed to read value
                                            }
                                        })
                                }
                                else{
                                    rootRef.child("Profile").child(chatr.tourist!!)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    val prof =
                                                        dataSnapshot.getValue(Profile::class.java)
                                                    if (prof != null) {
                                                        binding.oppositename.text =
                                                            prof.name

                                                        binding.chatview.adapter =
                                                            ChatRecycleAdapter(
                                                                chatList,
                                                                chatKey,
                                                                prof.name.toString()
                                                            )
                                                    }
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                // Failed to read value
                                            }
                                        })
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })

            binding.chatview.adapter = ChatRecycleAdapter(chatList, chatKey, binding.oppositename.text.toString())

            rootRef.child("Chat").orderByChild("chatroom").equalTo(chatroomkey).addChildEventListener(
                object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                        val newChat = dataSnapshot.getValue(Chat::class.java)

                        if (newChat != null) {
                            chatList.add(newChat)
                            chatKey.add(dataSnapshot.key!!)
                            binding.chatview.adapter?.notifyDataSetChanged()
                            binding.chatview.scrollToPosition(binding.chatview.adapter?.itemCount?.minus(1)!!)
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        val skey: String = snapshot.key!!
                        val index: Int = chatKey.indexOf(skey)

                        chatList.removeAt(index)
                        chatKey.removeAt(index)
                        binding.chatview.adapter?.notifyDataSetChanged()
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

        binding.send.setOnClickListener {
            if(binding.text.text.toString().isEmpty()){
                Toast.makeText(activity,"Empty Message", Toast.LENGTH_SHORT).show()
            }
            else{
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = current.format(formatter)

                if(chatroomkey.isNullOrEmpty()) {
                    chatroomkey = chatroomRef.push().key.toString()

                    if(mode == "T") {
                        val newRoom = Chatroom(key, guidekey, formatted)
                        chatroomRef.child(chatroomkey!!).setValue(newRoom)
                    }
                    else{
                        val newRoom = Chatroom(guidekey, key, formatted)
                        chatroomRef.child(chatroomkey!!).setValue(newRoom)
                    }

                    val chatList = ArrayList<Chat>()
                    val chatKey = ArrayList<String>()

                    binding.chatview.adapter = ChatRecycleAdapter(chatList, chatKey, binding.oppositename.text.toString())

                    rootRef.child("Chat").orderByChild("chatroom").equalTo(chatroomkey).addChildEventListener(
                        object : ChildEventListener {
                            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                                val newChat = dataSnapshot.getValue(Chat::class.java)

                                if (newChat != null) {
                                    chatList.add(newChat)
                                    chatKey.add(dataSnapshot.key!!)
                                    binding.chatview.adapter?.notifyDataSetChanged()
                                    binding.chatview.scrollToPosition(binding.chatview.adapter?.itemCount?.minus(1)!!)
                                }
                            }

                            override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                TODO("Not yet implemented")
                            }

                            override fun onChildRemoved(snapshot: DataSnapshot) {
                                val skey: String = snapshot.key!!
                                val index: Int = chatKey.indexOf(skey)

                                chatList.removeAt(index)
                                chatKey.removeAt(index)
                                binding.chatview.adapter?.notifyDataSetChanged()
                            }

                            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                                TODO("Not yet implemented")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                }
                val id = chatRef.push().key

                val newChat = Chat(key, binding.text.text.toString(), formatted, chatroomkey)
                if (id != null) {
                    chatRef.child(id).setValue(newChat)
                    chatroomRef.child(chatroomkey!!).child("lastupdate").setValue(formatted)
                }

                binding.text.setText("")
            }
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }
}
