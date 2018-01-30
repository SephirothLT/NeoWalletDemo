package newandroid.zhongtuobang.com.neoandroiddemo.crypto

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by drei on 11/29/17.
 */

class Encryptor {
    private val TRANSFORM = "AES/GCM/NoPadding"
    private val ANDROID_KEY_STORE = "AndroidKeyStore"

    private var encryption: ByteArray? = null
    private var iv: ByteArray? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptText(alias: String, textToEncrypt: String): ByteArray? {
        val cipher = Cipher.getInstance(TRANSFORM)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))
        iv = cipher.iv
        encryption = cipher.doFinal(textToEncrypt.toByteArray(Charsets.UTF_8))
        return encryption
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getSecretKey(alias: String): SecretKey {
        val keygen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

        keygen.init(KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build())

        return keygen.generateKey()
    }

    fun getEncryption(): ByteArray? {
        return encryption
    }

    fun getIv(): ByteArray? {
        return iv
    }
}