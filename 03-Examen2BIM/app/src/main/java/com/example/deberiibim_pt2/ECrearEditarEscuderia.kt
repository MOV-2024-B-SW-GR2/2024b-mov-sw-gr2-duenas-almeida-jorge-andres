package com.example.deberiibim_pt2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class ECrearEditarEscuderia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_ecrear_editar_escuderia)


        val escuderia: BEscuderia? = intent.getParcelableExtra("escuderia")

        escuderia?.let {
            val id = findViewById<EditText>(R.id.input_id_escuderia)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val fundacion = findViewById<EditText>(R.id.input_fundacion)
            val nacionalidad = findViewById<EditText>(R.id.input_nacionalidad)
            val presupuesto = findViewById<EditText>(R.id.input_presupuesto)
            val latCentroDesarrolo = findViewById<EditText>(R.id.input_lat_centro_desarrollo)
            val lonCentroDesarrolo = findViewById<EditText>(R.id.input_lon_centro_desarrollo)

            // Rellenar los campos con los datos de la escudería
            id.setText(it.id.toString())
            nombre.setText(it.nombre)
            fundacion.setText(it.fundacion)
            nacionalidad.setText(it.nacionalidad)
            presupuesto.setText(it.presupuesto.toString())
            latCentroDesarrolo.setText(it.latCentroDesarrollo.toString())
            lonCentroDesarrolo.setText(it.lonCentroDesarrollo.toString())
        }

        val botonCrearEscuderia = findViewById<Button>(R.id.btn_agregar_escuderia)
        botonCrearEscuderia.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val fundacion = findViewById<EditText>(R.id.input_fundacion)
            val nacionalidad = findViewById<EditText>(R.id.input_nacionalidad)
            val presupuesto = findViewById<EditText>(R.id.input_presupuesto)
            val latCentroDesarrolo = findViewById<EditText>(R.id.input_lat_centro_desarrollo)
            val lonCentroDesarrolo = findViewById<EditText>(R.id.input_lon_centro_desarrollo)
            val resultado = EBaseDeDatos.tablaEscuderia!!.crearEscuderia(
                nombre.text.toString(),
                fundacion.text.toString(),
                nacionalidad.text.toString(),
                esActiva = true,
                presupuesto.text.toString().toDouble(),
                latCentroDesarrolo.text.toString().toDouble(),
                lonCentroDesarrolo.text.toString().toDouble()
            )
            if (resultado) {
                mostrarSnackbar("Escudería creada")
            } else {
                mostrarSnackbar("Error al crear la escudería")
            }
        }

        // Lógica para actualizar los datos cuando el usuario hace clic en "Actualizar"
        val botonActualizarEscuderia = findViewById<Button>(R.id.btn_editar_escuderia)
        botonActualizarEscuderia.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val fundacion = findViewById<EditText>(R.id.input_fundacion)
            val nacionalidad = findViewById<EditText>(R.id.input_nacionalidad)
            val presupuesto = findViewById<EditText>(R.id.input_presupuesto)
            val latCentroDesarrolo = findViewById<EditText>(R.id.input_lat_centro_desarrollo)
            val lonCentroDesarrolo = findViewById<EditText>(R.id.input_lon_centro_desarrollo)
            val resultado = EBaseDeDatos.tablaEscuderia!!.actualizarEscuderia(
                nombre.text.toString(),
                fundacion.text.toString(),
                nacionalidad.text.toString(),
                esActiva = true,
                presupuesto.text.toString().toDouble(),
                latCentroDesarrolo.text.toString().toDouble(),
                lonCentroDesarrolo.text.toString().toDouble(),
                escuderia?.id ?: -1
            )
            if (resultado) {
                mostrarSnackbar("Escudería actualizada")
            } else {
                mostrarSnackbar("Error al actualizar la escudería")
            }
        }
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}
