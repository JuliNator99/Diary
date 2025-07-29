package de.julianschwers.diary.data.database

import java.io.InputStream
import java.io.Reader

interface AttachmentsDatabase {
    fun exists(name: String): Boolean
    fun read(name: String): InputStream
    fun write(name: String, data: Reader)
}