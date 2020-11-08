package com.example.tourrent.chatroom

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tourrent.Dclass.Chat
import com.example.tourrent.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRecycleAdapter(
    private val chatList: ArrayList<Chat>,
    private val chatKey: ArrayList<String>,
    private val oName: String
) :  RecyclerView.Adapter<ChatRecycleAdapter.ViewHolder>() {

    var rootRef = FirebaseDatabase.getInstance().reference

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.Name)
        val time: TextView = itemView.findViewById(R.id.time)
        val ctext: TextView = itemView.findViewById(R.id.chattext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prefs = holder.itemView.context.getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")

        if (key == chatList[position].sender) {
            holder.name.text = "You"
        }
        else{
            holder.name.text = oName
        }

        holder.ctext.text = chatList[position].text
        holder.time.text = chatList[position].time

        holder.itemView.setOnLongClickListener {
            onLongClick(it, chatList[position].text.toString(), chatKey[position])
        }
    }

    private fun onLongClick(view: View, clipText: String, chatID: String): Boolean {
        val b: AlertDialog.Builder = AlertDialog.Builder(view.context)
        b.setTitle("Additional Activity")
        val location = arrayOf("Copy to Clipboard", "Delete")
        b.setItems(location) { dialog, which ->
            dialog.dismiss()
            when (which) {
                0 -> copyClip(view, clipText)
                1 -> removeText(view, chatID)
            }
        }
        b.show()

        return true
    }

    private fun removeText(view: View, chatID: String){
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        rootRef.child("Chat").child(chatID).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                dataSnapshot.ref.removeValue()
                                Toast.makeText(view.context, "Message Removed", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        Toast.makeText(view.context, "Message Not Removed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
        builder.setMessage("Are you sure to Remove Message Sent?").setPositiveButton(
            "Yes", dialogClickListener
        ).setNegativeButton("No", dialogClickListener).show()
    }

    private fun copyClip(view: View, clipText: String){
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("copy", clipText)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(view.context, "Message Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }
}