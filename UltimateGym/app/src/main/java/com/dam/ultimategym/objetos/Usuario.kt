package com.dam.ultimategym.objetos

import java.io.Serializable

data class Usuario(
    val nombre:String,
    val apellidos:String,
    val email:String,
    val contraseña:String,
    val edad:String,
    val altura:String,
    val peso:String,
    val imc:String) : Serializable
