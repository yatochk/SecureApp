package com.yatochk.secure.app.model

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Base64InputStream
import java.io.*
import java.math.BigInteger
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.x500.X500Principal

@Singleton
class Cypher @Inject constructor(private val context: Context) {

    companion object {
        private const val KEY_STORE = "AndroidKeyStore"
        private const val RSA_ALIAS = "image_alias"
        private const val AES_MODE = "AES/ECB/PKCS7Padding"
        private const val SHARED_PREFERENCE_NAME = "KeyPreference"
        private const val ENCRYPTED_KEY = "secretKey"
        private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        private val keyStore = KeyStore.getInstance(KEY_STORE).apply {
            load(null)
        }
    }

    private val secretKey = getSecretKey()

    fun encrypt(bytes: ByteArray): ByteArray {
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = c.doFinal(bytes)
        return Base64.encode(encryptedBytes, Base64.DEFAULT)
    }

    @Throws(IllegalBlockSizeException::class)
    fun decrypt(bytes: ByteArray): ByteArray {
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.DECRYPT_MODE, secretKey)
        return c.doFinal(Base64.decode(bytes, Base64.DEFAULT))
    }

    @Throws(IllegalBlockSizeException::class)
    fun decryptFile(input: FileInputStream, output: FileOutputStream) {
        val buf = ByteArray(1024 * 1024)
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.DECRYPT_MODE, secretKey)

        val cipherInput =
            BufferedInputStream(CipherInputStream(Base64InputStream(input, Base64.DEFAULT), c))
        var read: Int
        while (cipherInput.read(buf).also { read = it } > 0) {
            output.write(buf, 0, read)
        }

        input.close()
        output.close()
    }

    private fun getRSAKey(): KeyStore.PrivateKeyEntry {
        if (!keyStore.containsAlias(RSA_ALIAS)) {
            RSA_ALIAS.createRSAKey()
        }
        return keyStore.getEntry(RSA_ALIAS, null) as KeyStore.PrivateKeyEntry
    }

    private fun String.createRSAKey() {
        val notBefore = Calendar.getInstance()
        val notAfter = Calendar.getInstance()
        notAfter.add(Calendar.YEAR, 20)

        val keyGenerator = KeyPairGenerator
            .getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                KEY_STORE
            )
        keyGenerator.initialize(
            KeyPairGeneratorSpec.Builder(context)
                .setAlias(this)
                .setSubject(X500Principal("CN=${this}"))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(notBefore.time)
                .setEndDate(notAfter.time)
                .build()
        )
        keyGenerator.generateKeyPair()
    }

    private fun rsaEncrypt(secret: ByteArray): ByteArray {
        val privateKeyEntry = getRSAKey()
        val inputCipher = Cipher.getInstance(RSA_MODE)
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        return outputStream.toByteArray()
    }

    private fun rsaDecrypt(encrypted: ByteArray): ByteArray {
        val privateKeyEntry = getRSAKey()
        val output = Cipher.getInstance(RSA_MODE)
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(ByteArrayInputStream(encrypted), output)

        val values = ArrayList<Byte>()
        var nextByte = cipherInputStream.read()
        while (nextByte != -1) {
            values.add(nextByte.toByte())
            nextByte = cipherInputStream.read()
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }

        return bytes
    }

    private fun createSecretKey() {
        val pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        var encryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null)
        if (encryptedKeyB64 == null) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncrypt(key)
            encryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString(ENCRYPTED_KEY, encryptedKeyB64)
            edit.commit()
        }
    }

    private fun getSecretKey(): Key {
        val pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        var encryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null)
        if (encryptedKeyB64 == null) {
            createSecretKey()
            encryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null)
        }
        val encryptedKey = Base64.decode(encryptedKeyB64, Base64.DEFAULT)
        val key = rsaDecrypt(encryptedKey)
        return SecretKeySpec(key, "AES")
    }
}
