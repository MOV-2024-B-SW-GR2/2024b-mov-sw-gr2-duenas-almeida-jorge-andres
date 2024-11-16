package org.example

import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase Escuderia que representa una escudería en el sistema.
 * Incluye la serialización para almacenar y recuperar objetos de tipo Escuderia.
 */
data class Escuderia(
    val id: Int,
    var nombre: String,
    var fundacion: Date,
    var nacionalidad: String,
    var esActiva: Boolean,
    var presupuesto: Double
) : Serializable

/**
 * Clase Piloto que representa a un piloto en el sistema.
 * También está preparada para serialización.
 */
data class Piloto(
    val id: Int,
    var nombre: String,
    var fechaNacimiento: Date,
    var numero: Int,
    var salario: Double,
    var esTitular: Boolean
) : Serializable

// Constantes para los archivos de almacenamiento de datos
const val ESCUDERIAS_FILE = "escuderias.bin"
const val PILOTOS_FILE = "pilotos.bin"

fun main() {
    // Cargar las escuderías y pilotos previamente guardados al iniciar el programa
    val escuderias = cargarEscuderias()
    val pilotos = cargarPilotos()

    val scanner = Scanner(System.`in`)
    while (true) {
        println("\n--- Menú Principal ---")
        println("1. Crear Escudería")
        println("2. Leer Escuderías")
        println("3. Actualizar Escudería")
        println("4. Eliminar Escudería")
        println("5. Gestionar Pilotos")
        println("6. Salir")
        print("Seleccione una opción: ")

        // Leer la opción del menú y ejecutar la acción correspondiente
        when (scanner.nextLine().toIntOrNull()) {
            1 -> crearEscuderia(escuderias, pilotos, scanner)
            2 -> leerEscuderias(escuderias, pilotos)
            3 -> actualizarEscuderia(escuderias, scanner)
            4 -> eliminarEscuderia(escuderias, pilotos, scanner)
            5 -> gestionarPilotos(escuderias, pilotos)
            6 -> {
                // Guardar las escuderías y pilotos al salir del programa
                guardarEscuderias(escuderias)
                guardarPilotos(pilotos)
                println("Datos guardados. ¡Hasta luego!")
                break
            }
            else -> println("Opción inválida. Intente nuevamente.")
        }
    }
}

/**
 * Guarda la lista de escuderías en un archivo binario.
 */
fun guardarEscuderias(escuderias: List<Escuderia>) {
    try {
        ObjectOutputStream(FileOutputStream(ESCUDERIAS_FILE)).use { it.writeObject(escuderias) }
        println("Escuderías guardadas exitosamente.")
    } catch (e: Exception) {
        println("Error al guardar las escuderías: ${e.message}")
    }
}

/**
 * Carga las escuderías desde un archivo binario.
 * Si no existe el archivo, devuelve una lista vacía.
 */
fun cargarEscuderias(): MutableList<Escuderia> {
    return try {
        ObjectInputStream(FileInputStream(ESCUDERIAS_FILE)).use { it.readObject() as MutableList<Escuderia> }
    } catch (e: Exception) {
        println("No se encontraron datos de escuderías previas.")
        mutableListOf() // Retorna una lista vacía si no se encuentra el archivo
    }
}

/**
 * Guarda el mapa de pilotos en un archivo binario.
 */
fun guardarPilotos(pilotos: Map<Int, List<Piloto>>) {
    try {
        ObjectOutputStream(FileOutputStream(PILOTOS_FILE)).use { it.writeObject(pilotos) }
        println("Pilotos guardados exitosamente.")
    } catch (e: Exception) {
        println("Error al guardar los pilotos: ${e.message}")
    }
}

/**
 * Carga los pilotos desde un archivo binario.
 * Si no existe el archivo, devuelve un mapa vacío.
 */
