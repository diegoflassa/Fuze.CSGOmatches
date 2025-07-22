package dev.diegoflassa.fuzecsgomatches.core.extensions

import java.util.Stack

fun <T> Stack<T>.copy(): Stack<T> {
    val newStack = Stack<T>()
    if (this.isNotEmpty()) {
        newStack.addAll(this)
    }
    return newStack
}
