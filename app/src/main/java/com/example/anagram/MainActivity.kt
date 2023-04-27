package com.example.anagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.anagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCustomActionBar()
        setEditTextListeners()
        observeViewModel()
    }

    private fun setCustomActionBar() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.custom_action_bar)
            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.action_bar_background
                )
            )
        }
    }

    private fun setEditTextListeners() {
        with(binding) {
            etEnterText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    getAnagram()
                }

                override fun afterTextChanged(s: Editable?) { }
            })

            etFilter.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    getAnagram()
                }

                override fun afterTextChanged(s: Editable?) { }
            })
        }
    }

    private fun getAnagram() {
        with(binding) {
            val text = etEnterText.text.toString().trim()
            val filter = etFilter.text.toString().trim()

            if (filter.isEmpty()) {
                viewModel.getAnagramWithoutFilter(text)
            }
            if (filter.isNotBlank() && text.isNotBlank()) {
                viewModel.getAnagramWithFilter(text, filter)
            }
        }
    }

    private fun observeViewModel(){
        viewModel.anagram.observe(this) {
            binding.tvAnagram.text = it
        }
    }
}