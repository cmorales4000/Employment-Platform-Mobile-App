package co.work.papp.data

import java.io.Serializable

class AreaDato (var nombre: String): Serializable {
    override fun toString(): String {
        return nombre
    }
}