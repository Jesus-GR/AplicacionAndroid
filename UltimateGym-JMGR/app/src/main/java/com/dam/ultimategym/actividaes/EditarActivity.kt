package com.dam.ultimategym.actividaes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.databinding.ActivityEditarBinding
import com.dam.ultimategym.entidades.Rutinas

class EditarActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditarBinding
    private var db:UltimateGymDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editRutineButton.setOnClickListener { editarRutina() }

    }

    fun editarRutina(){
        db = UltimateGymDatabase.getAppDatabase(this)
        var editaRutina:Rutinas = intent?.extras?.getSerializable("_rutina") as Rutinas
        editaRutina.nombre = binding.nameEditaRutinaText.text.toString()
        editaRutina.descripcion = binding.musculoEditaRutinaText.text.toString()
        editaRutina.diaSemana = binding.diaSemanaEditaRutinaText.text.toString()

        if(binding.imagenEditaRutinaText.text.isNullOrEmpty()){
           editaRutina.imagen =  "https://w7.pngwing.com/pngs/1018/952/png-transparent-man-holding-barbell-garage-gym-fitness-centre-computer-icons-physical-fitness-bodybuilding-white-logo-monochrome.png"
        }else{
            editaRutina.imagen = binding.imagenEditaRutinaText.text.toString()
        }



        val rutinaEditada = db?.rutinasDao()?.updateRutina(editaRutina)
        setResult(RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)

    }
}