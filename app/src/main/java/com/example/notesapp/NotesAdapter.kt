package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class  NotesAdapter (private var notes: List<Note>,context: Context): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private  val db : NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }


    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) { // set the data on element/
        val note =notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content


        holder.updateButton.setOnClickListener{
            val intent =Intent(holder.itemView.context,UpdateNoteActivity::class.java ).apply {
                putExtra("note_id",note.id)

            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete this note?")
            builder.setPositiveButton("Delete") { _, _ ->
                // Delete the note
                db.deleteNote(note.id)
                refreshData(db.getAllNotes())
                Toast.makeText(holder.itemView.context, "NOTE DELETED", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                // Do nothing, cancel the delete operation
            }
            builder.show()
        }

    }

    fun refreshData(newNotes: List<Note>)
    {
    notes = newNotes
    notifyDataSetChanged()
    }
}