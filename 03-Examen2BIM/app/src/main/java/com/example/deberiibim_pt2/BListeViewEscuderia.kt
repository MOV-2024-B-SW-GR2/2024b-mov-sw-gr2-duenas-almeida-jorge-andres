package com.example.deberiibim_pt2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class BListeViewEscuderia : AppCompatActivity() {
    private val arreglo = arrayListOf<BEscuderia>()
    private lateinit var adaptador: ArrayAdapter<BEscuderia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            enableEdgeToEdge()
        }
        setContentView(R.layout.activity_bliste_view_escuderia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar arreglo con datos desde la base de datos
        arreglo.addAll(EBaseDeDatos.tablaEscuderia?.consultarEscuderias() ?: arrayListOf())

        val listView = findViewById<ListView>(R.id.lv_list_view_escuderia)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonCrearEscuderia = findViewById<Button>(R.id.btn_crear_escuderia)
        botonCrearEscuderia.setOnClickListener {
            irActividad(ECrearEditarEscuderia::class.java)
        }
        registerForContextMenu(listView)
    }

    var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: android.view.View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_escuderia, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_actualizar_escuderia -> {
                val escuderiaSeleccionada = arreglo[posicionItemSeleccionado]
                irActividad(ECrearEditarEscuderia::class.java, escuderiaSeleccionada)
                true
            }
            R.id.mi_eliminar_escuderia -> {
                val escuderiaSeleccionada = arreglo[posicionItemSeleccionado]
                val dialogo = AlertDialog.Builder(this)
                dialogo.setTitle("¿Desea eliminar esta escudería?")
                dialogo.setPositiveButton(
                    "Aceptar"
                ) { _, _ ->
                    EBaseDeDatos.tablaEscuderia?.eliminarEscuderia(escuderiaSeleccionada.id)
                    actualizarLista()
                }
                dialogo.setNegativeButton("Cancelar", null)
                dialogo.show()
                true
            }
            R.id.mi_ver_pilotos -> {
                val escuderiaSeleccionada = arreglo[posicionItemSeleccionado]
                val idEscuderia = escuderiaSeleccionada.id
                if (idEscuderia > 0) {
                    irActividadParametros(BListViewPiloto::class.java, idEscuderia)
                } else {
                    mostrarSnackbar("El ID de la escudería no es válido.")
                }
                true
            }
            R.id.mi_ver_centro_desarrollo -> {
                val escuderiaSeleccionada = arreglo[posicionItemSeleccionado]
                val idEscuderia = escuderiaSeleccionada.id
                if (idEscuderia > 0) {
                    irActividadParametros(GGoogleMaps::class.java, idEscuderia)
                } else {
                    mostrarSnackbar("El ID de la escudería no es válido.")
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun actualizarLista() {
        arreglo.clear()
        arreglo.addAll(EBaseDeDatos.tablaEscuderia?.consultarEscuderias() ?: arrayListOf())
        adaptador.notifyDataSetChanged()
    }

    fun irActividad(clase: Class<*>, escuderia: BEscuderia? = null) {
        val intent = Intent(this, clase)
        if (escuderia != null) {
            intent.putExtra("escuderia", escuderia)
        }
        startActivity(intent)
    }

    fun irActividadParametros(clase: Class<*>, idEscuderia: Int) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("idEscuderia", idEscuderia) // Pasar el idEscuderia
        startActivity(intentExplicito)
    }


    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }
}
