package foo.bar.example.fore.fullapp02.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.basket.BasketModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * For the basket example we manage fore observers at the **View** level
 * [BasketView]
 */
class BasketFragment : Fragment() {

    //models that we need to sync with
    private val basketModel: BasketModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_basket, null)
        (view as BasketView).setViewModel(basketModel)
        return view
    }

    companion object {

        fun newInstance(): BasketFragment {
            return BasketFragment()
        }
    }

}
