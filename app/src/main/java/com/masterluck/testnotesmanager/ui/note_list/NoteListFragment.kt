package com.masterluck.testnotesmanager.ui.note_list

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.masterluck.testnotesmanager.R
import com.masterluck.testnotesmanager.databinding.FragmentNotesListBinding
import com.masterluck.testnotesmanager.utils.Utils
import com.masterluck.testnotesmanager.viewmodel.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

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
            showNoData()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        binding.run {
            viewModel.run {
                if (!isDataRefreshed) {
                    isDataRefreshed = true
                    loadData()
                }
            }
            rvNotes.adapter = adapter
            swipeLl.setOnRefreshListener {
                loadData()
                swipeLl.isRefreshing = false
            }
            fabAdd.setOnClickListener { viewModel.add() }
            ItemTouchHelper(itemTouchHelperCallback).apply { attachToRecyclerView(rvNotes) }
        }

    }

    private fun loadData() {
        viewModel.run {
            if (isLoading.value == true)
                return

            isLoading.postValue(true)
            fetchFromWeb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (Utils.isFirstLaunched(requireContext()))
                        binding.tvNoData.isVisible = false
                }
                .doOnSuccess { isRefreshed ->
                    isLoading.postValue(false)
                    if (isRefreshed) {
                        Utils.markLaunched(requireContext())
                    } else {
                        isDataRefreshed = false
                        showError()
                    }
                }
                .subscribe()
        }
    }

    private fun showError() {
        if (Utils.isFirstLaunched(requireContext()) && adapter.itemCount == 0) {
            binding.tvNoData.setText(R.string.no_internet)
            binding.tvNoData.isVisible = true
        } else {
            Snackbar.make(requireView(), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun showNoData() {
        binding.run {
            tvNoData.isVisible = (adapter.itemCount <= 0)
            tvNoData.setText(R.string.no_data)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.run {
            if (isLoading) {
                if (Utils.isFirstLaunched(requireContext())) {
                    loadingRoot.isVisible = true
                }
                else {
                    pbHorizontalLoading.isVisible = true
                    animateLoader(pbHorizontalLoading)
                }
            } else {
                pbHorizontalLoading.isVisible = false
                loadingRoot.isVisible = false
            }
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

    private fun animateLoader(pb: ProgressBar) {
        ObjectAnimator.ofInt(pb,"progress", 0, 1000).apply {
            duration = 5000
            interpolator = LinearInterpolator()
            start()
        }
    }

}