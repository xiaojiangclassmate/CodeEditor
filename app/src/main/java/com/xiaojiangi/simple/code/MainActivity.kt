package com.xiaojiangi.simple.code

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xiaojiangi.simple.code.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.editor.apply {
            text = String(assets.open("View.java").readBytes())
            textSize =18f
            setTextStyle(Typeface.createFromAsset(assets,"jetbrains_mono.ttf"))
        }
    }
}