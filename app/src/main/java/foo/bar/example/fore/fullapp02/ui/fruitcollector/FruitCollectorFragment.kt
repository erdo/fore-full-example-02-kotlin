package foo.bar.example.fore.fullapp02.ui.fruitcollector

import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.fragment.SyncableSupportFragment
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import org.koin.android.ext.android.inject


/**
 * For the basket example we manage fore observers at the **Fragment** level
 * using: [SyncableSupportFragment]
 */
class FruitCollectorFragment : SyncableSupportFragment() {

    private val fruitCollectorModel: FruitCollectorModel by inject()

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.fragment_fruitcollector
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            fruitCollectorModel
        )
    }

    companion object {
        fun newInstance(): FruitCollectorFragment {
            return FruitCollectorFragment()
        }
    }

}
