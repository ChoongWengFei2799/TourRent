package com.example.tourrent.Dclass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Guide (
    var email: String? = "",
    var name: String? = "",
    var password: String? = "",
    var bio: String? = "",
    var tags: String? = "",
    var location: String? = "",
    var price: String? = ""
)