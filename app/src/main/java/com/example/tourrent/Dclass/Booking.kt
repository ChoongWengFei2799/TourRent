package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Booking (
    var tourist: String? = "",
    var guide: String? = "",
    var type: String? = "",
    var startDate: String? = "",
    var endDate: String? = "",
    var location: String? = ""
)