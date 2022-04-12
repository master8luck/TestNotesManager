package com.masterluck.testnotesmanager.ui.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masterluck.testnotesmanager.databinding.ItemNoteBinding
import com.masterluck.testnotesmanager.model.Note
import com.masterluck.testnotesmanager.utils.Utils

class NoteAdapter(
    private var notes: List<Note>,
    private var onClick: (noteId: Long) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.Holder>() {

    fun setItems(data: List<Note>) {
        notes = data
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.run {
                tvTitle.text = note.title
                tvDesc.text = note.description
                tvDate.text = Utils.formatDate(note.time)
                root.setOnClickListener { onClick(note.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemBinding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun getNoteAt(position: Int): Note {
        return notes[position]
    }


}