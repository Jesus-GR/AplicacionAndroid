package com.dam.ultimategym.actividaes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dam.ultimategym.R
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.databinding.ActivityPerfilBinding
import com.dam.ultimategym.objetos.Usuario
import com.google.firebase.auth.FirebaseAuth

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val objetoBundle:Bundle
            objetoBundle = intent.extras?.get("_credenciales") as Bundle
        val email:String = objetoBundle.get("_email") as String
        val password:String = objetoBundle.get("_contraseña") as String
        val db = UltimateGymDatabase.getAppDatabase(this)
        val usuario:com.dam.ultimategym.entidades.Usuario = db.usuarioDao().getByEmailAndPass(email,password)
    
        binding.nombrePerfil.text = usuario.nombre
        binding.apellidosPerfil.text = usuario.apellidos
        binding.emailPerfil.text = usuario.email
        binding.fecNacPerfil.text = usuario.fecNac
        binding.pesoPerfil.text = usuario.peso
        binding.alturaPerfil.text = usuario.altura
        binding.imcPerfil.text = usuario.altura


    }

}