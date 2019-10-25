package dev.phoenixxt.conferenceproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import dev.phoenixxt.domain.Callback
import dev.phoenixxt.domain.RemoteBank

//use static RemoteBank

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("money = ${RemoteBank.currentAmountOfMoney}")

        RemoteBank.add(1000, object : Callback {
            override fun onSuccess() {
                println("success")
            }

            override fun onError(exception: Exception) {

            }
        })

        RemoteBank.add(1000, object : Callback {
            override fun onSuccess() {
                println("success")
            }

            override fun onError(exception: Exception) {

            }
        })

        RemoteBank.subtract(1000, object : Callback {
            override fun onSuccess() {
                println("success")
                println(RemoteBank.listOfTransactions)
            }

            override fun onError(exception: Exception) {

            }
        })

        RemoteBank.subtract(345345345, object : Callback {
            override fun onSuccess() {

            }

            override fun onError(exception: Exception) {
                println("error :(")
            }
        })
    }
}
