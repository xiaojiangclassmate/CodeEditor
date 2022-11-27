package com.xiaojiangi.simple.code

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

            R.id.editor_font_size -> {
                val view = View.inflate(this, R.layout.dialog_edit_text, null)

                MaterialAlertDialogBuilder(this).setView(view).setTitle("字体大小")
                    .setNegativeButton("确定") { _, _ ->
                        val size: Float =
                            (view.findViewById<EditText>(R.id.editTextText).text.toString()).toFloat()
                        _binding.editor.textSize = size
                    }.setNeutralButton("取消", null).show()
            }

            R.id.editor_jump_line -> {
                val view = View.inflate(this, R.layout.dialog_edit_text, null)
                MaterialAlertDialogBuilder(this).setView(view).setTitle("跳转行")
                    .setNegativeButton("确定") { _, _ ->
                        _binding.editor.jumpToLine(
                            (view.findViewById<EditText>(R.id.editTextText).text.toString().toInt())
                        )
                    }.setNeutralButton("取消", null).show()
            }

            R.id.editor_fixed_line -> {
                _binding.editor.isFixedLineNumber = !_binding.editor.isFixedLineNumber
                item.isChecked = _binding.editor.isFixedLineNumber
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }
}