fun cargarPilotos(): MutableMap<Int, MutableList<Piloto>> {
    return try {
        ObjectInputStream(FileInputStream(PILOTOS_FILE)).use {
            it.readObject() as MutableMap<Int, MutableList<Piloto>>
        }
    } catch (e: Exception) {
        println("No se encontraron datos de pilotos previos.")
        mutableMapOf() // Retorna un mapa vacío si no se encuentra el archivo
    }
}

/**
 * Crea una nueva escudería y la agrega a la lista de escuderías.
 * Realiza validaciones en la entrada de datos.
 */
fun crearEscuderia(
    escuderias: MutableList<Escuderia>,
    pilotos: MutableMap<Int, MutableList<Piloto>>,
    scanner: Scanner
) {
    // Validación para el ID de la escudería
    print("Ingrese el ID de la escudería: ")
    val id = scanner.nextLine().toIntOrNull() ?: run {
        println("ID inválido.")
        return
    }

    // Solicitar y validar el nombre de la escudería
    print("Ingrese el nombre de la escudería: ")
    val nombre = scanner.nextLine()

    // Validación de la fecha de fundación
    print("Ingrese la fecha de fundación (formato dd/MM/yyyy): ")
    val fundacion = try {
        SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine())
    } catch (e: Exception) {
        println("Fecha inválida.")
        return
    }

    // Solicitar la nacionalidad
    print("Ingrese la nacionalidad de la escudería: ")
    val nacionalidad = scanner.nextLine()

    // Validación del estado activo de la escudería
    print("¿La escudería está activa? (true/false): ")
    val esActiva = scanner.nextLine().toBooleanStrictOrNull() ?: run {
        println("Valor inválido.")
        return
    }

    // Validación del presupuesto de la escudería
    print("Ingrese el presupuesto de la escudería: ")
    val presupuesto = scanner.nextLine().toDoubleOrNull() ?: run {
        println("Presupuesto inválido.")
        return
    }

    // Crear y agregar la nueva escudería a la lista
    val nuevaEscuderia = Escuderia(id, nombre, fundacion, nacionalidad, esActiva, presupuesto)
    escuderias.add(nuevaEscuderia)
    pilotos[id] = mutableListOf() // Inicializar lista vacía de pilotos para esta escudería

    println("Escudería creada exitosamente.")
}

/**
 * Muestra la lista de escuderías y los pilotos asignados a ellas.
 */
fun leerEscuderias(escuderias: List<Escuderia>, pilotos: Map<Int, List<Piloto>>) {
    if (escuderias.isEmpty()) {
        println("No hay escuderías registradas.")
        return
    }

    println("\n--- Escuderías ---")
    escuderias.forEach { escuderia ->
        println("ID: ${escuderia.id}, Nombre: ${escuderia.nombre}, Fundada: ${escuderia.fundacion}, Nacionalidad: ${escuderia.nacionalidad}, Activa: ${escuderia.esActiva}, Presupuesto: ${escuderia.presupuesto}")
        val listaPilotos = pilotos[escuderia.id]
        if (!listaPilotos.isNullOrEmpty()) {
            println("   Pilotos:")
            listaPilotos.forEach { piloto ->
                println("   - ID: ${piloto.id}, Nombre: ${piloto.nombre}, Nacimiento: ${piloto.fechaNacimiento}, Número: ${piloto.numero}, Salario: ${piloto.salario}, Titular: ${piloto.esTitular}")
            }
        } else {
            println("   No tiene pilotos asignados.")
        }
    }
}

/**
 * Actualiza una escudería existente, permitiendo cambiar el nombre y estado activo.
 */
