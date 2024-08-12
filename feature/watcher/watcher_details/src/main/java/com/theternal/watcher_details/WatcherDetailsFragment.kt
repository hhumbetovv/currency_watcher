package com.theternal.watcher_details

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.fragment.findNavController
import com.theternal.core.framework.BaseFragment
import com.theternal.core.framework.Inflater
import com.theternal.core.framework.Initializer
import com.theternal.core.framework.contract.ViewEffect
import com.theternal.watcher_details.databinding.FragmentWatcherDetailsBinding
import com.theternal.watcher_details.WatcherDetailsContract.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatcherDetailsFragment : BaseFragment<FragmentWatcherDetailsBinding, WatcherDetailsViewModel, Event, State, Effect>() {

    override val inflateBinding: Inflater<FragmentWatcherDetailsBinding>
        get() = FragmentWatcherDetailsBinding::inflate
    override val getViewModelClass: () -> Class<WatcherDetailsViewModel> = {
        WatcherDetailsViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override val initViews: Initializer<FragmentWatcherDetailsBinding> = {
        val id = arguments?.getLong("id")
        if(id != null) {
            postEvent(Event.GetDetails(id))
        }
        adapter = RecordAdapter()
        recordList.adapter = adapter

        removeBtn.setOnClickListener {
            postEvent(Event.RemoveWatcher)
        }
    }

    private lateinit var adapter: RecordAdapter

    override fun onStateUpdate(oldState: State, newState: State) {
        adapter.submitList(newState.records)

        binding.emptyListText.visibility = if(newState.records.isEmpty()) {
            View.VISIBLE
        } else View.GONE
    }

    override fun onEffectUpdate(effect: Effect) {
        when(effect) {
            Effect.NavigateBack -> findNavController().popBackStack()
        }
    }
}