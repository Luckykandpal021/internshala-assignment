package com.example.internshal_lucky_assessment

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setSecretKeyPrefs(this)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idToken = getCredentials(this)

        Log.e("idTokenFromSharedPreference",idToken.toString())
        if (idToken != null) {
            showFragment(HomeFragment(),this.supportFragmentManager)


        } else {
            showFragment(LoginFragment(),this.supportFragmentManager)
        }


    }

}