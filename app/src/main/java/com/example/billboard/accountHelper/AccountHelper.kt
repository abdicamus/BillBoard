package com.example.billboard.accountHelper

import android.util.Log
import android.widget.Toast
import com.example.billboard.activities.MainActivity
import com.example.billboard.R
import com.example.billboard.consts.authErrorConsts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import java.lang.Exception

class AccountHelper(act: MainActivity) {

    private val act = act
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEmailVerification(it.result?.user!!)
                } else {
                    linkEmailToGoogleAcc(email, password)
                }
            }
        }
    }

    private fun linkEmailToGoogleAcc(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.firebaseAuth.currentUser != null) {
            act.firebaseAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.link_email_done),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(act, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exceptionHandler(exception: Exception?) {
        when (exception) {

            is FirebaseAuthInvalidCredentialsException -> {

                if (exception.errorCode == authErrorConsts.ERROR_INVALID_EMAIL) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.email_format_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else if (exception.errorCode == authErrorConsts.ERROR_WRONG_PASSWORD) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.wrong_password),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is FirebaseAuthUserCollisionException -> {

                if (exception.errorCode == authErrorConsts.ERROR_EMAIL_ALREADY_IN_USE) {
                    val user = act.firebaseAuth.currentUser
                    user?.let {
                        if (user.isEmailVerified) Toast.makeText(
                            act,
                            act.resources.getString(R.string.email_already_exist),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            is FirebaseAuthInvalidUserException -> {

                if (exception.errorCode == authErrorConsts.ERROR_USER_NOT_FOUND) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.user_not_found),
                        Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                Log.d("myLog", "${exception?.message}")
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    act.updateUI(it.result?.user)
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.welcome) + "\n $email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    exceptionHandler(it.exception)
                }
            }
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("667174139492-8sdkrq6c7coin0hdgu2pcad5jq5neegp.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun singInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.launcher.launch(intent)
    }

    fun singOutFromGoogleAcc() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.welcome),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    act,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.sign_in_verification),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(act,
                    act.resources.getString(R.string.verification_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun forgotPassword(email: String) {
        act.firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    act,
                    "Проверьте свою почту",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    act,
                    "Ошибка",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}