package com.example.deberiibim_pt2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date

class ESqliteHelperPiloto(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaPiloto =
            """
                CREATE TABLE PILOTO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    fechaNacimiento VARCHAR(10),
                    numero INTEGER,
                    salario DOUBLE,
                    esTitular BOOLEAN,
                    idEscuderia INTEGER,
                    FOREIGN KEY(idEscuderia) REFERENCES ESCUDERIA(id)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaPiloto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Actualización de la base de datos (puedes agregar las migraciones necesarias si hay cambios de esquema)
    }

    fun crearPiloto(
        nombre: String,
        fechaNacimiento: String,
        numero: Int,
        salario: Double,
        esTitular: Boolean,
        idEscuderia: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("nombre", nombre)
            put("fechaNacimiento", fechaNacimiento)
            put("numero", numero)
            put("salario", salario)
            put("esTitular", esTitular)
            put("idEscuderia", idEscuderia)
        }
        val resultadoGuardar = baseDatosEscritura.insert("PILOTO", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarPiloto(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("PILOTO", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun actualizarPiloto(
        nombre: String,
        fechaNacimiento: String,
        numero: Int,
        salario: Double,
        esTitular: Boolean,
        idEscuderia: Int,
        id: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("nombre", nombre)
            put("fechaNacimiento", fechaNacimiento)
            put("numero", numero)
            put("salario", salario)
            put("esTitular", esTitular)
            put("idEscuderia", idEscuderia)
        }
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("PILOTO", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarPilotoPorId(id: Int): BPiloto? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM PILOTO WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val fechaNacimiento = resultadoConsultaLectura.getString(2)
            val numero = resultadoConsultaLectura.getInt(3)
            val salario = resultadoConsultaLectura.getDouble(4)
            val esTitular = resultadoConsultaLectura.getInt(5) == 1
            val idEscuderia = resultadoConsultaLectura.getInt(6)

            val pilotoEncontrado = BPiloto(id, nombre, fechaNacimiento, numero, salario, esTitular, idEscuderia)
            baseDatosLectura.close()
            return pilotoEncontrado
        }
        baseDatosLectura.close()
        return null
    }

    fun consultarPilotos(): ArrayList<BPiloto> {
        val scriptConsultarPilotos = "SELECT * FROM PILOTO"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarPilotos, null)
        val arregloPilotos = arrayListOf<BPiloto>()

        while (resultadoConsultaLectura.moveToNext()) {
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val fechaNacimiento = resultadoConsultaLectura.getString(2)
            val numero = resultadoConsultaLectura.getInt(3)
            val salario = resultadoConsultaLectura.getDouble(4)
            val esTitular = resultadoConsultaLectura.getInt(5) == 1
            val idEscuderia = resultadoConsultaLectura.getInt(6)
            arregloPilotos.add(BPiloto(id, nombre, fechaNacimiento, numero, salario, esTitular, idEscuderia))
        }
        baseDatosLectura.close()
        return arregloPilotos
    }

    fun consultarPilotosPorEscuderia(idEscuderia: Int): ArrayList<BPiloto> {
        val scriptConsultarPilotosPorEscuderia = "SELECT * FROM PILOTO WHERE idEscuderia = ?"
        val baseDatosLectura = readableDatabase
        val parametrosConsulta = arrayOf(idEscuderia.toString())
        val resultadoConsulta = baseDatosLectura.rawQuery(scriptConsultarPilotosPorEscuderia, parametrosConsulta)
        val arregloPilotos = arrayListOf<BPiloto>()

        while (resultadoConsulta.moveToNext()) {
            val id = resultadoConsulta.getInt(0)
            val nombre = resultadoConsulta.getString(1)
            val fechaNacimiento = resultadoConsulta.getString(2)
            val numero = resultadoConsulta.getInt(3)
            val salario = resultadoConsulta.getDouble(4)
            val esTitular = resultadoConsulta.getInt(5) == 1
            val idEscuderia = resultadoConsulta.getInt(6)
            arregloPilotos.add(BPiloto(id, nombre, fechaNacimiento, numero, salario, esTitular, idEscuderia))
        }
        baseDatosLectura.close()
        return arregloPilotos
    }
}
