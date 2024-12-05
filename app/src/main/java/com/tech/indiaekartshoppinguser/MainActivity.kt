package com.tech.indiaekartshoppinguser

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.navigation.bottomNavigation.ShoppingNavigator
import com.tech.indiaekartshoppinguser.presentation.screens.payment.noification.NotificationPermission
import com.tech.indiaekartshoppinguser.presentation.viewmodel.AuthViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.OrderViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(),PaymentResultWithDataListener{

    private lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val authViewmodel: AuthViewmodel by viewModels()
    private val orderViewmodel: OrderViewmodel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var checkout: Checkout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()


//        Checkout.preload(applicationContext)

        // Register the launcher for result
        signInLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->
                        val credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(data)
                        val idToken = credential.googleIdToken
                        if (idToken != null) {
                            authViewmodel.firebaseWithGoogle(idToken)
                        } else {
                            Toast.makeText(this, "No ID Token found!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Sign-In Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            val navController = rememberNavController()
            val authGoogleState by authViewmodel.userAuthWithGoogleState.collectAsState()

            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {

                        ShoppingNavigator(
                            authViewmodel = authViewmodel,
                            firebaseAuth = firebaseAuth,
                            onGoogleSignInClick = {
                                authViewmodel.initiateOneTapGoogle(
                                    this@MainActivity,
                                    signInLauncher
                                )
                            },
                            navController = navController
                        )
                    }
                }
            }
            when {
                authGoogleState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MainColor)
                    }
                }

                authGoogleState.isFirebaseLogin -> {
                    Toast.makeText(
                        this,
                        "Google Login Success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    LaunchedEffect(key1 = Unit) {
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.HomeScreen) {
                                inclusive = true
                            }
                        }
                    }
                }

                authGoogleState.error.isNotEmpty() -> {
                    Toast.makeText(this, "Error: ${authGoogleState.error}", Toast.LENGTH_SHORT).show()
                }

                authGoogleState.onTapSuccess -> {
                    Toast.makeText(this, "One Tap Login Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun initPayment(payableAmount : Int) {

        checkout.setKeyID(R.string.rzr_key.toString())
        val activity = this
        try {
            val options = JSONObject()
            options.put("name","India EKartShop")
            options.put("description","Cloths Ordering App")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://images.search.yahoo.com/images/view;_ylt=Awr48ncrzU1nHPs3OFqJzbkF;_ylu=c2VjA3NyBHNsawNpbWcEb2lkAzQ2MzBkNzA0MTFmNTE2MThkYjY5Y2QyMWI2ZDMxYWVhBGdwb3MDMTMEaXQDYmluZw--?back=https%3A%2F%2Fimages.search.yahoo.com%2Fsearch%2Fimages%3Fp%3Dindia%2Bekart%2Blogo%26type%3DE210US885G0%26fr%3Dmcafee%26fr2%3Dpiv-web%26tab%3Dorganic%26ri%3D13&w=512&h=512&imgurl=play-lh.googleusercontent.com%2FVmdjZwbYHdiNyWnbZbbSSq78y-XP0QTY-9uH7cI09ZKijLQJhQx3fnKWG9nhdWJj5g&rurl=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.apextiming.ekart&size=63KB&p=india+ekart+logo&oid=4630d70411f51618db69cd21b6d31aea&fr2=piv-web&fr=mcafee&tt=eKart+-+Apps+on+Google+Play&b=0&ni=21&no=13&ts=&tab=organic&sigr=jzIZLWtPlQmM&sigb=GnlZx6Gr_nfC&sigi=MvcjfedGHhK.&sigt=khZBK.bOUbYx&.crumb=uY3/0xJzLvg&fr=mcafee&fr2=piv-web&type=E210US885G0")
            options.put("theme.color", MainColor);
            options.put("currency","INR");
            options.put("amount",payableAmount)//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","aman.nittc@gmail.com")
            prefill.put("contact","8294888127")

            options.put("prefill",prefill)
            checkout.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
        Log.d("@raz", "onPaymentError: $p1")
        // Update payment state in ViewModel
        orderViewmodel.setPaymentState(
            paymentId = p0.toString()
        )
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {

        val errorMsg = when (p0) {
            Checkout.PAYMENT_CANCELED -> "Payment was cancelled. Please try again."
            Checkout.NETWORK_ERROR -> "Network issue! Please check your connection."
            Checkout.INVALID_OPTIONS -> "Invalid payment options. Contact support."
            else -> "Payment failed! Reason: $response"
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        orderViewmodel.setPaymentState(
            errorMsg = errorMsg
        )
        Log.d("@raz", "onPaymentError: $p1")
    }
}