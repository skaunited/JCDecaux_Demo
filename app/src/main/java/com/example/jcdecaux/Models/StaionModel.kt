package com.example.jcdecaux.Models

data class StaionModel (
        val number : Int?,
        val contract_name : String?,
        val name : String?,
        val address : String?,
        val position : Position,
        val banking : Boolean?,
        val bonus : Boolean?,
        val bike_stands : Int?,
        val available_bike_stands : Int?,
        val available_bikes : Int?,
        val status : String?,
        val last_update : Double?
)