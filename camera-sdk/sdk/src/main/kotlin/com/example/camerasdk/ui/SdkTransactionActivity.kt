package com.example.camerasdk.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.camerasdk.AuthCallback
import com.example.camerasdk.MySdk

class SdkTransactionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val photoPath = intent.getStringExtra(EXTRA_PHOTO_PATH).orEmpty()

        setContent {
            MaterialTheme {
                TransactionScreen(
                    photoPath = photoPath,
                    onConfirm = { authenticate() }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TransactionScreen(photoPath: String, onConfirm: () -> Unit) {
        val bitmap = remember(photoPath) {
            if (photoPath.isNotEmpty()) BitmapFactory.decodeFile(photoPath) else null
        }

        Scaffold(
            topBar = { TopAppBar(title = { Text("Confirm Payment") }) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured QR code",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Merchant", style = MaterialTheme.typography.labelMedium)
                Text(text = "Coffee Shop", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Amount", style = MaterialTheme.typography.labelMedium)
                Text(text = "Rp 50.000", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirm Payment")
                }
            }
        }
    }

    private fun authenticate() {
        MySdk.getAnalyticsProvider().sendEvent("confirm_clicked")
        MySdk.getAuthProvider().authenticate(
            activity = this,
            callback = object : AuthCallback {
                override fun onAuthenticated() {
                    startActivity(Intent(this@SdkTransactionActivity, SdkPaymentSuccessActivity::class.java))
                    finish()
                }

                override fun onAuthenticationFailed(reason: String) {
                    startActivity(
                        Intent(this@SdkTransactionActivity, SdkPaymentFailedActivity::class.java)
                            .putExtra(SdkPaymentFailedActivity.EXTRA_FAILURE_REASON, reason)
                    )
                    finish()
                }

                override fun onAuthenticationCancelled() {
                    Toast.makeText(
                        this@SdkTransactionActivity,
                        "Payment cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        )
    }

    companion object {
        const val EXTRA_PHOTO_PATH = "sdk_photo_path"
    }
}
