package com.dam.ultimategym.actividaes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dam.ultimategym.R
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.databinding.ActivityNuevaRutinaBinding
import com.dam.ultimategym.entidades.Rutinas

class NuevaRutinaActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityNuevaRutinaBinding
    private lateinit var db:UltimateGymDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.saveRutineButton.setOnClickListener { registrarNuevaRutina() }

    }

    fun registrarNuevaRutina(){
        val nombreRutina: String = binding.nameNuevaRutinaText.text.toString()
        val musculoRutina:String = binding.musculoNuevaRutinaText.text.toString()
        val diaSemanaRutina:String = binding.diaSemanaNuevaRutinaText.text.toString()
         lateinit var imagenRutina:String
        if(binding.imagenNuevaRutinaText.text.isNullOrEmpty()){
             imagenRutina = "https://w7.pngwing.com/pngs/1018/952/png-transparent-man-holding-barbell-garage-gym-fitness-centre-computer-icons-physical-fitness-bodybuilding-white-logo-monochrome.png"
        }else{
            imagenRutina = binding.imagenNuevaRutinaText.text.toString()
        }

        val nuevaRutina = Rutinas(0,nombreRutina,musculoRutina,diaSemanaRutina,imagenRutina)

        db = UltimateGymDatabase.getAppDatabase(this)
        var rutina = db.rutinasDao().insert(nuevaRutina)
        setResult(RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }
}