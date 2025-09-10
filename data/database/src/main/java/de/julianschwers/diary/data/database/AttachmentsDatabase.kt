package de.julianschwers.diary.data.database

import java.io.File
import java.io.InputStream

interface AttachmentsDatabase {
    fun exists(name: String): Boolean
    fun read(name: String): File
    fun write(name: String, data: InputStream)
}