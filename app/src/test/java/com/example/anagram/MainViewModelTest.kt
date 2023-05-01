package com.example.anagram

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    @Before
    fun before() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    @After
    fun after() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun `getAnagramWithoutFilter should ignore digits`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithoutFilter("Foxminded cool 24/7")
        val expected = "dednimxoF looc 24/7"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getAnagramWithoutFilter should reverse word`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithoutFilter("abcd efgh")
        val expected = "dcba hgfe"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getAnagramWithoutFilter should ignore digits and non alphabetic symbols`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithoutFilter("a1bcd efg!h")
        val expected = "d1cba hgf!e"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getAnagramWithFilter should ignore digits`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithFilter("Foxminded cool 24/7", "xl")
        val expected = "dexdnimoF oocl 7/42"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getAnagramWithFilter should reverse word`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithFilter("abcd efgh", "xl")
        val expected = "dcba hgfe"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getAnagramWithFilter should ignore digits and non alphabetic symbols`() {
        val viewModel = MainViewModel()
        viewModel.getAnagramWithFilter("a1bcd efglh", "xl")
        val expected = "dcb1a hgfle"
        val actual = viewModel.anagram.value
        Assert.assertEquals(expected, actual)
    }
}