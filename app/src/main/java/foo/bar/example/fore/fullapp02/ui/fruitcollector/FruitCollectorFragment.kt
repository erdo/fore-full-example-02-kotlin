package foo.bar.example.fore.fullapp02.ui.fruitcollector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.early.fore.core.observer.Observer
import co.early.fore.core.ui.SyncableView
import co.early.fore.kt.core.ui.trigger.TriggerWhen
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.message.UserMessage
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerAnimationComplete
import kotlinx.android.synthetic.main.fragment_fruitcollector.*
import org.koin.android.ext.android.inject


/**
 * For the basket example we manage fore observers at the **Fragment** level
 * using: [SyncXFragment]
 */
class FruitCollectorFragment : Fragment(R.layout.fragment_fruitcollector), SyncableView {

    //models we need
    private val fruitCollectorModel: FruitCollectorModel by inject()

    //single observer reference
    var observer = Observer { syncView() }

    //other UI stuff
    private lateinit var fruitCollectorAdapter: FruitCollectorAdapter
    private lateinit var winAnimation: AnimatorSet
    private lateinit var fetchingStartedSyncTrigger: TriggerWhen
    private lateinit var fetchingStoppedSyncTrigger: TriggerWhen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonClickListeners()

        setupAdapters()

        setupAnimationTriggers()
    }

    private fun setupButtonClickListeners() {

        fruit_go1_button.setOnClickListener {
            fruitCollectorModel.fetchFruits1(
                !fruit_citrus1_switch.isChecked,
                { },
                { failureMessage ->
                    Toast.makeText(
                        context,
                        "Fetch 1 Failed, reason:" + failureMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
        }

        fruit_go2_button.setOnClickListener {
            fruitCollectorModel.fetchFruits2(
                !fruit_citrus2_switch.isChecked,
                { },
                { failureMessage: UserMessage ->
                    Toast.makeText(
                        context,
                        "Fetch 2 Failed, reason:" + failureMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
        }

        fruit_go3_button.setOnClickListener {
            fruitCollectorModel.fetchFruits3(
                !fruit_citrus3_switch.isChecked,
                { },
                { failureMessage: UserMessage ->
                    Toast.makeText(
                        context,
                        "Fetch 3 Failed, reason:" + failureMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
        }

        fruit_clear_button.setOnClickListener { fruitCollectorModel.clearFruit() }

    }

    private fun setupAdapters() {

        fruitCollectorAdapter = FruitCollectorAdapter(fruitCollectorModel)

        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = RecyclerView.VERTICAL

        fruit_list_recycleview.layoutManager = linearLayoutManager1
        fruit_list_recycleview.adapter = fruitCollectorAdapter
    }

    private fun setupAnimationTriggers() {

        winAnimation = AnimatorSet()
        winAnimation.duration = 700

        fetchingStartedSyncTrigger = TriggerWhen({ fruitCollectorModel.anyNetworkThreadsBusy() }) {
            winAnimation.playTogether(
                ObjectAnimator.ofFloat(
                    fruit_fetchingfruitmessage_txt,
                    "alpha",
                    0f,
                    1f
                )
            )
            winAnimation.start()
        }

        fetchingStoppedSyncTrigger = TriggerWhen({ !fruitCollectorModel.anyNetworkThreadsBusy() }) {
            // temporarily make fetchingFruitMessage visible before we fade it, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            fruit_fetchingfruitmessage_txt.visibility = View.VISIBLE
            winAnimation.playTogether(
                ObjectAnimator.ofFloat(
                    fruit_fetchingfruitmessage_txt,
                    "alpha",
                    1f,
                    0f
                )
            )
            winAnimation.addListener(SyncerAnimationComplete(this))//onAnimationEnd() -> syncView()
            winAnimation.start()
        }

    }


    //reactive ui stuff below

    override fun onStart() {
        super.onStart()
        fruitCollectorModel.addObserver(observer)
        syncView() //  <- don't forget this
    }

    override fun onStop() {
        super.onStop()
        fruitCollectorModel.removeObserver(observer)
    }

    override fun syncView() {

        fruit_go1_button.visibility =
            if (fruitCollectorModel.isBusy1) View.INVISIBLE else View.VISIBLE
        fruit_busy1_prog.visibility =
            if (fruitCollectorModel.isBusy1) View.VISIBLE else View.INVISIBLE
        fruit_go2_button.visibility =
            if (fruitCollectorModel.isBusy2) View.INVISIBLE else View.VISIBLE
        fruit_busy2_prog.visibility =
            if (fruitCollectorModel.isBusy2) View.VISIBLE else View.INVISIBLE
        fruit_go3_button.visibility =
            if (fruitCollectorModel.isBusy3) View.INVISIBLE else View.VISIBLE
        fruit_busy3_prog.visibility =
            if (fruitCollectorModel.isBusy3) View.VISIBLE else View.INVISIBLE
        fruit_fetchingfruitmessage_txt.visibility =
            if (fruitCollectorModel.anyNetworkThreadsBusy()) View.VISIBLE else View.INVISIBLE
        fruit_clear_button.isEnabled = fruitCollectorModel.itemCount > 0
        fruit_totalcount_txt.text = "" + fruitCollectorModel.totalFruitCount
        fruit_totalcitruscount_txt.text = "" + fruitCollectorModel.totalCitrusFruitCount
        fruit_citrus1_switch.isEnabled = !fruitCollectorModel.isBusy1
        fruit_citrus2_switch.isEnabled = !fruitCollectorModel.isBusy2
        fruit_citrus3_switch.isEnabled = !fruitCollectorModel.isBusy3

        fetchingStartedSyncTrigger.checkLazy()
        fetchingStoppedSyncTrigger.checkLazy()

        fruitCollectorAdapter.notifyDataSetChangedAuto()
    }

    companion object {
        fun newInstance(): FruitCollectorFragment {
            return FruitCollectorFragment()
        }
    }

}
