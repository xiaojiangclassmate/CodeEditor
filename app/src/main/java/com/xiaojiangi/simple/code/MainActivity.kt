package com.xiaojiangi.simple.code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.xiaojiangi.simple.code.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setSupportActionBar(_binding.toolbar)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editor_undo -> _binding.editor.undo()
            R.id.editor_redo -> _binding.editor.redo()
        }
        return super.onOptionsItemSelected(item)
    }
}