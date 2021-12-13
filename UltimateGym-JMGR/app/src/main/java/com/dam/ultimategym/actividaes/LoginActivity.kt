package com.dam.ultimategym.actividaes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.dam.ultimategym.R
import com.dam.ultimategym.databinding.ActivityLoginBinding
import com.dam.ultimategym.objetos.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var authG: GoogleSignInClient

    private var respuestaGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ googleResponse(it)

    }
    
    private lateinit var  binding : ActivityLoginBinding
    lateinit var email:String
    lateinit var contraseña:String
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            email = it.data?.extras?.get("key").toString()
            contraseña = binding.passwordText.text.toString()
            Toast.makeText(this, "El email es ${email} y la contraseña es ${contraseña}", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Error de registro", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var cuenta: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener { login() }
        binding.registerButton.setOnClickListener { goRegisterActivity() }
        binding.botonGoogle.setOnClickListener{google(it)}

        auth = Firebase.auth
    }

    fun login(){
        email = binding.emailText.text.toString()
        contraseña = binding.passwordText.text.toString()

        if(email.isNullOrEmpty() || contraseña.isNullOrEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email,contraseña)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val bundle = Bundle()
                        bundle.putString("_email",email)
                        bundle.putString("_contraseña",contraseña)
                        val intencion = Intent(this,RoutinesActivity::class.java)
                        intencion.putExtra("_bundle",bundle)
                        startActivity(intencion)
                        finish()
                    }   else{
                        Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun goRegisterActivity(){
        val intencionRegister = Intent(this, RegisterActivity::class.java)
        startActivity(intencionRegister)
    }
    fun google(view: View){

        val token = getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)

            .requestEmail()

            .build()
        authG = GoogleSignIn.getClient(this,gso)

        respuestaGoogle.launch(authG.signInIntent)
    }

    private fun googleResponse(ar: ActivityResult){
        if(ar.resultCode === RESULT_OK)
            try{
                val tarea =  GoogleSignIn.getSignedInAccountFromIntent(ar.data)

                cuenta = tarea.getResult(ApiException::class.java)

                authG.signOut()


                val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)

                auth.signInWithCredential(credenciales).addOnCompleteListener{
                    if(it.isSuccessful){
                        val bundle = Bundle()
                        bundle.putString("_email",email) as String
                        bundle.putString("_contraseña",contraseña)
                        val intencion = Intent(this,RoutinesActivity::class.java)
                        intencion.putExtra("_bundle",bundle)
                        startActivity(intencion)
                        finish()
                    }else {
                        Snackbar.make(binding.root,"Error de login", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }catch (e: ApiException){
                Log.e("UltimateGym excepcion", "Se ha producico un error en el servicio de google play",e)
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
        finish()
    }



}