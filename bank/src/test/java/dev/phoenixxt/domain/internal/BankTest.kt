package dev.phoenixxt.domain.internal

import dev.phoenixxt.domain.NotEnoughMoneyException
import dev.phoenixxt.domain.Transaction
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*


class BankTest {

    private val timeInMillis = 4325L

    private val mockTimeProvider = mockk<TimeProvider> {
        every { getCurrentTimeInMillis() } returns timeInMillis
    }

    private fun createBankWithMockedTimeProvider() = Bank(mockTimeProvider)

    @Test
    fun `adding money to a bank correctly changes current amount`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToAdd = 145L
        val amountBefore = bank.currentAmountOfMoney
        bank.add(amountToAdd)
        bank.currentAmountOfMoney shouldBe amountBefore + amountToAdd
    }

    @Test
    fun `subtracting money from a bank correctly changes current amount if there's enough money`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToSubtract = 894L
        bank.add(amountToSubtract)
        val amountBefore = bank.currentAmountOfMoney
        bank.subtract(amountToSubtract)
        bank.currentAmountOfMoney shouldBe amountBefore - amountToSubtract
    }

    @Test
    fun `subtracting money from a bank when it doesn't have enough money throws correct exception`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToSubtract = bank.currentAmountOfMoney + 1
        shouldThrow<NotEnoughMoneyException> { bank.subtract(amountToSubtract) }
    }

    @Test
    fun `subtracting money from a bank when it doesn't have enough money doesn't change amount`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToSubtract = bank.currentAmountOfMoney + 1
        val amountBeforeSubtract = bank.currentAmountOfMoney
        try {
            bank.subtract(amountToSubtract)
        } catch (exception: Exception) {}

        bank.currentAmountOfMoney shouldBe amountBeforeSubtract
    }

    @Test
    fun `when bank is created it should have zero transactions`() {
        val bank = createBankWithMockedTimeProvider()
        bank.listOfTransactions.size shouldBe 0
    }

    @Test
    fun `when bank is created it should have 1000 on its account`() {
        val bank = createBankWithMockedTimeProvider()
        bank.currentAmountOfMoney shouldBe 1000
    }

    @Test
    fun `adding money to a bank creates a correct transaction`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToAdd = 234L
        bank.add(amountToAdd)
        bank.listOfTransactions shouldContain Transaction(amountToAdd, timeInMillis)
    }

    @Test
    fun `subtracting money from a bank creates a correct transaction`() {
        val bank = createBankWithMockedTimeProvider()
        val amountToSubtract = 15L
        bank.add(amountToSubtract)
        bank.subtract(amountToSubtract)
        bank.listOfTransactions shouldContain Transaction(-amountToSubtract, timeInMillis)
    }

    @TestFactory
    fun `adding money to a bank creates correct amount of transactions`(): List<DynamicTest> {
        val bank = createBankWithMockedTimeProvider()
        val amountToAddEveryTime = 69L
        return (1..5).map { transactionCount ->
            DynamicTest.dynamicTest("When bank had $transactionCount then the amount of transactions should be $transactionCount") {
                bank.add(amountToAddEveryTime)
                bank.listOfTransactions.size shouldBe transactionCount
            }
        }
    }

    @TestFactory
    fun `subtracting money from a bank creates correct amount of transactions`(): List<DynamicTest> {
        val bank = createBankWithMockedTimeProvider()
        val amountToSubtractEveryTime = 69L
        return (1..5).map { transactionCount ->
            DynamicTest.dynamicTest("When bank had $transactionCount then the amount of transactions should be $transactionCount") {
                bank.subtract(amountToSubtractEveryTime)
                bank.listOfTransactions.size shouldBe transactionCount
            }
        }
    }

    @TestFactory
    fun `adding and subtracting to and from a bank creates correct amount of transactions`(): List<DynamicTest> {
        val bank = createBankWithMockedTimeProvider()
        val amountToChangeByEveryTime = 69L
        return (1..5).map { halfTransactionCount ->
            val totalTransactionCount = halfTransactionCount * 2
            DynamicTest.dynamicTest("When bank had $totalTransactionCount then the amount of transactions should be $totalTransactionCount") {
                bank.add(amountToChangeByEveryTime)
                bank.subtract(amountToChangeByEveryTime)
                bank.listOfTransactions.size shouldBe totalTransactionCount
            }
        }
    }
}