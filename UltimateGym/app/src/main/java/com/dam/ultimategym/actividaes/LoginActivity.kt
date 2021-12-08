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
    //Propiedad de autenticación de Firebase
    private lateinit var auth: FirebaseAuth
    // Propiedad de autenticación google
    private lateinit var authG: GoogleSignInClient

    //Usamos la api activityResult para registrar la resupesta de Google
    private var respuestaGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ googleResponse(it)
        //Como el sevicio de autenticación de google lanza una nueva aplicación donde te piden
        //introducir correo y contraseña, con la Api activityResult podemos verificar que está bien o no
        // y realiar alguna accion a raiz de eso
        //googleResponse es una función que definimos más abajo
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
        //setContentView(R.layout.activity_login)
        //######################################
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener { login() }
        binding.registerButton.setOnClickListener { goRegisterActivity() }
        binding.botonGoogle.setOnClickListener{google(it)}

        // Instancia de la propiedad auth a través del servicio de Firebase
        auth = Firebase.auth
    }

    /*override fun onStart() {
        super.onStart()
        /**Comprobar que estamos logeados.*/
        if(auth.currentUser != null){
            goRoutines()
        }
    }*/


    fun login(){
        //Definimos los valores de email y contraseña
        email = binding.emailText.text.toString()
        contraseña = binding.passwordText.text.toString()

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

    fun goRegisterActivity(){
        val intencionRegister = Intent(this, RegisterActivity::class.java)
        startActivity(intencionRegister)
    }
    fun google(view: View){

        //El objeto GoogleSignInOptions nos permite configurar el cliente de atuetnicación de google
        //El método Builder de dicha clase configura la accion por defecto
        //que debe realiar el cliente de autenticación
        val token = getString(R.string.default_web_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            // El método requestIdToken nos permite proporcionarle
            // al cliente de autenticación el Token único para servicio Auth 2.09.
            // Pero nos olvidamos del token ya que al añadir la App a Firebase se genera
            //Automáticamente
            //IMPORTANTE: No tenemos que definir la cadena default_web_client_id. Se coge directamente
            // del archivo google-services.json
            .requestEmail()
            //El método requestEmail indica al cliente que debe solicitar
            //email para realizar el login
            .build()

        //Para crear el cliente de atuetnciación con Google necsitamos el
        // contexto y el objeto de tipo GoogleSignInOptions

        authG = GoogleSignIn.getClient(this,gso)
        //Lanzamos la activityResult y le pasamos una intención que se ha creado automáticamente
        // authG.signInIntent
        respuestaGoogle.launch(authG.signInIntent)
    }

    private fun googleResponse(ar: ActivityResult){
        //Gestionamos la respuesta del servicio de Google Play
        if(ar.resultCode === RESULT_OK)
        //Recuperamos la información de la cuenta que se ha logeado de la intención con Google y
        // este método
            try{
                val tarea =  GoogleSignIn.getSignedInAccountFromIntent(ar.data)
                //Aquí tenemos ya la cuenta del usuario logeado. ApiException es una excepición
                //que se genera en caso de que no se pueda recupera la información
                cuenta = tarea.getResult(ApiException::class.java)

                //recuperada los datos de la cuenta de usuario, cerramos
                //la sesión con Google, ya que puede que en el dispositivo
                // tengamos más de una cuenta registrada.
                authG.signOut()

                //Recuperamos el token a partir de las credenciales

                val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)

                //Logueamos al usuario sober Firebase
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


    /* fun goRoutines(){
        val intencionRoutines = Intent(this, RoutinesActivity::class.java)
        //val usuario = Usuario("","","email","contraseña","","","","")
       // intencionRoutines.putExtra("usuario",usuario)
        startActivity(intencionRoutines)
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
        finish()
    }



}