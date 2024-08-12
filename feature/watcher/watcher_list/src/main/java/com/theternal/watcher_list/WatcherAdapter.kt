package com.theternal.watcher_list

import android.annotation.SuppressLint
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.theternal.common.extensions.format
import com.theternal.core.framework.Inflater
import com.theternal.core.ui.BaseAdapter
import com.theternal.core.ui.Binder
import com.theternal.domain.entities.WatcherEntity
import com.theternal.watcher_list.databinding.ViewWatcherItemBinding

class WatcherAdapter(
    private val onWatcherUpdate: (WatcherEntity) -> Unit,
) : BaseAdapter<WatcherEntity, ViewWatcherItemBinding>(
    object : ItemCallback<WatcherEntity>() {

        override fun areItemsTheSame(oldItem: WatcherEntity, newItem: WatcherEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WatcherEntity, newItem: WatcherEntity): Boolean {
            return oldItem.recordCount == newItem.recordCount
        }
    }
) {
    override val bindingInflater: Inflater<ViewWatcherItemBinding>
        get() = ViewWatcherItemBinding::inflate

    override val itemBinder: Binder<WatcherEntity, ViewWatcherItemBinding>
        @SuppressLint("SetTextI18n")
        get() = { item, _ ->

            container.setOnClickListener {
                Navigation.findNavController(container).navigate(
                    WatcherListFragmentDirections.toWatcherDetails(item.id)
                )
            }

            isActiveSwitch.isChecked = item.isActive

            isActiveSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked != item.isActive) {
                    onWatcherUpdate(item.copy(
                        isActive = isChecked
                    ))
                }
            }

            amount.text = item.amount.format()
            amountCurrency.text = item.amountCurrency

            threshold.text = item.threshold.format()
            thresholdCurrency.text = item.thresholdCurrency

            recordCount.text = "Records: ${item.recordCount}"
        }

}