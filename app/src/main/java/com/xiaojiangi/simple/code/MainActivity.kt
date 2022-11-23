package com.xiaojiangi.simple.code

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.xiaojiangi.simple.code.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)
        _binding.editor.setTextTypeface(Typeface.createFromAsset(assets, "jetbrains_mono.ttf"))
        _binding.editor.setText(String(assets.open("CodeEditor.java").readBytes()))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editor_undo -> _binding.editor.undo()
            R.id.editor_redo -> _binding.editor.redo()
            R.id.editor_clear -> _binding.editor.setText("")
            R.id.file_open_view -> _binding.editor.setText(
                String(
                    assets.open("View.java").readBytes()
                )
            )

            R.id.file_open_code_editor -> _binding.editor.setText(
                String(
                    assets.open("CodeEditor.java").readBytes()
                )
            )

            R.id.editor_print -> Log.d("Editor", _binding.editor.text.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}