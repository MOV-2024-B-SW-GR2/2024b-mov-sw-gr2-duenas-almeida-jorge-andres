package com.example.deberiibim_pt2

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class GGoogleMaps : AppCompatActivity() {
    private var idEscuderia = -1
    private var latCentroDesarrollo = -0.176
    private var lonCentroDesarrollo = -78.480
    private lateinit var mapa: GoogleMap
    var permisos = false
    val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        solicitarPermisos()
        inicializarLogicaMapa()
        // Obtener el ID de la escudería desde los parámetros
        idEscuderia = intent.getIntExtra("idEscuderia", -1)
        if (idEscuderia == -1) {
            finish() // Finalizar actividad si no se recibe un ID válido
            return
        }
        val escuderia = EBaseDeDatos.tablaEscuderia?.consultarEscuderiaPorId(idEscuderia)
        if (escuderia != null) {
            latCentroDesarrollo = escuderia.latCentroDesarrollo
            lonCentroDesarrollo = escuderia.lonCentroDesarrollo
            val tvCentroDesarrollo = findViewById<android.widget.TextView>(R.id.tv_centro_desarrollo)
            tvCentroDesarrollo.text = "Centro de desarrollo de ${escuderia.nombre}"
        }
    }

    fun tengoPermisos():Boolean {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        permisos = tienePermisos
        return permisos
    }
    fun solicitarPermisos(){
        if(!tengoPermisos()){
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }
    fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
                moverCentroDesarrollo()
                escucharListeners()
            }
        }
    }
    fun moverCentroDesarrollo() {
        val centroDesarrollo = LatLng(latCentroDesarrollo, lonCentroDesarrollo)
        val titulo = "centro de desarrollo"
        val marcadorCentroDesarrollo = anadirMarcador(centroDesarrollo, titulo)
        marcadorCentroDesarrollo.tag = titulo
        moverCamaraConZoom(centroDesarrollo)
    }
    fun escucharListeners() {
        mapa.setOnMarkerClickListener {
            mostrarSnackbar("setOnMarkerClickListener ${it.tag}")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraIdleListener {
            mostrarSnackbar("setOnCameraIdleListener")
        }
        mapa.setOnCameraMoveStartedListener {
            mostrarSnackbar("setOnCameraMoveStartedListener")
        }
        mapa.setOnCameraMoveListener {
            mostrarSnackbar("setOnCameraMoveListener")
        }
    }
    @SuppressLint("MissingPermission")
    fun establecerConfiguracionMapa() {
        with(mapa) {
            if(tengoPermisos()) {
                mapa.isMyLocationEnabled = true
                mapa.uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }
    fun moverCamaraConZoom(latLng: LatLng, zoom: Float = 17f) {
        mapa.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(latLng, zoom)
        )
    }
    fun anadirMarcador(latLng: LatLng, titulo: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLng).title(titulo))!!
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