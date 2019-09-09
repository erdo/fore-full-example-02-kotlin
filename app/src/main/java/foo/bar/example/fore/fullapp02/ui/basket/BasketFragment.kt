package foo.bar.example.fore.fullapp02.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.basket.BasketModel


/**
 * For the basket example we manage fore observers at the **View** level
 * [BasketView]
 */
class BasketFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_basket, null)

        /**
         * Google's ViewModelProviders needs an Activity or Fragment reference to
         * give us the view model so we don't do it in the view anymore. The factory
         * is only necessary if the ViewModel has construction parameters
         */
        val viewModel = ViewModelProviders.of(this, App.inst.appComponent.basketModelFactory)
            .get(BasketModel::class.java)
        (view as BasketView).setViewModel(viewModel)
        return view
    }

    companion object {

        fun newInstance(): BasketFragment {
            return BasketFragment()
        }
    }

}
