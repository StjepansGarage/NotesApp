package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.database.AppDatabase
import com.example.notes.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Biljeska : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.biljeska)

        database = AppDatabase.getDatabase(applicationContext)

        val naslovEditText = findViewById<EditText>(R.id.etNoteTitle)
        val tekstEditText = findViewById<EditText>(R.id.editTextTextMultiLine)
        val saveButton = findViewById<Button>(R.id.saveBtn)

        saveButton.setOnClickListener {
            val naslov = naslovEditText.text.toString()
            val tekst = tekstEditText.text.toString()

            if (naslov.isNotEmpty() && tekst.isNotEmpty()) {
                // Koristi Coroutine za asinkrono ubacivanje u bazu podataka
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        database.noteDao().insert(NoteEntity(naslov = naslov, tekst = tekst))
                    }

                    // Kada se unese bilješka, postavi rezultat i završi aktivnost
                    setResult(RESULT_OK)
                    finish()
                }
            } else {
                Toast.makeText(this, "Unesite naslov i tekst", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "com.example.notes.EXTRA_ID"
        const val EXTRA_NASLOV = "com.example.notes.EXTRA_NASLOV"
        const val EXTRA_TEKST = "com.example.notes.EXTRA_TEKST"
        const val EDIT_RESULT_CODE = 2
    }
}
