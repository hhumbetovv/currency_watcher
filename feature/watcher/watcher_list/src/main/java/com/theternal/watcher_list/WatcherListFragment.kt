package com.theternal.watcher_list

import android.annotation.SuppressLint
import android.view.View
import androidx.transition.Visibility
import com.theternal.core.framework.BaseFragment
import com.theternal.watcher_list.WatcherListContract.*
import com.theternal.core.framework.Inflater
import com.theternal.core.framework.Initializer
import com.theternal.core.framework.contract.ViewEffect
import com.theternal.core.framework.contract.ViewEvent
import com.theternal.watcher_list.databinding.FragmentWatcherListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatcherListFragment() : BaseFragment<FragmentWatcherListBinding, WatcherListViewmodel, Event, State, ViewEffect.Empty> () {

    override val inflateBinding: Inflater<FragmentWatcherListBinding>
        get() = FragmentWatcherListBinding::inflate

    override val getViewModelClass: () -> Class<WatcherListViewmodel> = {
        WatcherListViewmodel::class.java
    }

    private lateinit var adapter: WatcherAdapter

    override val initViews: Initializer<FragmentWatcherListBinding> = {
        adapter = WatcherAdapter {
            postEvent(Event.UpdateWatcher(it))
        }
        watcherList.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    override fun onStateUpdate(oldState: State, newState: State) {

        binding.emptyListText.visibility = if(newState.list.isEmpty()) {
            View.VISIBLE
        } else View.GONE

        adapter.submitList(newState.list)
    }
}