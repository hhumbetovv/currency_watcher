package com.theternal.uikit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.theternal.uikit.databinding.FragmentBottomSheetBinding

class BottomSheetFragment(
    private val fragmentFactory: (() -> Fragment)? = null
) : BottomSheetDialogFragment() {

    private var binding: FragmentBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(fragmentFactory == null){
            dismiss()
        } else {
            binding?.apply {
                val fragment = fragmentFactory.invoke()
                childFragmentManager.beginTransaction().apply {
                    add(bottomSheetContainerView.id, fragment)
                    commit()
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
