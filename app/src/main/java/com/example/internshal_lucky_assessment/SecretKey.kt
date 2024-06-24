package com.example.internshal_lucky_assessment

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


fun setSecretKeyPrefs(context: Context) {

    val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "my_encrypted_prefs_file",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    with(sharedPreferences.edit()) {
        putString("secret_google_web_key", "696985627226-g5qtl2ulg7bqhngvoti5htg4aos54u94.apps.googleusercontent.com")
        apply()
    }

}
fun getSecretKey(context:Context):String?{
    val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "my_encrypted_prefs_file",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return sharedPreferences.getString("secret_google_web_key", null)

}
