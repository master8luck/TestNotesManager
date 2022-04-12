package com.masterluck.testnotesmanager.ui.note_edit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.masterluck.testnotesmanager.R
import com.masterluck.testnotesmanager.databinding.FragmentNoteEditBinding
import com.masterluck.testnotesmanager.utils.Utils
import com.masterluck.testnotesmanager.viewmodel.NoteEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteEditFragment : Fragment() {

    private lateinit var binding: FragmentNoteEditBinding

    private val viewModel: NoteEditViewModel by viewModels()
    private val args: NoteEditFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoteEditBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.id = args.noteId
        viewModel.init()

        binding.run {
            viewModel.note.run {
                etTitle.setText(title)
                etDesc.setText(description)
                tvDate.text = Utils.formatDate(time)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                viewModel.update(binding.etTitle.text.toString(), binding.etDesc.text.toString())
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}