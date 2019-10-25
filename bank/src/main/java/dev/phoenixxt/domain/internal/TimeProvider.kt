package dev.phoenixxt.domain.internal


internal class TimeProvider {

    fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }
}