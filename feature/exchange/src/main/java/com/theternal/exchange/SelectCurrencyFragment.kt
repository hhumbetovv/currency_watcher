package com.theternal.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.theternal.exchange.adapters.CurrencyListAdapter
import com.theternal.exchange.databinding.FragmentSelectCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A custom type holds string (currency) and boolean (specifies which field it is)
 * */
typealias CurrencyResult = Pair<Boolean, String>

@AndroidEntryPoint
class SelectCurrencyFragment : Fragment() {

    companion object {
        const val CURRENCY_KEY = "CURRENCY_KEY"
    }

    private var binding: FragmentSelectCurrencyBinding? = null

    /**
     * Initializing args to get the list and boolean value sent from the previous screen
     * */
    private val args : SelectCurrencyFragmentArgs by navArgs()

    private val navController by lazy { findNavController() }

    private lateinit var currencyListAdapter: CurrencyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCurrencyBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyListAdapter = CurrencyListAdapter(
            callback = { currency ->

                /**
                 * Updating the navigation state of the previous screen with [CurrencyResult]
                 * */
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    CURRENCY_KEY, CurrencyResult(args.isPrimary, currency)
                )
                navController.popBackStack()
            }
        )

        currencyListAdapter.submitList(args.currencyList.toList())

        binding?.apply {
            currencyList.adapter = currencyListAdapter
        }
    }
}