package com.example.deberiibim_pt2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date

class ESqliteHelperEscuderia(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEscuderia =
            """
                CREATE TABLE ESCUDERIA(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    fundacion VARCHAR(10),
                    nacionalidad VARCHAR(50),
                    esActiva BOOLEAN,
                    presupuesto DOUBLE,
                    latCentroDesarrollo DOUBLE,
                    lonCentroDesarrollo DOUBLE
                )
            """.trimIndent()
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
        db?.execSQL(scriptSQLCrearTablaEscuderia)
        db?.execSQL(scriptSQLCrearTablaPiloto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun crearEscuderia(
        nombre: String,
        fundacion: String,
        nacionalidad: String,
        esActiva: Boolean,
        presupuesto: Double,
        latCentroDesarrollo: Double,
        lonCentroDesarrollo: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("nombre", nombre)
            put("fundacion", fundacion)
            put("nacionalidad", nacionalidad)
            put("esActiva", esActiva)
            put("presupuesto", presupuesto)
            put("latCentroDesarrollo", latCentroDesarrollo)
            put("lonCentroDesarrollo", lonCentroDesarrollo)
        }
        val resultadoGuardar = baseDatosEscritura.insert("ESCUDERIA", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarEscuderia(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("ESCUDERIA", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun actualizarEscuderia(
        nombre: String,
        fundacion: String,
        nacionalidad: String,
        esActiva: Boolean,
        presupuesto: Double,
        latCentroDesarrollo: Double,
        lonCentroDesarrollo: Double,
        id: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("nombre", nombre)
            put("fundacion", fundacion)
            put("nacionalidad", nacionalidad)
            put("esActiva", esActiva)
            put("presupuesto", presupuesto)
            put("latCentroDesarrollo", latCentroDesarrollo)
            put("lonCentroDesarrollo", lonCentroDesarrollo)
        }
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("ESCUDERIA", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarEscuderiaPorId(id: Int): BEscuderia? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM ESCUDERIA WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val fundacion = resultadoConsultaLectura.getString(2)
            val nacionalidad = resultadoConsultaLectura.getString(3)
            val esActiva = resultadoConsultaLectura.getInt(4) == 1
            val presupuesto = resultadoConsultaLectura.getDouble(5)
            val latCentroDesarrollo = resultadoConsultaLectura.getDouble(6)
            val lonCentroDesarrollo = resultadoConsultaLectura.getDouble(7)
            baseDatosLectura.close()
            return BEscuderia(id, nombre, fundacion, nacionalidad, esActiva, presupuesto, latCentroDesarrollo, lonCentroDesarrollo)
        }
        baseDatosLectura.close()
        return null
    }

    fun consultarEscuderias(): ArrayList<BEscuderia> {
        val scriptConsultarEscuderias = "SELECT * FROM ESCUDERIA"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultarEscuderias, null)
        val arregloEscuderias = arrayListOf<BEscuderia>()

        while (resultadoConsultaLectura.moveToNext()) {
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val fundacion = resultadoConsultaLectura.getString(2)
            val nacionalidad = resultadoConsultaLectura.getString(3)
            val esActiva = resultadoConsultaLectura.getInt(4) == 1
            val presupuesto = resultadoConsultaLectura.getDouble(5)
            val latCentroDesarrollo = resultadoConsultaLectura.getDouble(6)
            val lonCentroDesarrollo = resultadoConsultaLectura.getDouble(7)
            arregloEscuderias.add(BEscuderia(id, nombre, fundacion, nacionalidad, esActiva, presupuesto, latCentroDesarrollo, lonCentroDesarrollo))
        }
        baseDatosLectura.close()
        return arregloEscuderias
    }
}
