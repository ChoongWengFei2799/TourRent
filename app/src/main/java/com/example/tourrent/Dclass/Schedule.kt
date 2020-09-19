package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Schedule (
    var date: String? = "",
    var time: String? = "",
    var location: String? = "",
    var bookingId: String? = ""
)