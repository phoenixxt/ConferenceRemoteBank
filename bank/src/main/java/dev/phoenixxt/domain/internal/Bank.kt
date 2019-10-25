package dev.phoenixxt.domain.internal

import dev.phoenixxt.domain.NotEnoughMoneyException
import dev.phoenixxt.domain.Transaction
import java.lang.IllegalArgumentException


internal class Bank(private val timeProvider: TimeProvider) {

    private var mutableListOfTransactions = mutableListOf<Transaction>()

    val listOfTransactions: List<Transaction> = mutableListOfTransactions

    var currentAmountOfMoney = 1000L
        private set(value) {
            onAmountOfMoneyChanged(oldAmount = field, newAmount = value)
            field = value
        }

    fun subtract(amount: Long) {
        val amountOfMoneyAfterTransaction = currentAmountOfMoney - amount
        if (amountOfMoneyAfterTransaction < 0) {
            throw NotEnoughMoneyException()
        }
        currentAmountOfMoney = amountOfMoneyAfterTransaction
    }

    fun add(amount: Long) {
        currentAmountOfMoney += amount
    }

    private fun onAmountOfMoneyChanged(oldAmount: Long, newAmount: Long) {
        val transactionAmount = newAmount - oldAmount
        mutableListOfTransactions.add(
            Transaction(
                transactionAmount,
                timeProvider.getCurrentTimeInMillis()
            )
        )
    }
}