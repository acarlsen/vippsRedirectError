package com.example.vippstest

import android.net.Uri
import eu.nets.pia.PiaSDK
import eu.nets.pia.wallets.PaymentProcess
import eu.nets.pia.wallets.WalletPaymentRegistration
import eu.nets.pia.wallets.WalletURLCallback

object NetaxeptHelper {

    fun startNetaxeptMobilePayPayment(
        activity: MainActivity,
        walletUrl: String?,
        paymentResult: (String) -> Unit
    ) {
        val walletProcess = PaymentProcess.mobilePay<MainActivity, MainActivity>(activity) ?: return
        if (walletUrl.isNullOrEmpty()) {
            paymentResult("WalletUrl is not set")
            return
        }

        val mobileWalletRegistration = object : WalletPaymentRegistration {
            override fun registerPayment(callbackWithWalletURL: WalletURLCallback) {
                callbackWithWalletURL.successWithWalletURL(Uri.parse(walletUrl))
            }
        }

        val canLaunch = PiaSDK.initiateMobileWallet(
            walletProcess,
            mobileWalletRegistration
        )

        if (!canLaunch) {
            paymentResult("MobilePay appSwitch can not be completed")
        }
    }

    fun startNetaxeptVippsPayment(
        activity: MainActivity,
        isTest: Boolean,
        walletUrl: String?,
        paymentResult: (String) -> Unit
    ) {
        val walletProcess =
            PaymentProcess.vipps<MainActivity, MainActivity>(isTest, activity) ?: return

        if (walletUrl.isNullOrEmpty()) {
            paymentResult("WalletUrl is not set")
            return
        }

        val mobileWalletRegistration = object : WalletPaymentRegistration {
            override fun registerPayment(callbackWithWalletURL: WalletURLCallback) {
                callbackWithWalletURL.successWithWalletURL(Uri.parse(walletUrl))
            }
        }

        val canLaunch = PiaSDK.initiateMobileWallet(
            walletProcess,
            mobileWalletRegistration
        )

        if (!canLaunch) {
            paymentResult("Vipps appSwitch can not be completed")
        }
    }
}
