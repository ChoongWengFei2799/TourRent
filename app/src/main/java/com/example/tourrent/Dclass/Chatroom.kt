package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Chatroom (
    var tourist: String? = "",
    var guide: String? = "",
    var lastupdate: String? = ""
)