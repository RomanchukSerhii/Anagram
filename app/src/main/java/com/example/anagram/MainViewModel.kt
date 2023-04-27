package com.example.anagram

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _anagram = MutableLiveData<String>()
    val anagram = _anagram

    fun getAnagramWithoutFilter(text: String) {
        val regex = Regex("[^A-Za-z]")
        getAnagram(text, regex)
    }

    fun getAnagramWithFilter(text: String, filter: String) {
        val filterRegex = StringBuilder()
        filter.forEach { ch ->
            filterRegex.append("|")
            filterRegex.append(ch)
        }
        val regex = Regex(filterRegex.toString())
        getAnagram(text, regex)
    }

    private fun getAnagram(text: String, regex: Regex) {
        val wordList = text.split(" ")
        val result = StringBuilder()
        wordList.forEach { word ->
            result.append(getWordAnagram(word, regex))
            result.append(" ")
        }
        _anagram.value = result.trim().toString()
    }

    private fun getWordAnagram(word: String, regex: Regex): String {
        val lastIndex = word.length - 1
        val letterList = Array(word.length){""}
        val filteredSymbols = mutableMapOf<Char, Int>()

        for (i in 0..lastIndex) {
            val letter = word[i].toString()
            if (letter.matches(regex)) {
                filteredSymbols[word[i]] = i
                continue
            }
            letterList[lastIndex - i] = letter
        }

        val result = StringBuilder()
        letterList.forEach { letter ->
            result.append(letter)
        }

        for ((symbol, index) in filteredSymbols) {
            result.insert(index, symbol)
        }

        return result.toString()
    }
}