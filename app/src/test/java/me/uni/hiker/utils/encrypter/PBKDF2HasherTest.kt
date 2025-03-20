package me.uni.hiker.utils.encrypter

import org.junit.Assert.*
import org.junit.Test


class PBKDF2HasherTest {
    @Test
    fun testPBKDF2PasswordHashing() {
        val password = "test_password"
        val hasher: Hasher = PBKDF2Hasher()

        val hash = hasher.hash(password)

        assertNotNull(hash)
    }

    @Test
    fun testPBKDF2PasswordVerification() {
        val password = "test_password"
        val hash = "mLUPVEUY8DaJSQA3KMx+Wu33IZGUMb2XTN0QlR621YM="
        val hasher: Hasher = PBKDF2Hasher()

        val result = hasher.verify(password, hash)

        assertTrue(result)
    }
}