fun actualizarEscuderia(escuderias: MutableList<Escuderia>, scanner: Scanner) {
    if (escuderias.isEmpty()) {
        println("No hay escuderías registradas.")
        return
    }

    // Mostrar escuderías disponibles
    println("\n--- Escuderías Disponibles ---")
    escuderias.forEach { println("ID: ${it.id}, Nombre: ${it.nombre}") }

    // Solicitar el ID de la escudería a actualizar
    print("Ingrese el ID de la escudería a actualizar: ")
    val id = scanner.nextLine().toIntOrNull()
    val escuderia = escuderias.find { it.id == id }
    if (escuderia != null) {
        // Permitir cambiar el nombre y el estado activo
        print("Ingrese el nuevo nombre (${escuderia.nombre}): ")
        escuderia.nombre = scanner.nextLine()
        print("¿La escudería está activa? (${escuderia.esActiva}): ")
        escuderia.esActiva = scanner.nextLine().toBooleanStrictOrNull() ?: escuderia.esActiva
        println("Escudería actualizada exitosamente.")
    } else {
        println("Escudería no encontrada.")
    }
}

/**
 * Elimina una escudería y todos sus pilotos asociados.
 */
fun eliminarEscuderia(
    escuderias: MutableList<Escuderia>,
    pilotos: MutableMap<Int, MutableList<Piloto>>,
    scanner: Scanner
) {
    if (escuderias.isEmpty()) {
        println("No hay escuderías registradas.")
        return
    }

    // Mostrar las escuderías disponibles
    println("\n--- Escuderías Disponibles ---")
    escuderias.forEach { println("ID: ${it.id}, Nombre: ${it.nombre}") }

    // Solicitar el ID de la escudería a eliminar
    print("Ingrese el ID de la escudería a eliminar: ")
    val id = scanner.nextLine().toIntOrNull()

    // Eliminar la escudería y sus pilotos asociados
    if (escuderias.removeIf { it.id == id }) {
        pilotos.remove(id) // Eliminar la lista de pilotos asociada a la escudería
        println("Escudería eliminada exitosamente.")
    } else {
        println("Escudería no encontrada.")
    }
}

/**
 * Función para gestionar los pilotos: permite crear, leer, actualizar o eliminar pilotos.
 */
fun gestionarPilotos(escuderias: List<Escuderia>, pilotos: MutableMap<Int, MutableList<Piloto>>) {
    val scanner = Scanner(System.`in`)

    while (true) {
        println("\n--- Gestión de Pilotos ---")
        println("1. Crear Piloto")
        println("2. Leer Pilotos")
        println("3. Actualizar Piloto")
        println("4. Eliminar Piloto")
        println("5. Volver")
        print("Seleccione una opción: ")

        when (scanner.nextInt()) {
            1 -> crearPiloto(pilotos)
            2 -> leerPilotos(pilotos)
            3 -> {
                // Solicitar ID de la escudería para actualizar pilotos
                print("Ingrese el ID de la escudería para los pilotos: ")
                val escuderiaId = scanner.nextInt()
                val listaPilotos = pilotos[escuderiaId]
                if (listaPilotos != null) {
                    actualizarPiloto(listaPilotos)
                } else {
                    println("Escudería no encontrada.")
                }
            }
            4 -> {
                // Solicitar ID de la escudería para eliminar pilotos
                print("Ingrese el ID de la escudería para los pilotos: ")
                val escuderiaId = scanner.nextInt()
                val listaPilotos = pilotos[escuderiaId]
                if (listaPilotos != null) {
                    eliminarPiloto(listaPilotos)
                } else {
                    println("Escudería no encontrada.")
                }
            }
            5 -> break
            else -> println("Opción inválida.")
        }
    }
}

/**
 * Crea un nuevo piloto y lo asigna a una escudería.
 * Realiza las validaciones necesarias.
 */
