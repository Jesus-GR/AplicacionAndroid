package com.dam.ultimategym.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dam.ultimategym.entidades.Rutinas

@Dao
interface RutinasDAO {

    @Query("SELECT * FROM Rutinas")
    fun getAll():MutableList<Rutinas>

    @Query("SELECT * FROM Rutinas WHERE diaSemana = :dia")
    fun getByDay(dia:String):MutableList<Rutinas>

    @Insert
    fun insert(vararg rutinas:Rutinas)

    @Delete
    fun delete(vararg rutinas:Rutinas)

}