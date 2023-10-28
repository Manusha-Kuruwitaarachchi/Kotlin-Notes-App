package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper (context:Context) :SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION){ //null is used for curser factory which creates a cursor obj and act as a pointer to read data from db(passing it as null means using default data factory)

    companion object{
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_TITLE = "TITLE"
        private const val COLUMN_CONTENT = "content"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_TITLE TEXT,$COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { // we use this query to remove similary table if exists
        val dropTableQuery ="DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)//new table will create after dropping the table with same name
    }

    //============================INSERT OPTION=======================================
    fun insertNote(note:Note){//pass the note class in Note variable
        val db =writableDatabase //kept the db as writeable means it could be modified
        val values = ContentValues().apply {
            put(COLUMN_TITLE,note.title)//put takes the argument. Title is takes the data from note.kt title variable
            put(COLUMN_CONTENT,note.content)
        }//added a ContentValues clz used to store values associated with column names(apply is used to peform operations)

        db.insert(TABLE_NAME,null,values)//insert the data to database
        db.close()//close db connection
    }

    //========================== VIEW ===============================================

    fun getAllNotes():List<Note>{
        val notesList = mutableListOf<Note>() // created a listed that can be modify or flexible that is known as mutable
        val db = readableDatabase //used readabledataabase method to read the data
        val query = "SELECT * from $TABLE_NAME"
        val cursor = db.rawQuery(query,null)//used rawquery method to execute the query and used cursor variable to store the result

    while(cursor.moveToNext()){ // going through the data
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)) //get the id of the data that the cursor in currently in the data base
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        val note = Note(id,title,content)//when all th  e 3 data is retrieve successfully and pass an argument and stored in nte variable
        notesList.add(note)//add the data to the note list
    }
     cursor.close()//close cursor
     db.close()
     return notesList
    }

    //=================== DELETE ALL ======================================

    fun deleteAllNotes() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
    //============================== UPDATE DATA ======================================

    fun updateNote(note:Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause,whereArgs)
        db.close()
    }

    fun getNoteById(noteId: Int):Note{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query,null )

        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)
    }

    fun deleteNote(noteId:Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID= ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}