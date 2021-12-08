package com.dam.ultimategym.actividaes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.R
import com.dam.ultimategym.RutinaAdaptador
import com.dam.ultimategym.databinding.ActivityRoutinesBinding
import com.dam.ultimategym.entidades.Rutinas
import com.dam.ultimategym.objetos.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RoutinesActivity : AppCompatActivity() {
    //Propiedad de autenticación de Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding : ActivityRoutinesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRoutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        /**
         * Instanciamos la base de datos
         */
        val db = UltimateGymDatabase.getAppDatabase(this)
        /**
         * Creamos un objeto que contiene todos los registros de las rutinas con el método getAll()
         */

       /** val Insertardatos = db.rutinasDao().insert(
            Rutinas(1,"Press banca","Pecho","Lunes","https://img2.freepng.es/20180520/hog/kisspng-bench-press-computer-icons-weight-training-dumbbel-5b012f61338d53.2868704915268043212112.jpg"),
            Rutinas(2,"Dominadas","Dorsales","Martes","https://img2.freepng.es/20180425/tiq/kisspng-weight-training-computer-icons-olympic-weightlifti-national-fitness-figure-5ae093fe2a4455.0339717215246673901731.jpg"),
            Rutinas(3,"Elevacion lateral", "Hombros", "Miercoles","https://cdn-icons-png.flaticon.com/512/9/9158.png"),
            Rutinas(4,"Sentadillas","Piernas","Jueves","https://img2.freepng.es/20180422/fqe/kisspng-encapsulated-postscript-computer-icons-clip-art-5adc2fc9ca96e2.3651307115243795938298.jpg"),
            Rutinas(5,"Curl biceps","Biceps","Viernes","https://key0.cc/images/preview/190708_543ae599c71f1eada38d501b3265fcd8.png")
        )*/
        val datos = db.rutinasDao().getAll()

        /**
         *Recibimos el objeto dentro de un bundle de RegisterActivity y lo añadimos a la bbdd

        val objetoBundleFromRegister:Bundle = intent.extras?.get("_usuario") as Bundle
        val nombre:String = objetoBundleFromRegister.get("_nombre") as String
        val apellidos:String = objetoBundleFromRegister.get("_apellidos") as String
        val email:String = objetoBundleFromRegister.get("_email") as String
        val password:String = objetoBundleFromRegister.get("_contraseña") as String
        val fecNac:String = objetoBundleFromRegister.get("_fechNac") as String
        val altura:String = objetoBundleFromRegister.get("_altura") as String
        val peso:String = objetoBundleFromRegister.get("_peso") as String
        val imc:String = objetoBundleFromRegister.get("_imc") as String

        val usuario = com.dam.ultimategym.entidades.Usuario(1,nombre,apellidos,email,password,fecNac,altura,peso,imc)
        val insertUsuario = db.usuarioDao().insert(usuario)
         */
        /**
         * Creamos un adaptador y le pasamos por parámetro los datos creados anteriormente
         * En el constructor de adaptador, le pasamos como parámetro la lamda que hemos definido en el adaptador.
         *
         * Definimos la función para gestionar la pulsación corta sobre un elemento del RecyclerView
         */

        val gestionarPulsacionCorta:(Rutinas) ->Unit = {
            Toast.makeText(this, "Has pulsado en la rutina  ${it.nombre}", Toast.LENGTH_SHORT).show()
        }
        val gestionarPulsacionLarga:(MenuItem,Rutinas)->Boolean = {
            /**
             * item y rutina son los valores que le asignamos dentro de la función a MenuItem y a Rutinas
             */
            item,rutina ->
            when(item.itemId){
                R.id.menuEliminar -> {
                    Snackbar.make(binding.root,"Has pulsado sobre la opción Borrar ${rutina.nombre}",Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.menuEditar -> {
                    Snackbar.make(binding.root,"Has pulsado sobre la opción Editar ${rutina.nombre}",Snackbar.LENGTH_SHORT).show()
                    true
                }
            }
            false
        }
        /**
         * Estas variables las pasamos como parámetros al adaptador
         */
        val adaptador = RutinaAdaptador(datos, gestionarPulsacionCorta, gestionarPulsacionLarga)
        binding.listaRutinas.adapter = adaptador
        /**
         * El método setHasFixedSize lo llamamos cuando preveemos que el recyclerView
         * no va a variar de tamaño.
         */
       // binding.listaRutinas.setHasFixedSize(true)

       // registerForContextMenu()

        /*Recuperamos la información lanzada desde el loginActivity a través del put extra del Intent*/
       // val email:String = intent.extras?.get("email").toString()
        //val contraseña:String = intent.extras?.get("contraseña").toString()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_routines, menu)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nuevaRutina ->{
                val intencion = Intent(this,NuevaRutinaActivity::class.java)
                startActivity(intencion)
                return true
            }
            R.id.perfil -> {
                val objetoBundle:Bundle = intent.extras?.get("_bundle") as Bundle
                val goPerfil = Intent(this,PerfilActivity::class.java)
                goPerfil.putExtra("_credenciales",objetoBundle)
                startActivity(goPerfil)
                return true
            }
            R.id.salir ->{
                auth.signOut()
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuEditar ->{

                return true
            }
            R.id.menuEliminar ->{
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onContextItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
        finish()
    }
}


