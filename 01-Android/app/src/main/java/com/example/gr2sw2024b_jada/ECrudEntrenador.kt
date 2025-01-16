package com.example.gr2sw2024b_jada

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class ECrudEntrenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ecrud_entrenador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonBuscarBDD = findViewById<Button>(R.id.btn_buscar_bdd)
        botonBuscarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val entrenador = EBaseDeDatos.tablaEntrenador!!
                .consultarEntrenadorPorId(id.text.toString().toInt())
            if (entrenador == null) {
                id.setText("")
                nombre.setText("")
                descripcion.setText("")
                mostrarSnackbar("No se encontró el entrenador")
            } else {
                nombre.setText(entrenador.nombre)
                descripcion.setText(entrenador.descripcion)
                mostrarSnackbar("Entrenador encontrado")
            }
        }

        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_bdd)
        botonEliminarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_id)
            val resultado = EBaseDeDatos.tablaEntrenador!!
                .eliminarEntrenador(id.text.toString().toInt())
            if (resultado) {
                id.setText("")
                mostrarSnackbar("Entrenador eliminado")
            } else {
                mostrarSnackbar("No se encontró el entrenador")
            }
        }

        val botonCrearBdd = findViewById<Button>(R.id.btn_crear_bdd)
        botonCrearBdd.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val resultado = EBaseDeDatos.tablaEntrenador!!
                .crearEnrenador(
                    nombre.text.toString(),
                    descripcion.text.toString()
                )
            if (resultado) {
                mostrarSnackbar("Entrenador creado")
            } else {
                mostrarSnackbar("Error al crear el entrenador")
            }
        }

        val botonActualizarBdd = findViewById<Button>(R.id.btn_actualizar_bdd)
        botonActualizarBdd.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val resultado = EBaseDeDatos.tablaEntrenador!!
                .actualizarEntrenador(
                    nombre.text.toString(),
                    descripcion.text.toString(),
                    id.text.toString().toInt()
                )
            if (resultado) {
                mostrarSnackbar("Entrenador actualizado")
            } else {
                mostrarSnackbar("Error al actualizar el entrenador")
            }
        }
    }

    fun mostrarSnackbar (texto: String) {
        val snack = Snackbar.make (
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}