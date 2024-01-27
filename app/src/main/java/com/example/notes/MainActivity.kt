package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.database.NoteEntity
import com.example.notes.database.NoteViewModel
import com.example.notes.rvAdapter.NoteAdapter
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var addBtn :Button

    companion object {
        const val BILJESKE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addBtn=findViewById(R.id.addBtn)
        val recyclerView = findViewById<RecyclerView>(R.id.rvNoteList)

        // Inicijalizirajte adapter s potrebnim listenerima
        adapter = NoteAdapter(
            deleteClickListener = { clickedNote ->
                viewModel.delete(clickedNote)
            },
            cardClickListener = { clickedNote ->
                openBiljeskaDetails(clickedNote.id)
            }
        )

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // Observing LiveData for updates
        viewModel.allNotes.observe(this, Observer { notes ->
            notes?.let {
                adapter.submitList(it)
            }
        })
        addBtn.setOnClickListener {
            // Otvorite novu aktivnost za dodavanje bilješke
            val intent = Intent(this, Biljeska::class.java)
            startActivityForResult(intent, BILJESKE_REQUEST_CODE)
        }
    }




    private fun openBiljeskaDetails(noteId: Long) {
        // Otvorite BiljeskaDetails aktivnost s ID-om odabrane bilješke
        val intent = Intent(this, BiljeskaDetails::class.java)
        intent.putExtra(BiljeskaDetails.EXTRA_ID, noteId)
        startActivityForResult(intent, BILJESKE_REQUEST_CODE)
    }
}
