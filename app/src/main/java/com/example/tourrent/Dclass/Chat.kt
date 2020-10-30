package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Chat (
    var sender: String? = "",
    var text: String? = "",
    var time: String? = "",
    var chatroom:String? = ""
)