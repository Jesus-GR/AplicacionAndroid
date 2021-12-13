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

class RutinaAdaptador( val gestionarPulsacionCorta:(Rutinas) -> Unit,
                       val gestionarPulsacionLarga: (MenuItem,Rutinas)-> Boolean):RecyclerView.Adapter<RutinaAdaptador.RutinaContenedor>() {

    var datos:MutableList<Rutinas> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()

        }
    override fun getItemCount(): Int = datos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaContenedor {

        val inflador = LayoutInflater.from(parent.context)
       val binding = ListaRutinasBinding.inflate(inflador,parent, false)

        return RutinaContenedor(binding)
    }

    override fun onBindViewHolder(holder: RutinaContenedor, position: Int) {

        holder.bindRutinas(datos[position])
    }

    inner class RutinaContenedor( val binding: ListaRutinasBinding):RecyclerView.ViewHolder(binding.root){

        fun bindRutinas(rutina:Rutinas){

            binding.tituloRutina.text = rutina.nombre

            Glide.with(binding.root).load(rutina.imagen).into(binding.imagenRutina)

            binding.root.setOnClickListener{gestionarPulsacionCorta(rutina) }

            binding.root.setOnLongClickListener{

                val pop = PopupMenu(binding.root.context,binding.tituloRutina)

                pop.inflate(R.menu.menu_contextual)
                pop.setOnMenuItemClickListener {
                    gestionarPulsacionLarga(it,rutina) }
                pop.show()
                true
            }
        }
    }
}