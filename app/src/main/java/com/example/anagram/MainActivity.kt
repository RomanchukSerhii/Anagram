package com.example.anagram

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.anagram.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText

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
        checkOrientation()
        setEditTextListeners()
        observeViewModel()
    }

    private fun checkOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setCustomActionBar()
        } else {
            supportActionBar?.hide()
        }
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
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setTextParams(etEnterText)
                    getAnagram()
                }

                override fun afterTextChanged(s: Editable?) {

                }
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

    private fun setTextParams(editText: TextInputEditText) {
        if (editText.text.toString().isNotBlank()) {
            editText.gravity = Gravity.START
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            editText.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        } else {
            editText.gravity = Gravity.CENTER
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            editText.textAlignment = View.TEXT_ALIGNMENT_CENTER
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

    private fun observeViewModel() {
        viewModel.anagram.observe(this) {
            with(binding) {
                if (it.isEmpty()) {
                    tvAnagram.text = getString(R.string.here_must_be_your_anagram)
                    tvAnagram.setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.gray
                        )
                    )
                } else {
                    tvAnagram.text = it
                    tvAnagram.setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.yellow
                        )
                    )
                }
            }
        }
    }
}