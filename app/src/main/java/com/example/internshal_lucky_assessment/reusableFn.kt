package com.example.internshal_lucky_assessment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.security.MessageDigest

fun showFragment(fragment: Fragment, fragmentManager: FragmentManager) {
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.commit()
}
fun hashUserId(userId: String): String {
    Log.e("hashUserIdString",userId)
    val bytes = userId.toByteArray()
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val digest = messageDigest.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

