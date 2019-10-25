package dev.phoenixxt.domain

import dev.phoenixxt.domain.internal.Bank
import dev.phoenixxt.domain.internal.ThreadManager
import dev.phoenixxt.domain.internal.TimeProvider


object RemoteBank {

    private val bank = Bank(TimeProvider())

    private val threadManager = ThreadManager()

    val listOfTransactions: List<Transaction> = bank.listOfTransactions

    val currentAmountOfMoney
        get() = bank.currentAmountOfMoney

    fun subtract(amount: Long, callback: Callback) {
        threadManager.awaitAndDo {
            try {
                bank.subtract(amount)
                callback.onSuccess()
            } catch (exception: NotEnoughMoneyException) {
                callback.onError(exception)
            }
        }
    }

    fun add(amount: Long, callback: Callback) {
        threadManager.awaitAndDo {
            bank.add(amount)
            callback.onSuccess()
        }
    }
}