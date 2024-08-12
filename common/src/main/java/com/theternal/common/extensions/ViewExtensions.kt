package com.theternal.common.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.Spinner

fun Spinner.onItemSelectedListener(listener: (Int) -> Unit) {
    onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }
}

fun EditText.setOnChangeListener(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(text: Editable?) {
            if(hasFocus()) {
                listener.invoke(text.toString())
            }
        }
    })
}
