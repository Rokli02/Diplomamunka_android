package me.uni.hiker.utils.encrypter

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PBKDF2Hasher: Hasher {
    override fun hash(text: String): String? {
        val random = SecureRandom()
        val salt = ByteArray(SIZE_OF_SALT)
        random.nextBytes(salt)

        val spec: KeySpec = PBEKeySpec(text.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(AGR)

        val hash =  factory.generateSecret(spec).encoded ?: return null

        val result = ByteArray(SIZE_OF_SALT + hash.size)
        salt.copyInto(result, 0, 0, SIZE_OF_SALT)
        hash.copyInto(result, SIZE_OF_SALT, 0, hash.size)

        return Base64.getEncoder().encode(result).decodeToString()
    }

    override fun verify(text: String, hash: String): Boolean {
        val decodedHash = Base64.getDecoder().decode(hash)
        val salt = ByteArray(SIZE_OF_SALT)
        val extractedHashByteArray = ByteArray(decodedHash.size - SIZE_OF_SALT)

        decodedHash.copyInto(salt, 0, 0, SIZE_OF_SALT)
        decodedHash.copyInto(extractedHashByteArray, 0, SIZE_OF_SALT)

        val spec: KeySpec = PBEKeySpec(text.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(AGR)

        val hashForVerification =  factory.generateSecret(spec).encoded?.decodeToString()

        return extractedHashByteArray.decodeToString() == hashForVerification
    }

    companion object {
        private const val SIZE_OF_SALT = 16
        private const val ITERATION_COUNT = 1024
        private const val KEY_LENGTH = 128
        private const val AGR = "PBKDF2WithHmacSHA1"
    }
}