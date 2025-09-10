package de.julianschwers.diary.data.database.directory

import de.julianschwers.diary.data.database.AttachmentsDatabase
import java.io.File
import java.io.InputStream

class DirectoryAttachmentsDatabase(private val directory: File) : AttachmentsDatabase {
    init {
        directory.mkdirs()
        if (!directory.isDirectory) throw IllegalArgumentException("Cannot use DirectoryAttachmentsDatabase on $directory as it is not a directory.")
    }
    
    
    private fun getFile(name: String): File = File(directory, name)
    override fun exists(name: String): Boolean = getFile(name).exists()
    
    override fun read(name: String): File {
        val file = getFile(name)
        if (!file.exists()) throw IllegalArgumentException("Failed to find an attachment named $name.")
        return file
    }
    
    override fun write(name: String, data: InputStream) {
        val file = getFile(name)
        file.writeBytes(data.readBytes())
    }
}