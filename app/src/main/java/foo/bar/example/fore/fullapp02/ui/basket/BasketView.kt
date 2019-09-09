package foo.bar.example.fore.fullapp02.ui.basket

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import co.early.fore.core.ui.SyncableView
import foo.bar.example.fore.fullapp02.feature.basket.BasketModel
import foo.bar.example.fore.fullapp02.ui.basket.DialogCheckout
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.utils.MoneyFormatter
import kotlinx.android.synthetic.main.fragment_basket.view.*


class BasketView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr), SyncableView {

    //models that we need to sync with
    private lateinit var basketModel: BasketModel

    //single observer reference
    private var observer = { syncView() }


    override fun onFinishInflate() {
        super.onFinishInflate()

        /**
         * Google's MVVM implementation only lets us get locally
         * scoped ViewModels via Fragment or Activity, so we
         * don't get it from here, please see the Fragment code
         */
        // getModelReferences()

        setupButtonClickListeners()
    }

    fun setViewModel(model: BasketModel) {
        basketModel = model
    }

    private fun setupButtonClickListeners() {

        basket_add_button.setOnClickListener { v -> basketModel.addItem() }

        basket_remove_button.setOnClickListener { v -> basketModel.removeItem() }

        basket_checkout_button.setOnClickListener { v ->
            DialogCheckout.newInstance().show(
                ViewUtils.getActivityFromContext(context)?.fragmentManager,
                DialogCheckout::class.java.simpleName
            )
        }

        basket_discount_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            basketModel.isDiscountEnabled = isChecked
        }
    }


    //data binding stuff below

    override fun syncView() {
        basket_items_txt.text = "" + basketModel.numberOfItems
        basket_remove_button.isEnabled = basketModel.numberOfItems > 0
        basket_total_text.text = MoneyFormatter.format(basketModel.totalCost)
        basket_discount_text.text = MoneyFormatter.format(basketModel.totalDiscount)
        basket_discount_checkbox.isEnabled = basketModel.isDiscountEnabled
        basket_checkout_button.isEnabled = basketModel.numberOfItems > 0
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        basketModel.addObserver(observer)
        syncView()  // <- don't forget this
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        basketModel.removeObserver(observer)
    }

}
