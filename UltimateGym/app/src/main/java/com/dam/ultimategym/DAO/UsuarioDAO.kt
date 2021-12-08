package com.dam.ultimategym.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dam.ultimategym.entidades.Usuario

@Dao
interface UsuarioDAO {

    @Query("SELECT * FROM Usuario WHERE email = :email AND password = :password")
    fun getByEmailAndPass(email:String, password:String):Usuario

    @Insert
    fun insert(usuario:Usuario)

    @Delete
    fun delete(vararg usuario:Usuario)
}