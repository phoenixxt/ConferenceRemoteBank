package dev.phoenixxt.domain.internal

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper


internal class ThreadManager {

    private val workHandler: Handler

    private val workThread = HandlerThread("BankHandlerThread")

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    init {
        workThread.start()
        workHandler = Handler(workThread.looper)
    }

    fun awaitAndDo(action: () -> Unit) {
        workHandler.post {
            Thread.sleep(1500)
            mainThreadHandler.post(action)
        }
    }
}