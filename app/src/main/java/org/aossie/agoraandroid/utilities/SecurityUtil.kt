package org.aossie.agoraandroid.utilities

import android.util.Base64
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class SecurityUtil(
  private val secretKey: String
) {

  fun encryptToken(token: String): String? {
    try {
      val ivParameterSpec = IvParameterSpec(Base64.decode(AppConstants.IV, Base64.DEFAULT))

      val factory = SecretKeyFactory.getInstance(AppConstants.SECRET_KEY_FACTORY_ALGORITHM)
      val spec =
        PBEKeySpec(
          secretKey.toCharArray(), Base64.decode(AppConstants.SALT, Base64.DEFAULT), 10000, 256
        )
      val tmp = factory.generateSecret(spec)
      val secretKey = SecretKeySpec(tmp.encoded, AppConstants.SECRET_KEY_SPEC_ALGORITHM)

      val cipher = Cipher.getInstance(AppConstants.CIPHER_TRANSFORMATION)
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
      return Base64.encodeToString(
        cipher.doFinal(token.toByteArray(Charsets.UTF_8)), Base64.DEFAULT
      )
    } catch (e: Exception) {
      Timber.d(e.toString())
    }
    return null
  }

  fun decryptToken(encryptedToken: String): String? {
    try {

      val ivParameterSpec = IvParameterSpec(Base64.decode(AppConstants.IV, Base64.DEFAULT))

      val factory = SecretKeyFactory.getInstance(AppConstants.SECRET_KEY_FACTORY_ALGORITHM)
      val spec =
        PBEKeySpec(
          secretKey.toCharArray(), Base64.decode(AppConstants.SALT, Base64.DEFAULT), 10000, 256
        )
      val tmp = factory.generateSecret(spec)
      val secretKey = SecretKeySpec(tmp.encoded, AppConstants.SECRET_KEY_SPEC_ALGORITHM)

      val cipher = Cipher.getInstance(AppConstants.CIPHER_TRANSFORMATION)
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
      return String(cipher.doFinal(Base64.decode(encryptedToken, Base64.DEFAULT)))
    } catch (e: Exception) {
      Timber.d(e.toString())
    }
    return null
  }
}
