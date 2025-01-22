package com.example.deberiibim_pt2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class ECrearEditarPiloto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_ecrear_editar_piloto)

        val piloto: BPiloto? = intent.getParcelableExtra("piloto")

        piloto?.let {
            val id = findViewById<EditText>(R.id.input_id_piloto)
            val nombre = findViewById<EditText>(R.id.input_nombre_piloto)
            val fechaNacimiento = findViewById<EditText>(R.id.input_nacimiento)
            val numero = findViewById<EditText>(R.id.input_numero)
            val salario = findViewById<EditText>(R.id.input_salario)

            // Rellenar los campos con los datos del piloto
            id.setText(it.id.toString())
            nombre.setText(it.nombre)
            fechaNacimiento.setText(it.fechaNacimiento)
            numero.setText(it.numero.toString())
            salario.setText(it.salario.toString())
        }

        val botonCrearPiloto = findViewById<Button>(R.id.btn_agregar_piloto)
        botonCrearPiloto.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre_piloto)
            val fechaNacimiento = findViewById<EditText>(R.id.input_nacimiento)
            val numero = findViewById<EditText>(R.id.input_numero)
            val salario = findViewById<EditText>(R.id.input_salario)

            // Obtener el ID de la escudería desde el Intent
            val idEscuderia = intent.getIntExtra("idEscuderia", -1)
            if (idEscuderia == -1) {
                mostrarSnackbar("Escudería no válida")
                return@setOnClickListener
            }

            val resultado = EBaseDeDatos.tablaPiloto!!.crearPiloto(
                nombre.text.toString(),
                fechaNacimiento.text.toString(),
                numero.text.toString().toInt(),
                salario.text.toString().toDouble(),
                esTitular = true,
                idEscuderia = idEscuderia
            )
            if (resultado) {
                mostrarSnackbar("Piloto creado")
            } else {
                mostrarSnackbar("Error al crear el piloto")
            }
        }

        val botonActualizarPiloto = findViewById<Button>(R.id.btn_editar_piloto)
        botonActualizarPiloto.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombre_piloto)
            val fechaNacimiento = findViewById<EditText>(R.id.input_nacimiento)
            val numero = findViewById<EditText>(R.id.input_numero)
            val salario = findViewById<EditText>(R.id.input_salario)

            val resultado = EBaseDeDatos.tablaPiloto!!.actualizarPiloto(
                nombre.text.toString(),
                fechaNacimiento.text.toString(),
                numero.text.toString().toInt(),
                salario.text.toString().toDouble(),
                esTitular = true,
                idEscuderia = piloto?.idEscuderia ?: -1,
                piloto?.id ?: -1
            )
            if (resultado) {
                mostrarSnackbar("Piloto actualizado")
            } else {
                mostrarSnackbar("Error al actualizar el piloto")
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
