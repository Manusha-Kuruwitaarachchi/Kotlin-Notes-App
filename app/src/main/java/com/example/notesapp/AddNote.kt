package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.databinding.ActivityAddNoteBinding

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()//take user inputs using edit text using .text method and stored in title variable
            val content = binding.contentEditText.text.toString()
            val note = Note(0, title, content)//pass the arguments in note clz(id start with zero)
            db.insertNote(note) //access the db and insert note function and pass the argument as note variable
            finish()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        }

    }
}
