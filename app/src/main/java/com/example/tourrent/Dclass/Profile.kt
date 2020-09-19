package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Profile (
    var email: String? = "",
    var name: String? = "",
    var password: String? = ""
)