package com.example.weatherapp

import com.example.weatherapp.Views.MainActivity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDebug1() {
        val testDebug = MainActivity()
        val result = testDebug.testDebug(2, 4)
        assertEquals(6, result)
    }
//    fun abc(a: Int, b: Int): Int {
//        return a + b
//    }

}