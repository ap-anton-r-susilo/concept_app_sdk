package com.example.authsdk

import android.app.Activity
import android.app.AlertDialog
import android.text.InputType
import android.widget.EditText

class SimpleAuthProvider : AuthProvider {

    private var authenticated: Boolean = false

    override fun authenticate(activity: Activity, callback: AuthCallback) {
        authenticated = false
        val input = EditText(activity).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
            hint = "Enter PIN"
        }

        AlertDialog.Builder(activity)
            .setTitle("Authentication Required")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Verify") { _, _ ->
                val pin = input.text?.toString().orEmpty()
                if (pin == "1234") {
                    authenticated = true
                    callback.onAuthenticated()
                } else {
                    callback.onAuthenticationFailed("Incorrect PIN")
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                callback.onAuthenticationCancelled()
            }
            .show()
    }

    override fun isAuthenticated(): Boolean = authenticated
}
