package com.dam.ultimategym.DAO

import androidx.room.*
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

    @Update
    fun updateRutina(rutina:Rutinas)

}