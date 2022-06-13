package de.bembelnaut.courses.modularizingapps.core

class Logger(
    private val tag: String,
    private val isDebug: Boolean = true,
) {
    fun log(msg: String) {
        if (!isDebug) {
            // TODO: production loggin - crashlytics
        } else {
            printLogD(tag, msg)
        }
    }

    private fun printLogD(tag: String, msg: String) {
        println("$tag: $msg")
    }

    companion object Factory {
        fun buildDebug(tag: String) =
            Logger(
                tag = tag,
                isDebug = true,
            )

        fun buildRelease(tag: String) =
            Logger(
                tag = tag,
                isDebug = false,
            )
    }
}