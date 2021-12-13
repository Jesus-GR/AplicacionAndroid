package com.dam.ultimategym.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo val nombre:String,
    @ColumnInfo val apellidos:String,
    @ColumnInfo val email:String,
    @ColumnInfo val password:String,
    @ColumnInfo val fecNac:String,
    @ColumnInfo val altura:String,
    @ColumnInfo val peso:String,
    @ColumnInfo val imc:String
)
