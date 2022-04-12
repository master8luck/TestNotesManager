package com.masterluck.testnotesmanager.ui.note_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.masterluck.testnotesmanager.databinding.FragmentNotesListBinding
import com.masterluck.testnotesmanager.viewmodel.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentNotesListBinding
    private val viewModel: NoteListViewModel by viewModels()

    private val adapter = NoteAdapter(listOf(), this@NoteListFragment::onClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.setItems(notes)
        }

        binding.run {
            rvNotes.adapter = adapter
            fabAdd.setOnClickListener { viewModel.add() }
            ItemTouchHelper(itemTouchHelperCallback).apply { attachToRecyclerView(rvNotes) }
        }

    }

    private fun onClick(noteId: Long) {
        val action =
            NoteListFragmentDirections.actionFragmentNotesListToNoteEditFragment(
                noteId
            )
        findNavController().navigate(action)
    }

    private val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
        }
    }

}