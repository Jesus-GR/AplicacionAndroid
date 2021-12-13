package com.dam.ultimategym.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Rutinas(
    @PrimaryKey(autoGenerate = true) val rutinaId:Int,
    @ColumnInfo var nombre:String,
    @ColumnInfo var descripcion:String,
    @ColumnInfo var diaSemana:String,
    @ColumnInfo var imagen:String
):Serializable
