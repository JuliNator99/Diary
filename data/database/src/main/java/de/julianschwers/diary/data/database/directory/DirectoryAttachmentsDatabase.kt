package de.julianschwers.diary.data.database.directory

import de.julianschwers.diary.data.database.AttachmentsDatabase
import java.io.File
import java.io.Reader

class DirectoryAttachmentsDatabase(private val directory: File) : AttachmentsDatabase {
    init {
        directory.mkdirs()
        if (!directory.isDirectory) throw IllegalArgumentException("Cannot use DirectoryAttachmentsDatabase on $directory as it is not a directory.")
    }
    
    
    private fun getFile(name: String): File = File(directory, name)
    override fun exists(name: String): Boolean = getFile(name).exists()
    
    override fun read(name: String): Reader {
        val file = getFile(name)
        return file.bufferedReader()
    }
    
    override fun write(name: String, data: Reader) {
        val file = getFile(name)
        val writer = file.bufferedWriter()
        
        while (data.ready()) writer.write(data.read())
    }
}