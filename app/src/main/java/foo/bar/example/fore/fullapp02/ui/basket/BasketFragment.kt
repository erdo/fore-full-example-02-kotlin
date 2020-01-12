package foo.bar.example.fore.fullapp02.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.fragment.SyncFragmentX
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.basket.BasketModel
import foo.bar.example.fore.fullapp02.ui.common.widget.NotImplementedDialog
import foo.bar.example.fore.fullapp02.utils.MoneyFormatter
import kotlinx.android.synthetic.main.fragment_basket.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * For the basket example we manage fore observers at the **Fragment** level
 * [BasketView]
 */
class BasketFragment : SyncFragmentX() {

    //models that we need to sync with
    private val basketModel: BasketModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_basket, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {

        basket_add_button.setOnClickListener { basketModel.addItem() }

        basket_remove_button.setOnClickListener { basketModel.removeItem() }

        basket_checkout_button.setOnClickListener {
            NotImplementedDialog.newInstance().show(
                requireFragmentManager(),
                NotImplementedDialog::class.java.simpleName
            )
        }

        basket_discount_checkbox.setOnCheckedChangeListener { _, isChecked ->
            basketModel.isDiscount = isChecked
        }
    }


    //data binding stuff below

    override fun syncView() {
        basket_items_txt.text = "" + basketModel.numberOfItems
        basket_remove_button.isEnabled = basketModel.numberOfItems > 0
        basket_total_text.text = MoneyFormatter.format(basketModel.totalCost)
        basket_discount_text.text = MoneyFormatter.format(basketModel.totalDiscount)
        basket_discount_checkbox.isChecked = basketModel.isDiscount
        basket_checkout_button.isEnabled = basketModel.numberOfItems > 0
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            basketModel
        )
    }

    companion object {
        fun newInstance(): BasketFragment {
            return BasketFragment()
        }
    }

}
