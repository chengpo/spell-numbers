package com.monkeyapp.numbers

import org.junit.Assert


infix fun <T> T.shouldEqual(expect: T) {
    Assert.assertEquals(expect, this)
}

infix fun <T> Comparable<T>.shouldLessThan(expect: T) {
    Assert.assertTrue("should less than $expect", this < expect)
}
