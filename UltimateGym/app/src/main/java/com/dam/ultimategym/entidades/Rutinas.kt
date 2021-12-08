package com.dam.ultimategym.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rutinas(
    @PrimaryKey val rutinaId:Int,
    @ColumnInfo val nombre:String,
    @ColumnInfo val descripcion:String,
    @ColumnInfo val diaSemana:String,
    @ColumnInfo val imagen:String
)
