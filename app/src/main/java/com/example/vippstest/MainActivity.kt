package com.example.vippstest

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.nets.pia.wallets.MobileWallet
import eu.nets.pia.wallets.MobileWalletError
import eu.nets.pia.wallets.MobileWalletListener

class MainActivity : ComponentActivity(), MobileWalletListener {

    // not recommended way, but fine for this example
    private val redirectEvent = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestScreen()
        }
    }

    override fun onMobileWalletAppSwitchFailure(wallet: MobileWallet, error: MobileWalletError) {
        Log.i("MainActivity", "onMobileWalletAppSwitchFailure")
        redirectEvent.value = "onMobileWalletAppSwitchFailure"
    }

    override fun onMobileWalletRedirect(wallet: MobileWallet) {
        Log.i("MainActivity", "onMobileWalletRedirect")
        redirectEvent.value = "onMobileWalletRedirect"
    }

    override fun onMobileWalletRedirectInterrupted(wallet: MobileWallet) {
        Log.i("MainActivity", "onMobileWalletRedirectInterrupted")
        redirectEvent.value = "onMobileWalletRedirectInterrupted"
    }

    @Composable
    fun TestScreen() {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val walletUrl = remember { mutableStateOf("") }
        val paymentEvent = remember { mutableStateOf("") }
        val testMode = remember { mutableStateOf(true) }

        Column(
            modifier = Modifier.scrollable(scrollState, Orientation.Vertical)
        ) {
            Text(
                text = "Enter WalletUrl:",
                modifier = Modifier.padding(16.dp)
            )
            TextField(
                value = walletUrl.value,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                maxLines = 4,
                onValueChange = { walletUrl.value = it }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Test mode",
                    modifier = Modifier.padding(16.dp)
                )
                Checkbox(
                    checked = testMode.value,
                    onCheckedChange = { testMode.value = it },
                    modifier = Modifier.padding(16.dp)
                )
            }
            Button(
                onClick = {
                    context.getActivity()?.let { activity ->
                        NetaxeptHelper.startNetaxeptVippsPayment(
                            activity,
                            testMode.value,
                            walletUrl.value
                        ) { eventText ->
                            paymentEvent.value = eventText
                        }
                    }
                }, modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "PaymentEvent: ${paymentEvent.value}",
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "RedirectEvent: ${redirectEvent.value}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


fun Context.getActivity(): MainActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is MainActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}
