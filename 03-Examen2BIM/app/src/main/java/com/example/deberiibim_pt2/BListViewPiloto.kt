package com.example.deberiibim_pt2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BListViewPiloto : AppCompatActivity() {
    private var arreglo = arrayListOf<BPiloto>()
    private var idEscuderia = -1
    private var posicionItemSeleccionado = -1
    private lateinit var adaptador: ArrayAdapter<BPiloto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blist_view_piloto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el ID de la escudería desde los parámetros
        idEscuderia = intent.getIntExtra("idEscuderia", -1)

        if (idEscuderia == -1) {
            finish() // Finalizar actividad si no se recibe un ID válido
            return
        }

        // Consultar los pilotos de la escudería usando el idEscuderia
        arreglo.addAll(EBaseDeDatos.tablaPiloto?.consultarPilotosPorEscuderia(idEscuderia) ?: arrayListOf())

        // Obtener el ID de la escudería desde los parámetros
        idEscuderia = intent.getIntExtra("idEscuderia", -1)

        // colocar nombre de escuderia en tv
        val escuderia = EBaseDeDatos.tablaEscuderia?.consultarEscuderiaPorId(idEscuderia)
        if (escuderia != null) {
            val tvNombreEscuderia = findViewById<android.widget.TextView>(R.id.tv_escuderia_elegida)
            tvNombreEscuderia.text = escuderia.nombre
        }

        // Si no hay pilotos, mostrar un mensaje o un botón para agregar uno
        if (arreglo.isEmpty()) {
            Toast.makeText(this, "No hay pilotos disponibles. Agrega uno primero.", Toast.LENGTH_SHORT).show()
        }

        val listView = findViewById<ListView>(R.id.lv_list_view_piloto)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )

        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        // Botón para crear piloto
        val botonCrearPiloto = findViewById<Button>(R.id.btn_crear_piloto)
        botonCrearPiloto.setOnClickListener {
            // Pasar el idEscuderia al crear un piloto
            irActividadParametros(ECrearEditarPiloto::class.java, idEscuderia)
        }

        registerForContextMenu(listView)
    }

    // Crear menú contextual
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: android.view.View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_piloto, menu) // Usar un menú específico para pilotos
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    // Manejar selección del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_actualizar_piloto -> {
                val pilotoSeleccionado = arreglo[posicionItemSeleccionado]
                irActividad(ECrearEditarPiloto::class.java, pilotoSeleccionado)
                true
            }
            R.id.mi_eliminar_piloto -> {
                val pilotoSeleccionado = arreglo[posicionItemSeleccionado]
                val dialogo = AlertDialog.Builder(this)
                dialogo.setTitle("Desea eliminar")
                dialogo.setPositiveButton("Aceptar") { _, _ ->
                    // Eliminar el piloto de la base de datos
                    val resultado = EBaseDeDatos.tablaPiloto?.eliminarPiloto(pilotoSeleccionado.id) ?: false
                    if (resultado) {
                        // Eliminar el piloto de la lista y actualizar el ListView
                        arreglo.removeAt(posicionItemSeleccionado)
                        adaptador.notifyDataSetChanged()
                        Toast.makeText(this, "Piloto eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al eliminar el piloto", Toast.LENGTH_SHORT).show()
                    }
                }
                dialogo.setNegativeButton("Cancelar", null)
                dialogo.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Navegar a otra actividad
    private fun irActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    fun irActividad(clase: Class<*>, piloto: BPiloto? = null) {
        val intent = Intent(this, clase)
        if (piloto != null) {
            intent.putExtra("piloto", piloto)
        }
        startActivity(intent)
    }

    // Navegar a otra actividad con parámetros
    fun irActividadParametros(clase: Class<*>, idEscuderia: Int) {
        val intent = Intent(this, clase)
        intent.putExtra("idEscuderia", idEscuderia)
        startActivity(intent)
    }
}
