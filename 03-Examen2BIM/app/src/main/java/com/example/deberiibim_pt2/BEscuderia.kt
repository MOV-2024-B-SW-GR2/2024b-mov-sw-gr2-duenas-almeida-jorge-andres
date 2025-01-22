package com.example.deberiibim_pt2

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class BEscuderia (
    val id: Int,
    var nombre: String,
    var fundacion: String,
    var nacionalidad: String,
    var esActiva: Boolean,
    var presupuesto: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(fundacion)
        parcel.writeString(nacionalidad)
        parcel.writeByte(if (esActiva) 1 else 0)
        parcel.writeDouble(presupuesto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BEscuderia> {
        override fun createFromParcel(parcel: Parcel): BEscuderia {
            return BEscuderia(parcel)
        }

        override fun newArray(size: Int): Array<BEscuderia?> {
            return arrayOfNulls(size)
        }
    }
    override fun toString(): String {
        return "${nombre} ${fundacion} ${nacionalidad} ${esActiva} ${presupuesto}"
    }
}