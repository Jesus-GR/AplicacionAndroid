package com.dam.ultimategym

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dam.ultimategym.databinding.ListaRutinasBinding
import com.dam.ultimategym.entidades.Rutinas

/**
 * Tenemos que indicar que la clase RutinaAadaptador hereda de RecyclerView.Adapter
 * Tenemos que indicarle que contenedor vamos a utilizar para almacenar cada elemento de la coleción
 * de datos.
 *
 * El contendeor lo definimos dentro de la clase RutinaAdaptdor
 *
 * RutinaContenedor es la que utilizará el adaptador como plantilla para crear un contenedor para
 * cada elementod e la colección de datos.
 *
 * Tenemos que implementarle los 3 métodos. ClickDerecho/Generate/ImplementMethods.
 * Por parametro le apsamos una lista mutable de Rutinas.
 * gestionarPulsacionCorta es la función lambda que utilizamos en el listener del contenedor para
 * gestionar pulsaciones. Es una lamda que no recibe ni devuelve nada. La pasamos como parámetro a la clase
 * adaptador. Esta función la pasamos al contructor como parámetro en el routinesActivity
 */
class RutinaAdaptador( var datos:MutableList<Rutinas>,
                       val gestionarPulsacionCorta:(Rutinas) -> Unit,
                       val gestionarPulsacionLarga: (MenuItem,Rutinas)-> Boolean):RecyclerView.Adapter<RutinaAdaptador.RutinaContenedor>(){
    /**
     * Este es el primer método que llama nuestro adaptador para determinar el tamaño de la colección
     * de datos que va a manejar y saber cuantos contenedores tiene que hacer
     */
    override fun getItemCount(): Int = datos.size

    /**
     * Con forme recorremos la colección de datos, el adaptador llama al método
     * onCreateViewHolder para crear un contenedor para cada uno de esos items.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaContenedor {
        /**
         * Vamos a utilizar la vinculación de vistas. Creamos una variable dentro del metotodo
         * viewHolder y inflamos el layout. Para inflarlo necesitamos un inflador, asociarlo al recylerView.
         * Para asociarlo, usamos el LayoutInflater.from(y le añadimos el contexto del padre, que en este caso
         * es el recyclerView)
         *
         */
        val inflador = LayoutInflater.from(parent.context)
       val binding = ListaRutinasBinding.inflate(inflador,parent, false)

        return RutinaContenedor(binding)
    }

    /**
     * Una vez creado cada contenedor, se le pasa al método onBindViewHolder para
     * vincular ese contenedor con el elemento correspondiente de la coleccón de datos
     */
    override fun onBindViewHolder(holder: RutinaContenedor, position: Int) {

        /**
         * Recibimos el contenedor que hemos hecho anteriormente y la posición del elemento
         *
         * El método bindRutinas lo tenemos que crear dentro de la clase contendor, más abajo
         */
        holder.bindRutinas(datos[position])
    }



    /**
     * El conenedor siempre debe recibir la vinculación de vistas del layout
     * Al constructor de viewHOLDER tenemos que pasarle la raiz del layout que vamos a utilizar
     * para crear cada contenedor. En este caso binding.root
     *
     * Utilizamos inner para que desde la clase RutinaContenedor podamos acceder a las propiedades
     * de su clase contenedora, que en este caso es RutinaAdaptador
     */
    inner class RutinaContenedor( val binding: ListaRutinasBinding):RecyclerView.ViewHolder(binding.root){
        /**
         * Creamos el método, le pasamos un objeto de tipo Rutinas y rellenamos cada una de las
         * que añadimos al layout ListaRutinas con los datos de las rutinas que se nos está pasando
         * parámetro
         */
        fun bindRutinas(rutina:Rutinas){
            /**
             * Mostramos el título de la rutina
             */
            binding.tituloRutina.text = rutina.nombre

            /**
             * Cargamos la imagen de la vista correspondiente. Necesitamos la libería glide.
             * Para ello necesitamos acceder a la librería, https://github.com/bumptech/glide,
             * copiamos las dependencias y añadirlas en el gradle.
             *
             * Utilizamos el método with para proporcionarle uan vista, actividad, contexto...En este
             * caso podemos proporcionarle la raiz de nustro layout a traves de binding.root.
             * A continuación le decimos que carge la imagen con el método load y finalmente donde tiene
             * que cargar la imagen con el método .into(binding.imagenRutina)
             *
             * Después de esto nos vamos a RoutinesActivity
             */

            Glide.with(binding.root).load(rutina.imagen).into(binding.imagenRutina)

            /**
             * Defiinimos un listener asocido a la raiz del layout que reaccionará cuando
             * realicemos una pulsación sobre el propio contenedor
             * La función gestionarPulsaciónCorta() la pasamos por parámetro a la clase adaptador
             * a través del constructor
             */
            binding.root.setOnClickListener{gestionarPulsacionCorta(rutina) }

            /**
             * Definimos la pulsación larga. Esta pulsación devuelve un booleano. Definimos el
             * menú contextual de una forma diferente con PopupMenu. Este necesita un contexto y una
             * vista la cual vamos a asociar. El contexto lo hacemos con binding.root.context y la
             * vista puede ser la raiz del contenedor, la imagen, el nombre de la rutina....
             * Las opciones del menú las gestionamos como la pulsación corta. Dentro de la función
             * que le pasamos gestionarPulsacionLarga le pasamos (it), para saber sobre que item hacemos
             * la pulsación. Tambien tenemos que definir la pulsación larga en el constructor de la clase RutinaAdaptador
             */

            binding.root.setOnLongClickListener{

                val pop = PopupMenu(binding.root.context,binding.tituloRutina)
                /**
                 * Inflamos el menú contextual
                 */
                pop.inflate(R.menu.menu_contextual)
                pop.setOnMenuItemClickListener {
                    gestionarPulsacionLarga(it,rutina) }
                /**
                 * Lo mostramos
                 */
                pop.show()
                true
            }
        }
    }
}