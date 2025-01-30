package com.example.deberiibim_pt2

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class BPiloto (
    val id: Int,
    var nombre: String,
    var fechaNacimiento: String,
    var numero: Int,
    var salario: Double,
    var esTitular: Boolean,
    var idEscuderia: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(fechaNacimiento)
        parcel.writeInt(numero)
        parcel.writeDouble(salario)
        parcel.writeByte(if (esTitular) 1 else 0)
        parcel.writeInt(idEscuderia)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BPiloto> {
        override fun createFromParcel(parcel: Parcel): BPiloto {
            return BPiloto(parcel)
        }

        override fun newArray(size: Int): Array<BPiloto?> {
            return arrayOfNulls(size)
        }
    }
    override fun toString(): String {
        return "Nombre: ${nombre} \nNacimiento: ${fechaNacimiento} \nNúmero: ${numero} \nSalario: ${salario} \nTitular: ${esTitular} \nIdEscuderia: ${idEscuderia}"
    }
}