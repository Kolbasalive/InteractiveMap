package com.example.navigation

import com.example.navigation.Core.Core
import com.example.navigation.Core.TestDi
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
    fun testEpta(){
        val s = Core.getPathRoute(, 1, 5)
        //assertEquals("1 2 3 4 ", s)
        println(s)
    }

    @Test
    fun test(){
        TestDi.main(arrayOf())
    }

}