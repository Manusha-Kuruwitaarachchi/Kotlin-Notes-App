package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  lateinit var db :NoteDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use binding.root to set the layout

        db = NoteDatabaseHelper(this )
        notesAdapter = NotesAdapter(db.getAllNotes(),this)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this )
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }
        binding.deleteButton.setOnClickListener {
            // Show a confirmation dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete All Notes")
            builder.setMessage("Are you sure you want to delete all notes?")
            builder.setPositiveButton("Yes") { _, _ ->
                // User confirmed to delete all data
                db.deleteAllNotes()

                // Refresh the data by updating the adapter
                notesAdapter.refreshData(db.getAllNotes())

                // Display a message using a Toast
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { _, _ ->
                // User chose not to delete the data
                // You can add code here for any action you want to perform on "No"
            }
            builder.show()
        }

    }

    override fun onResume()
    {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())

    }


}
