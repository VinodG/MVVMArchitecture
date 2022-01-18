package com.example.mvvmarchitecture.list.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DemoTest {
    @Mock
    lateinit var operators: Operators
    lateinit var calculator: Calculator

    @Before
    fun onSetup() {
        calculator = Calculator(operators = operators)
    }

    @Test
    fun givenValidInput_WhenAdd_shouldCallAddFunction() {
        val a = 10
        val b = 20
        calculator.addTwoNumbers(a, b)
        verify(operators).add(a, b)
        `when`(operators.add(a, b)).thenReturn(30)
        assertEquals(calculator.addTwoNumbers(a, b), 30)
    }

    @Test
    fun givenValidInput_WhenAdd_shouldReturnValidOutPut() {
        val a = 10
        val b = 20
        calculator.addTwoNumbers(a, b)
        `when`(operators.add(a, b)).thenReturn(30)
        assertEquals(calculator.addTwoNumbers(a, b), 30)
    }
}

object Operators {
    fun add(a: Int, b: Int): Int = a + b

    fun subtract(a: Int, b: Int): Int = a - b

    fun multiply(a: Int, b: Int): Int = a * b

    fun divide(a: Int, b: Int): Int = a / b
}

class Calculator(private val operators: Operators) {
    fun addTwoNumbers(a: Int, b: Int): Int = operators.add(a, b)

    fun subtractTwoNumbers(a: Int, b: Int): Int = operators.subtract(a, b)

    fun multiplyTwoNumbers(a: Int, b: Int): Int = operators.multiply(a, b)

    fun divideTwoNumbers(a: Int, b: Int): Int = operators.divide(a, b)
}