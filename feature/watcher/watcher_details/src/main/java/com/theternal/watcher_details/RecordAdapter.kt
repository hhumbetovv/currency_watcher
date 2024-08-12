package com.theternal.watcher_details

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.theternal.common.extensions.format
import com.theternal.core.framework.Inflater
import com.theternal.core.ui.BaseAdapter
import com.theternal.core.ui.Binder
import com.theternal.domain.entities.RecordEntity
import com.theternal.watcher_details.databinding.ViewRecordItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordAdapter : BaseAdapter<RecordEntity, ViewRecordItemBinding>(
    object : ItemCallback<RecordEntity>() {

        override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())

    override val bindingInflater: Inflater<ViewRecordItemBinding>
        get() = ViewRecordItemBinding::inflate

    override val itemBinder: Binder<RecordEntity, ViewRecordItemBinding>
        @SuppressLint("SetTextI18n")
        get() = { item, _ ->

            value.text = item.value.format()

            currency.text = item.currency

            date.text = dateFormat.format(Date(item.date))

            icon.setImageResource(
                if(item.maxExceeded) com.theternal.uikit.R.drawable.ic_trend_up
                else com.theternal.uikit.R.drawable.ic_trend_down
            )
        }

}