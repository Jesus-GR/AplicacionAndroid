package com.dam.ultimategym.actividaes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.databinding.ActivityRegisterBinding
import com.dam.ultimategym.objetos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    var idUsuario:Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding.saveRegisterButton.setOnClickListener { register() }
    }

    fun register(){
        val db = UltimateGymDatabase.getAppDatabase(this)
        val nombre = binding.nameText.text.toString().trim()
        val apellidos = binding.apeText.text.toString().trim()
        val email = binding.emailRegisterText.text.toString().trim()
        val contraseña = binding.passwordRegisterText.text.toString().trim()
        val fechaNac = binding.ageText.text.toString().trim()
        val altura = binding.heighText.text.toString().trim()
        val peso = binding.weighRegisterText.text.toString().trim()
        val imc = binding.imcRegisterText.text.toString().trim()

        if(nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || contraseña.isEmpty() || fechaNac.isEmpty() ||
            altura.isEmpty() || peso.isEmpty() || imc.isEmpty()){

            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(email,contraseña).addOnCompleteListener(this){

                if(it.isSuccessful){

                    val usuario = com.dam.ultimategym.entidades.Usuario(
                        idUsuario++,nombre,apellidos,email,contraseña,fechaNac,altura,peso,imc)
                    db.usuarioDao().insert(usuario)
                    val intencion = Intent(this, LoginActivity::class.java)
                    startActivity(intencion)
                }
                else{
                    Toast.makeText(this, "Error de registro", Toast.LENGTH_SHORT).show()
                }
            }
          
        }


    }




    }
