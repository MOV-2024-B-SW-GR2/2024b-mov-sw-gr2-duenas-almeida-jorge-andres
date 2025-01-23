package com.example.gr2sw2024b_jada

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
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

class GGoogleMaps : AppCompatActivity() {
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
        moverQuicentro()
        anadirPolilinea()
        anadirPoligono()
        escucharListeners()
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
    fun solicitarPermisos() {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        if (tienePermisos) {
            permisos = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(nombrePermisoFine, nombrePermisoCoarse),
                1
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
            }
        }
    }
    fun moverQuicentro() {
        val quicentro = LatLng(-0.176, -78.480)
        val titulo = "Quicentro"
        val marcadorQuicentro = anadirMarcador(quicentro, titulo)
        marcadorQuicentro.tag = titulo
        moverCamaraConZoom(quicentro)
    }
    fun anadirPolilinea() {
        with(mapa) {
            val polilinea = addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .add(
                        LatLng(-0.176, -78.480),
                        LatLng(-0.176, -78.481),
                        LatLng(-0.177, -78.481),
                        LatLng(-0.177, -78.480),
                        LatLng(-0.176, -78.480)
                    )
            )
            polilinea.tag = "polilinea-uno"
        }
    }
    fun anadirPoligono() {
        with(mapa) {
            val poligono = mapa.addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .add(
                        LatLng(-0.176, -78.480),
                        LatLng(-0.176, -78.481),
                        LatLng(-0.177, -78.481),
                        LatLng(-0.177, -78.480),
                        LatLng(-0.176, -78.480)
                    )
            )
            poligono.tag = "poligono-uno"
        }
    }
    fun escucharListeners() {
        mapa.setOnPolylineClickListener {
            mostrarSnackbar("setOnPolylineClickListener ${it.tag}")
        }
        mapa.setOnPolygonClickListener {
            mostrarSnackbar("setOnPolygonClickListener ${it.tag}")
        }
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
    fun anadirMarcador(latLng: LatLng, titulo: String):Marker {
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