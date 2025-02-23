package bewis09.communicated.util

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 128-bit AES Encryption
 */
object EncryptionUtil {
    fun encrypt(strToEncrypt: String, key: ByteArray): String {
        try {
            val secureRandom = SecureRandom()
            val iv = ByteArray(16)
            secureRandom.nextBytes(iv)

            val ivParameterSpec = IvParameterSpec(iv)
            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

            val encrypted = cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8")))
            val encryptedWithIV = ByteArray(iv.size + encrypted.size)
            System.arraycopy(iv, 0, encryptedWithIV, 0, iv.size)
            System.arraycopy(encrypted, 0, encryptedWithIV, iv.size, encrypted.size)

            return Base64.getEncoder().encodeToString(encryptedWithIV)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun decrypt(strToDecrypt: String?, key: ByteArray): String {
        try {
            if(strToDecrypt == "")
                return ""

            val encryptedWithIV = Base64.getDecoder().decode(strToDecrypt)
            val iv = ByteArray(16)
            val encrypted = ByteArray(encryptedWithIV.size - iv.size)

            System.arraycopy(encryptedWithIV, 0, iv, 0, iv.size)
            System.arraycopy(encryptedWithIV, iv.size, encrypted, 0, encrypted.size)

            val ivParameterSpec = IvParameterSpec(iv)
            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

            return String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}