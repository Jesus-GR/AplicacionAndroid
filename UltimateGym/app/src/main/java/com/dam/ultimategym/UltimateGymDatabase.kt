package com.dam.ultimategym

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dam.ultimategym.DAO.RutinasDAO
import com.dam.ultimategym.DAO.UsuarioDAO
import com.dam.ultimategym.entidades.Rutinas
import com.dam.ultimategym.entidades.Usuario

@Database(entities = arrayOf(Rutinas::class, Usuario::class),version=2)
abstract class UltimateGymDatabase:RoomDatabase() {
    abstract fun rutinasDao(): RutinasDAO
    abstract fun usuarioDao(): UsuarioDAO

    companion object{

        @Volatile
        private var instancia:UltimateGymDatabase? = null

        @Synchronized
         fun getAppDatabase(context:Context):UltimateGymDatabase{
            if(instancia == null)
                instancia = Room.databaseBuilder(context,UltimateGymDatabase::class.java,"UltimateGymDatabase")
                    .allowMainThreadQueries()
                    .build()

            return instancia as UltimateGymDatabase
        }
    }
}