fun crearPiloto(pilotos: MutableMap<Int, MutableList<Piloto>>) {
    val scanner = Scanner(System.`in`)

    // Mostrar escuderías disponibles para asignar pilotos
    println("\n--- Escuderías Disponibles ---")
    pilotos.forEach { (escuderiaId, _) ->
        println("ID: $escuderiaId")
    }
    print("Ingrese el ID de la escudería para el piloto: ")
    val escuderiaId = scanner.nextInt()
    if (!pilotos.containsKey(escuderiaId)) {
        println("Escudería no encontrada.")
        return
    }

    // Solicitar los detalles del piloto
    print("Ingrese el ID del piloto: ")
    val id = scanner.nextInt()
    scanner.nextLine() // Limpiar el buffer
    print("Ingrese el nombre del piloto: ")
    val nombre = scanner.nextLine()

    // Validación de la fecha de nacimiento
    print("Ingrese la fecha de nacimiento (formato dd/MM/yyyy): ")
    val fechaNacimiento = try {
        SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine())
    } catch (e: Exception) {
        println("Fecha inválida.")
        return
    }

    // Solicitar el número de piloto
    print("Ingrese el número del piloto: ")
    val numero = scanner.nextInt()

    // Solicitar el salario del piloto
    print("Ingrese el salario del piloto: ")
    val salario = scanner.nextDouble()

    // Validación si el piloto es titular o no
    print("¿Es piloto titular? (true/false): ")
    val esTitular = scanner.nextBoolean()

    // Crear el nuevo piloto y añadirlo a la lista de la escudería correspondiente
    val nuevoPiloto = Piloto(id, nombre, fechaNacimiento, numero, salario, esTitular)
    pilotos[escuderiaId]?.add(nuevoPiloto)

    println("Piloto creado exitosamente.")
}

/**
 * Muestra la lista de pilotos asignados a las escuderías.
 */
fun leerPilotos(pilotos: Map<Int, List<Piloto>>) {
    if (pilotos.isEmpty()) {
        println("No hay pilotos registrados.")
        return
    }

    println("\n--- Pilotos ---")
    pilotos.forEach { (escuderiaId, listaPilotos) ->
        println("Escudería ID: $escuderiaId")
        listaPilotos.forEach { piloto ->
            println("   - ID: ${piloto.id}, Nombre: ${piloto.nombre}, Nacimiento: ${piloto.fechaNacimiento}, Número: ${piloto.numero}, Salario: ${piloto.salario}, Titular: ${piloto.esTitular}")
        }
    }
}

/**
 * Elimina un piloto de la lista de pilotos de una escudería.
 */
fun eliminarPiloto(listaPilotos: MutableList<Piloto>) {
    if (listaPilotos.isEmpty()) {
        println("No hay pilotos registrados.")
        return
    }

    // Mostrar los pilotos disponibles para eliminar
    println("\n--- Pilotos Disponibles ---")
    listaPilotos.forEach { println("ID: ${it.id}, Nombre: ${it.nombre}") }

    val scanner = Scanner(System.`in`)
    print("Ingrese el ID del piloto a eliminar: ")
    val id = scanner.nextInt()

    // Eliminar el piloto de la lista
    if (listaPilotos.removeIf { it.id == id }) {
        println("Piloto eliminado exitosamente.")
    } else {
        println("Piloto no encontrado.")
    }
}

/**
 * Actualiza los datos de un piloto (nombre y número).
 */
fun actualizarPiloto(listaPilotos: MutableList<Piloto>) {
    if (listaPilotos.isEmpty()) {
        println("No hay pilotos registrados.")
        return
    }

    // Mostrar los pilotos disponibles para actualización
    println("\n--- Pilotos Disponibles ---")
    listaPilotos.forEach { println("ID: ${it.id}, Nombre: ${it.nombre}") }

    val scanner = Scanner(System.`in`)
    print("Ingrese el ID del piloto a actualizar: ")
    val id = scanner.nextInt()

    // Buscar el piloto a actualizar
    val piloto = listaPilotos.find { it.id == id }
    if (piloto != null) {
        // Solicitar nuevos datos para actualizar
        print("Ingrese el nuevo número (${piloto.numero}): ")
        piloto.numero = scanner.nextInt()
        scanner.nextLine() // Limpiar el buffer
        print("Ingrese el nuevo nombre (${piloto.nombre}): ")
        piloto.nombre = scanner.next()

        println("Piloto actualizado exitosamente.")
    } else {
        println("Piloto no encontrado.")
    }
}
