package com.example.swipeassignmentbymustafa


data class PostModel(
    val product_name:String,
    val product_type:String,
    val price:Double?=null,
    val tax:Double?=null,
    val image: String?=null)