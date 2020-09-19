package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Review (
    var tourist: String? = "",
    var guide: String? = "",
    var feedback: String? = ""
)