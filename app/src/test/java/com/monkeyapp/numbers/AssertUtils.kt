package com.monkeyapp.numbers

import org.junit.Assert


infix fun <T> T.shouldEqual(expect: T) {
    Assert.assertEquals(expect, this)
}