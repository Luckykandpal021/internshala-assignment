package com.example.internshal_lucky_assessment

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.security.MessageDigest

fun setCredential(context: Context, idToken: String) {

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

    with(sharedPreferences.edit()) {
        putString("ID_TOKEN", idToken)
        apply()
    }

}



fun saveNotes(context: Context, userId: String, notes: MutableList<Note>) {
    Log.e("saveNotesSetCred",notes.toString())
    val encodedUserId = hashUserId(userId)

    val notesJson = Gson().toJson(notes)

    val sharedPreferences = context.getSharedPreferences("user_notes_$encodedUserId", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("NOTES", notesJson)
    editor.apply()
    Log.e("saveNotes", "Saved notesJson: $notesJson")

}


