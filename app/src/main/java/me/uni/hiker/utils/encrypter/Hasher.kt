package me.uni.hiker.utils.encrypter

interface Hasher {
    fun hash(text: String): String?
    fun verify(text: String, hash: String): Boolean
}