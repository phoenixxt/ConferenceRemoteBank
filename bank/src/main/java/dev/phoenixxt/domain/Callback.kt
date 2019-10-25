package dev.phoenixxt.domain


interface Callback {
    fun onSuccess()
    fun onError(exception: Exception)
}