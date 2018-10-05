package com.monkeyapp.numbers

import org.junit.Assert

infix fun <T> Array<T>.shouldEqual(expect: Array<T>) {
    Assert.assertArrayEquals(expect, this)
}