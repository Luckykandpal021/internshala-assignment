package com.example.internshal_lucky_assessment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
class LoginFragment : Fragment() {
    private  var getWebKey:String?=null
    private lateinit var credentialManager: CredentialManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = context?.let { CredentialManager.create(it) }!!


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_login, container, false)
        val sign_in_btn=view.findViewById<androidx.cardview.widget.CardView>(R.id.sign_in_btn)
        getWebKey=getSecretKey(requireContext())

        sign_in_btn.setOnClickListener {
            if (getWebKey!=null){
            lifecycleScope.launch {
                signInWithGoogle(getWebKey!!)
            }}
        }




        return  view
    }

    private suspend fun signInWithGoogle(webKey:String) {

        val rowNone= UUID.randomUUID().toString()
        val bytes=rowNone.toByteArray()
        val md= MessageDigest.getInstance("SHA-256")
        val digest=md.digest(bytes)
        val hashedNounce=digest.fold(""){str,it->str+"%02x".format(it)}

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webKey)
            .setNonce(hashedNounce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = withContext(Dispatchers.IO) {
                credentialManager.getCredential(
                    request = request,
                    context = requireContext(),
                )
            }
            val credential = result.credential

            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            val googleIdToken = googleIdTokenCredential.idToken

                            Log.e("googleIdTokenLogin",googleIdToken.toString())
                            setCredential(requireContext(), googleIdToken)

                            showFragment(HomeFragment(),requireActivity().supportFragmentManager)

                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(
                                "GoogleIdTokenParsingExceptionError",
                                "Received an invalid google id token response",
                                e
                            )
                        }
                    } else {
                        Log.e("TYPE_GOOGLE_ID_TOKEN_CREDENTIALErorr", "Unexpected type of credential")
                    }
                }

                else -> {
                    Log.e("CustomCredentialError", "Unexpected type of credential")
                }
            }

        } catch (e: GetCredentialException) {
            Log.e("GetCredentialExceptionCredentialManager",e.message.toString())
        }
    }}

