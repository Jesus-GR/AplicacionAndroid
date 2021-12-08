package com.dam.ultimategym.actividaes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dam.ultimategym.R
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.databinding.ActivityNuevaRutinaBinding
import com.dam.ultimategym.entidades.Rutinas

class NuevaRutinaActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityNuevaRutinaBinding
    var idRutina = 7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.saveRutineButton.setOnClickListener { registrarNuevaRutina() }

    }

    fun registrarNuevaRutina(){
        val nombreRutina:String = binding.nameNuevaRutinaText.text as String
        val musculoRutina:String = binding.musculoNuevaRutinaText.text as String
        val diaSemanaRutina:String = binding.diaSemanaNuevaRutinaText as String
        val imagenRutina:String = binding.imagenNuevaRutinaText as String
        val nuevaRutina = Rutinas(20,nombreRutina,musculoRutina,diaSemanaRutina,imagenRutina)

        val db = UltimateGymDatabase.getAppDatabase(this)
        var rutina = db.rutinasDao().insert(nuevaRutina)
    }
}