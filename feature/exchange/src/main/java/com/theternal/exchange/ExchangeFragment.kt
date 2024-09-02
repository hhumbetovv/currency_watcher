package com.theternal.exchange

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.fragment.findNavController
import com.theternal.add_watcher.AddWatcherContract
import com.theternal.add_watcher.AddWatcherFragment
import com.theternal.common.extensions.format
import com.theternal.common.extensions.round
import com.theternal.common.extensions.setOnChangeListener
import com.theternal.common.extensions.toBigDecimalOrZero
import com.theternal.core.framework.BaseFragment
import com.theternal.core.framework.Inflater
import com.theternal.core.framework.Initializer
import com.theternal.core.framework.contract.ViewEffect
import com.theternal.exchange.ExchangeContract.*
import com.theternal.exchange.databinding.FragmentExchangeBinding
import com.theternal.uikit.fragments.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode


@AndroidEntryPoint
class ExchangeFragment : BaseFragment<FragmentExchangeBinding, ExchangeViewModel, Event, State, ViewEffect.Empty>() {

    override val inflateBinding: Inflater<FragmentExchangeBinding>
        get() = FragmentExchangeBinding::inflate

    override val getViewModelClass: () -> Class<ExchangeViewModel> = {
        ExchangeViewModel::class.java
    }

    private val addWatcherFragment = AddWatcherFragment()
    private val bottomSheet = BottomSheetFragment { addWatcherFragment }
    private val navController by lazy { findNavController() }

    override val initViews: Initializer<FragmentExchangeBinding> = {

        /**
         * Observing navigation state to get result and update currency
         * @see CurrencyResult
         * */
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<CurrencyResult>(
            SelectCurrencyFragment.CURRENCY_KEY
        )?.observe(viewLifecycleOwner) { result ->
            val isPrimary = result.first
            val currency = result.second
            postEvent(
                if(isPrimary) Event.SelectPrimaryCurrency(currency)
                else Event.SelectSecondaryCurrency(currency)
            )
        }

        container.apply {
            layoutTransition = LayoutTransition().apply {
                enableTransitionType(LayoutTransition.CHANGING)
            }
        }

        primaryField.setOnChangeListener { value ->
            postEvent(Event.SetPrimaryAmount(value.toBigDecimalOrZero()))
        }

        secondaryField.setOnChangeListener { value ->
            postEvent(Event.SetSecondaryAmount(value.toBigDecimalOrZero()))
        }

        /**
         * Navigates to the new screen for primary currency selection
         * */
        primaryCurrency.setOnClickListener {
            val action = ExchangeFragmentDirections.toSelectCurrencyFragment(
                currencyList = state?.currencyList?.toTypedArray() ?: emptyArray(),
                isPrimary = true
            )
            navController.navigate(action)
        }

        /**
         * Navigates to the new screen for secondary currency selection
         * */
        secondaryCurrency.setOnClickListener {
            val action = ExchangeFragmentDirections.toSelectCurrencyFragment(
                currencyList = state?.currencyList?.toTypedArray() ?: emptyArray(),
                isPrimary = false
            )
            navController.navigate(action)
        }

        addWatcherBtn.setOnClickListener {
            state?.let {
                if(!bottomSheet.isAdded) {
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }
                addWatcherFragment.setInitialState(
                    AddWatcherContract.State(
                        primaryCurrency = it.primaryCurrency,
                        secondaryCurrency = it.secondaryCurrency,
                        primaryAmount = it.primaryAmount.round(),
                        secondaryAmount = it.secondaryAmount.round()
                    )
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStateUpdate(oldState: State, newState: State) {

        updateLoading(newState.isLoading)

        if(newState.currencyList != null) {
            updateCurrencyList(
                newState.primaryCurrency,
                newState.secondaryCurrency
            )
        }

        updateFields(newState.primaryAmount, newState.secondaryAmount)

        updateSubtitles(newState.primaryCurrency, newState.secondaryCurrency,newState.exchangeValue)

        with(newState) {
            val hasAmount = primaryAmount > BigDecimal.ZERO || secondaryAmount > BigDecimal.ZERO

            binding.addWatcherBtn.isEnabled = !isLoading
                    && (primaryCurrency != secondaryCurrency)
                    && hasAmount
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        binding {
            loader.visibility = if(isLoading) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

            for (i in 0 until container.childCount) {
                container.getChildAt(i).isEnabled = !isLoading
            }
        }
    }

    private fun updateCurrencyList(
        primary: String,
        secondary: String,
    ) {
        binding {
            primaryCurrency.text = primary
            secondaryCurrency.text = secondary
        }
    }

    private fun updateFields(primaryAmount: BigDecimal, secondaryAmount: BigDecimal) {
        binding {
            if(primaryField.text.toString().toBigDecimalOrZero() != primaryAmount) {
                primaryField.setText(primaryAmount.format())
            }

            if(secondaryField.text.toString().toBigDecimalOrZero() != secondaryAmount) {
                secondaryField.setText(secondaryAmount.format())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSubtitles(
        primaryCurrency: String,
        secondaryCurrency: String,
        exchangeValue: BigDecimal
    ) {
        binding {
            //! Update Subtitles
            if(primaryCurrency == secondaryCurrency) {
                primarySubtitle.text = ""
                secondarySubtitle.text = ""
            } else {

                primarySubtitle.text = "1 $primaryCurrency = ${exchangeValue.format()} $secondaryCurrency"

                secondarySubtitle.text = "1 $secondaryCurrency = ${
                    BigDecimal.ONE.divide(exchangeValue, 6, RoundingMode.HALF_UP)
                } $primaryCurrency"
            }
        }
    }

}