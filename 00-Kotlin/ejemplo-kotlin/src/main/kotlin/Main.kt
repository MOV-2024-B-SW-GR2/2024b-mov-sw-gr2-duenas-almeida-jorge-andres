package org.example

import java.util.*

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args: Array<String>) {
    // INMUTABLES ( No se Re Asigna "=")
    val inmutable: String = "Jorge"
    //inmutable = "Andres" // Error

    // MUTABLES ( Se puede Re Asignar "=")
    var mutable: String = "Andres"
    mutable = "Jorge" // Ok
    // val > var

    // Duck Typing
    val ejemploVariable = "Jorge Dueñas"
    ejemploVariable.trim()
    val edadEjemplo: Int = 12
    //ejemploVariable = edadEjemplo // Error
    // Variables Primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayoEdad: Boolean = true
    // Clases en Java
    val fechaNacimiento: Date = Date()

    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito

    imprimirNombre("Jorge")

    calcularSueldo(10.00) //Solo parámetro requerido.
    calcularSueldo(10.00, 15.00, 20.00) //Parámetro requerido y sobreescribir parámetros opcionales
    // Named Parameters
    // calcularSueldo(sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00) //Usando el parametro bonoEspecial en 2do lugar
    // Gracias a los parametros nombrados
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)
    // Usando el parámetro bonoEspecial en 1ra posicion
    // Usando el parámetro sueldo en 2da posicion
    // Usando el parámetro tasa en 3ra posicion
    // Gracias a los parametros nombrados

    val sumaA = Suma(1,1)
    val sumaB = Suma(null,1)
    val sumaC = Suma(1,null)
    val sumaD = Suma(null,null)
    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    //Arreglo estático
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3,4,5,6,7,8,9,10)
    println(arregloEstatico)

    //Arreglo dinámico
    var arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
    println(arregloDinamico)

    // FOR EACH => Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> // -< = >
            println("Valor actual: ${valorActual}");
        }
    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor actual (it): ${it}")}

    // MAP -> MUTA(Modifica cambio) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un NUEVO ARREGLO con valores de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int -> // -< = >
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // Filter -> FILTRA el arreglo
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            // Expresion o condicion
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }

    val respuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno cumple la condicion)
    // AND -> ALL (Todos cumplen la condicion)
    val respuestaAny: Boolean = arregloDinamico
        .any { valorActual: Int ->
            return@any valorActual > 5
        }
    println(respuestaAny) // TRUE

    val respuestaAll: Boolean = arregloDinamico
        .all { valorActual: Int ->
            return@all valorActual > 5
        }
    println(respuestaAll) // FALSE

    // REDUCE -> Valor Acumulado
    // Valor aumulado = 0 (Siempre empiezo en 0 en Kotlin)
    // [1,2,3,4,5] -> Acumular "SUMAR" estos valores del arreglo
    // valorIteracion1 = valorEpieza + 1 = 0 + 1 = 1 -> Iteracion1
    // valorIteracion2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // valorIteracion3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    // valorIteracion4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4
    // valorIteracion5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion5
    val respuestaReduce: Int = arregloDinamico
        .reduce { acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) // Cambiar o usar la logica de negocio
        }
    println(respuestaReduce)
    // return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)

}

// Funciones
fun imprimirNombre(nombre: String): Unit {
    fun otraFuncionAdentro(): Unit {
        println("Otra Funcion Adentro")
    }
    // Template String
    println("Nombre: $nombre") // Uso sin llaves
    println("Nombre: ${nombre}") // String Template
    println("Nombre: ${nombre + nombre}") // Uso con llaves (Concatenado)
    println("Nombre: ${nombre.toString()}") // Uso con llaves (Función)
    println("Nombre: $nombre.toString()") // INCORRECTO (No se puede usar sin llaves)
    otraFuncionAdentro()
}



fun calcularSueldo(
    sueldo:Double, //Requerido
    tasa: Double = 12.00, //Opcional(defecto)
    bonoEspecial: Double? = null //Opcional(nullable)
    // Variable? -> "?" Es Nullable (osea que puede en algun momento ser nulo)
): Double {
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    if(bonoEspecial == null) {
        return sueldo * (100 / tasa)
    }else {
        return sueldo * (100 / tasa) + bonoEspecial
    }
}

abstract class Numeros ( // COnstructor Primario
    //Caso 1. Parametro normal
    // uno: Int (Parametro sin moficiador acceso)

    // Caso 2. Parametro y Propiedad (atributo)
    // private var uno: Int (Propiedad "instancia.uno")
    protected val numeroUno: Int, //Instancia numero uno
    protected val numeroDos: Int, //Instancia numero dos
    parametroNoUsadoNoPropiedadDeLaClase: Int? = null
){
    init { // bloque constructor primario opcional
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

abstract class NumerosJava {
    protected val numeroUno: Int
    private val numeroDos: Int
    constructor(
        uno: Int,
        dos: Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

class Suma( // Constructor primario
    unoParametro: Int, //Parametro
    dosParametro: Int, //Parametro
): Numeros( //Clase papá, Numeros (extendiendo)
    unoParametro,
    dosParametro
) {
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico Implicito"
    init { // Bloque del constructor primario
        this.numeroUno
        this.numeroDos
        numeroUno // this.Opcional [propiedades, metodos]
        numeroDos
        this.soyPublicoImplicito
        soyPublicoExplicito
    }

    constructor( // Constructor secundario
        uno: Int?, // Entero nullable
        dos: Int
    ):this(
        if(uno == null) 0 else uno,
        dos
    ) {
        //Bloque de codigo de constructor secundario
    }

    constructor( // Constructor secundario con nullable
        uno:Int?,
        dos:Int?
    ):this(
        if(uno == null) 0 else uno,
        if(dos == null) 0 else dos
    )

    fun sumar ():Int{
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }
    companion object { // Comparte entre todas las instancias, similar al STATIC
        // funciones, variables
        // Accesible mediante NombreClase.variable, NombreClase.funcion =>
        // Suma.pi
        val pi = 3.14
        // Suma.elevarAlCuadrado
        fun elevarAlCuadrado(numero: Int): Int {
            return numero * numero
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorTotalSuma:Int) { //Suma.agregarHistorial
            historialSumas.add(valorTotalSuma)
        }
    }
}