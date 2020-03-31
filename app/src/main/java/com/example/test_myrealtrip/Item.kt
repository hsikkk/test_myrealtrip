package com.example.test_myrealtrip

import android.graphics.Bitmap
import java.io.Serializable

const val Load_Fail_Message = "Failed to Load"

class Item(
    val title : String,
    val link : String,
    val description : String,
    val img : Bitmap?

) : Serializable{

    val keywords: ArrayList<String>

    init{
        when (description != Load_Fail_Message) {
            true -> keywords = ExtractKeywords(description).keywords
            false -> keywords = ArrayList()
        }
    }


}