package com.example.notes.rvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.database.NoteEntity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlin.random.Random

class NoteAdapter(
    private val cardClickListener: (NoteEntity) -> Unit,
    private val deleteClickListener: (NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_liste, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.naslovTextView.text = currentNote.naslov
        holder.tekstTextView.text = currentNote.tekst

        // Postavite onClickListener za karticu
        holder.cardView.setOnClickListener {
            cardClickListener.invoke(currentNote)
        }

        // Postavite onClickListener za gumb za brisanje
        holder.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, currentNote)
        }

        // Postavite nasumičnu boju pozadine za karticu
        val randomColor = getRandomColor(holder.itemView.context)
        holder.cardView.setCardBackgroundColor(randomColor)
    }

    private fun getRandomColor(context: Context): Int {
        val colorList = listOf(
            R.color.sticky_1,
            R.color.sticky_2,
            R.color.sticky_3,
            R.color.sticky_4,
            R.color.sticky_5
        )

        val randomColor = colorList[Random.nextInt(colorList.size)]
        return ContextCompat.getColor(context, randomColor)
    }


    private fun showDeleteConfirmationDialog(context: Context, note: NoteEntity) {
        val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete this note?")

        builder.setPositiveButton("Delete") { _, _ ->
            // Poziv metode za brisanje ako korisnik potvrdi
            deleteClickListener.invoke(note)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naslovTextView: MaterialTextView = itemView.findViewById(R.id.tvNoteTitle)
        val tekstTextView: MaterialTextView = itemView.findViewById(R.id.tvDetails)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete)
    }
    // DiffCallback za automatsko prepoznavanje promjena u listi
    private class NoteDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }
    }
}
