package com.thecheckpoint.azerbaijanwallpaper

import com.google.firebase.Timestamp

data class WallpaperModel(
    val thumbnail: String = "",
    val image: String = "",
    val date : Timestamp? = null

)