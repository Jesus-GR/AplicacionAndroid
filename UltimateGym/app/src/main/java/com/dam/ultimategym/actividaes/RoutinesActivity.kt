package com.dam.ultimategym.actividaes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dam.ultimategym.UltimateGymDatabase
import com.dam.ultimategym.R
import com.dam.ultimategym.RutinaAdaptador
import com.dam.ultimategym.databinding.ActivityRoutinesBinding
import com.dam.ultimategym.entidades.Rutinas
import com.dam.ultimategym.objetos.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RoutinesActivity : AppCompatActivity() {
    //Propiedad de autenticación de Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding : ActivityRoutinesBinding
    private lateinit var datos:MutableList<Rutinas>
    private lateinit var db:UltimateGymDatabase
    private lateinit var adaptador: RutinaAdaptador
    private  var nuevaRutinaActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            adaptador.datos = db.rutinasDao().getAll()
            adaptador.notifyItemInserted(datos.size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRoutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        /**
         * Instanciamos la base de datos
         */
        db = UltimateGymDatabase.getAppDatabase(this)
        /**
         * Creamos un objeto que contiene todos los registros de las rutinas con el método getAll()
         */


            if(db.rutinasDao().getAll().isEmpty()){
               val insertarDatos =  db.rutinasDao().insert(
                    Rutinas(0,"Press banca","Pecho","Lunes","https://img2.freepng.es/20180520/hog/kisspng-bench-press-computer-icons-weight-training-dumbbel-5b012f61338d53.2868704915268043212112.jpg"),
                    Rutinas(0,"Dominadas","Dorsales","Martes","https://img2.freepng.es/20180425/tiq/kisspng-weight-training-computer-icons-olympic-weightlifti-national-fitness-figure-5ae093fe2a4455.0339717215246673901731.jpg"),
                    Rutinas(0,"Elevacion lateral", "Hombros", "Miercoles","https://key0.cc/images/preview/190708_543ae599c71f1eada38d501b3265fcd8.png"),
                    Rutinas(0,"Sentadillas","Piernas","Jueves","https://img2.freepng.es/20180422/fqe/kisspng-encapsulated-postscript-computer-icons-clip-art-5adc2fc9ca96e2.3651307115243795938298.jpg"),
                    Rutinas(0,"Curl biceps","Biceps","Viernes","https://key0.cc/images/preview/190708_543ae599c71f1eada38d501b3265fcd8.png")
                )
                datos = db.rutinasDao().getAll()
            }else{
                datos = db.rutinasDao().getAll()
            }

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
                R.id.menuEditar -> {
                    Snackbar.make(binding.root,"Has pulsado sobre la opción Borrar ${rutina.nombre}",Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.menuEliminar -> {
                    //eliminamos la serie de la base de datos
                    db.rutinasDao().delete(rutina)
                    //buscamos el índice de la serie
                    val index = datos.indexOfFirst { it.equals(rutina) }
                    //eliminamos la serie de la colección de datos
                    datos.removeAt(index)
                    // notificamos el cambio al adaptador
                    adaptador.notifyItemRemoved(index)
                    true
                }
            }
            false
        }
        /**
         * Estas variables las pasamos como parámetros al adaptador
         */
         adaptador = RutinaAdaptador(gestionarPulsacionCorta, gestionarPulsacionLarga)
        adaptador.datos = datos
        binding.listaRutinas.adapter = adaptador
        /**
         * El método setHasFixedSize lo llamamos cuando preveemos que el recyclerView
         * no va a variar de tamaño.
         */
       // binding.listaRutinas.setHasFixedSize(true)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_routines, menu)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth = Firebase.auth
        when (item.itemId) {
            R.id.nuevaRutina ->{
               val intencion = Intent(this,NuevaRutinaActivity::class.java)
                //startActivity(intencion)
                nuevaRutinaActivityResult.launch(intencion)
                return true
            }
            R.id.perfil -> {
                val objetoBundle:Bundle = intent.extras?.get("_bundle") as Bundle
                val goPerfil = Intent(this,PerfilActivity::class.java)
                goPerfil.putExtra("_credenciales",objetoBundle)
                startActivity(goPerfil)
                return true
            }
            R.id.diaLunes ->{
                adaptador.datos = db.rutinasDao().getByDay("Lunes")

                true
            }
            R.id.diaMartes ->{
                adaptador.datos = db.rutinasDao().getByDay("Martes")

                true
            }
            R.id.diaMiercoles ->{
                adaptador.datos = db.rutinasDao().getByDay("Miercoles")

                true
            }
            R.id.diaJueves ->{
                adaptador.datos = db.rutinasDao().getByDay("Jueves")

                true
            }
            R.id.diaViernes ->{
                adaptador.datos = db.rutinasDao().getByDay("Viernes")

                true
            }
            R.id.diaSabado ->{
                adaptador.datos = db.rutinasDao().getByDay("Sabado")

                true
            }
            R.id.diaDomingo ->{
                adaptador.datos = db.rutinasDao().getByDay("Domingo")

                true
            }
            R.id.todos ->{
                adaptador.datos = db.rutinasDao().getAll()
                true
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

    /**override fun onContextItemSelected(item: MenuItem): Boolean {
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
    }*/
    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
        finish()
    }
}


