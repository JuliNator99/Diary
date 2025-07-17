package de.julianschwers.diary.core.util

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class NavigationArgumentRegister<T> {
    private val register = mutableMapOf<String, T>()
    
    fun exists(id: String): Boolean = register.containsKey(id)
    
    @OptIn(ExperimentalUuidApi::class)
    fun store(value: T): String {
        val id = Uuid.random().toString()
        
        store(id = id, value = value)
        return id
    }
    
    fun store(id: String, value: T) {
        if (exists(id)) throw kotlin.IllegalArgumentException("The id $id is already existing in the NavArgRegister.")
        register[id] = value
    }
    
    fun retrieve(id: String): T {
        val value = register[id] ?: throw kotlin.IllegalArgumentException("The id $id is does not exist in the NavArgRegister.")
        register.remove(id)
        return value
    }
}