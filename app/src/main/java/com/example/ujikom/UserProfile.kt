package com.example.ujikom

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    var name: String = "",
    var nim: String = "",
    var alamat: String = "",
    var tanggalLahir: String = "",
    var tempatLahir: String = "",
    var kelamin: String = "",
    val photoUri: String
): Parcelable