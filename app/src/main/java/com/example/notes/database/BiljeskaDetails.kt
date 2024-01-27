package com.example.notes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notes.database.AppDatabase
import com.example.notes.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BiljeskaDetails : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "ID_KEY"
    }

    private lateinit var database: AppDatabase
    private lateinit var naslovEditText: EditText
    private lateinit var tekstEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biljeska_details)

        database = AppDatabase.getDatabase(applicationContext)

        naslovEditText = findViewById(R.id.etTitle)
        tekstEditText = findViewById(R.id.etText)
        saveButton = findViewById(R.id.saveBtn2)

        val noteId = intent.getLongExtra(EXTRA_ID, -1)

        if (noteId != -1L) {
            loadNoteData(noteId)

            saveButton.setOnClickListener {
                saveChanges(noteId)
            }
        }
    }

    private fun loadNoteData(noteId: Long) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val note = database.noteDao().getNoteById(noteId)
                    // Postavite vrijednosti u EditText
                    withContext(Dispatchers.Main) {
                        naslovEditText.setText(note?.naslov)
                        tekstEditText.setText(note?.tekst)
                    }
                } catch (e: Exception) {
                    // Obrada pogrešaka pri čitanju iz baze podataka
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@BiljeskaDetails,
                            "Pogreška pri učitavanju bilješke.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveChanges(noteId: Long) {
        val naslov = naslovEditText.text.toString()
        val tekst = tekstEditText.text.toString()

        if (naslov.isNotEmpty() && tekst.isNotEmpty()) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        database.noteDao().update(NoteEntity(noteId, naslov, tekst))
                        withContext(Dispatchers.Main) {
                            // Prikaz poruke o uspjehu
                            Toast.makeText(
                                this@BiljeskaDetails,
                                "Promjene su spremljene.",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        }
                    } catch (e: Exception) {
                        // Obrada pogrešaka pri pisanju u bazu podataka
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@BiljeskaDetails,
                                "Pogreška pri spremanju promjena.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Unesite naslov i tekst", Toast.LENGTH_SHORT).show()
        }
    }
}
