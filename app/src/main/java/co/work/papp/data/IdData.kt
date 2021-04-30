package co.work.papp.data

import java.io.Serializable

class IdData (var id: String): Serializable{
    override fun toString(): String {
        return id
    }
}

