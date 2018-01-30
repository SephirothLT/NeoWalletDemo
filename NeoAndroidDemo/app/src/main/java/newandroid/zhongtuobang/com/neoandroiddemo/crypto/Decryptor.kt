package newandroid.zhongtuobang.com.neoandroiddemo.crypto

import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by litao on 2018/1/22.
 */
class Decryptor {
    private val TRANSFORM = "AES/GCM/NoPadding"
    private val ANDROID_KEY_STORE = "AndroidKeyStore"

    private var keyStore: KeyStore? = null

    init {
        initKeyStore()
    }

    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore?.load(null)
    }

    fun decrypt(alias: String, encryptedData: ByteArray, encryptionIv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORM)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return cipher.doFinal(encryptedData).toString()
    }

    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore?.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}