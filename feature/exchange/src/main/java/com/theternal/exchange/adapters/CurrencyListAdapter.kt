package com.theternal.exchange.adapters

import androidx.recyclerview.widget.DiffUtil
import com.theternal.core.framework.Inflater
import com.theternal.core.ui.BaseAdapter
import com.theternal.core.ui.Binder
import com.theternal.exchange.databinding.ViewCurrencyItemBinding

class CurrencyListAdapter(
    val callback: (String) -> Unit
) : BaseAdapter<String, ViewCurrencyItemBinding>(
    diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override val bindingInflater: Inflater<ViewCurrencyItemBinding>
        get() = ViewCurrencyItemBinding::inflate

    override val itemBinder: Binder<String, ViewCurrencyItemBinding> = { item, _ ->
        currencyText.text = item
        currencyContainer.setOnClickListener {
            callback(item)
        }
    }
}