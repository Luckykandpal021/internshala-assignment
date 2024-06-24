package com.example.internshal_lucky_assessment

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

fun getCredentials(context:Context): String ?{
    val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_auth",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return sharedPreferences.getString("ID_TOKEN", null)
}
fun getNotes(context: Context, userId: String): MutableList<Note> {
    val encodedUserId = hashUserId(userId)
    Log.e("encodedUserIdGetNotes",encodedUserId.toString())

    val sharedPreferences = context.getSharedPreferences("user_notes_$encodedUserId", Context.MODE_PRIVATE)
    val notesJson = sharedPreferences.getString("NOTES", "[]")
    Log.e("getNotes", "userId: $userId, notesJson: $notesJson")
    val type = object : TypeToken<MutableList<Note>>() {}.type
    return Gson().fromJson(notesJson, type)
}

