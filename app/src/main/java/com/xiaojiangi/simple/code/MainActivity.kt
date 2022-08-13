package com.xiaojiangi.simple.code

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StatFs
import android.view.Menu
import android.view.MenuItem
import com.xiaojiangi.simple.code.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        StatFs("").toString()
        setSupportActionBar(_binding.toolbar)
        _binding.editor.apply {
            text = String(assets.open("View.java").readBytes())
            textSize =18f
            setTextStyle(Typeface.createFromAsset(assets,"jetbrains_mono.ttf"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.file_view -> _binding.editor.text = String(assets.open("View.java").readBytes())
            R.id.file_code_editor -> _binding.editor.text = String(assets.open("CodeEditor.java").readBytes())
            R.id.editor_clear -> _binding.editor.text = ""
            R.id.editor_undo -> _binding.editor.undo()
            R.id.editor_redo -> _binding.editor.redo()
        }
        return true
    }
}