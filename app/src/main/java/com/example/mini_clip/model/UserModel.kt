package com.example.mini_clip.model

data class UserModel(

    var id : String = "",
    var email : String = "",
    var username : String= "",
    var profilePic : String="",
    var bookMarkList: MutableList<String> = mutableListOf(),
    var followerList : MutableList<String> = mutableListOf(),
    var followingList : MutableList<String> = mutableListOf()
)
