package foo.bar.example.fore.fullapp02.ui.fruitcollector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.early.fore.core.callbacks.FailureCallbackWithPayload
import co.early.fore.core.callbacks.SuccessCallback
import co.early.fore.core.ui.SyncTrigger
import co.early.fore.core.ui.SyncableView
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.message.UserMessage
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerAnimationComplete
import kotlinx.android.synthetic.main.fragment_fruitcollector.view.*

/**
 * For the fruit collector example we manage fore observers at the **Fragment** level
 * [FruitCollectorFragment]
 */
class FruitCollectorView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr), SyncableView {

    //models that we need to sync with
    private lateinit var fruitCollectorModel: FruitCollectorModel

    //other UI stuff
    private lateinit var fruitCollectorAdapter: FruitCollectorAdapter
    private lateinit var winAnimation: AnimatorSet
    private lateinit var fetchingStartedSyncTrigger: SyncTrigger
    private lateinit var fetchingStoppedSyncTrigger: SyncTrigger


    override fun onFinishInflate() {
        super.onFinishInflate()

        getModelReferences()

        setupButtonClickListeners()

        setupAdapters()

        setupAnimationTriggers()
    }


    private fun getModelReferences() {
        fruitCollectorModel = App.inst.appComponent.fruitCollectorModel
    }


    private fun setupButtonClickListeners() {

        fruit_go1_button.setOnClickListener {
            fruitCollectorModel.fetchFruits1(
                                !fruit_citrus1_switch.isChecked,
                                SuccessCallback { },
                                FailureCallbackWithPayload { failureMessage ->
                                   Toast.makeText(
                                       this@FruitCollectorView.context,
                                       "Fetch 1 Failed, reason:" + failureMessage,
                                       Toast.LENGTH_LONG
                                   ).show()
                                })
        }

        fruit_go2_button.setOnClickListener {
            fruitCollectorModel.fetchFruits2(
                               !fruit_citrus2_switch.isChecked,
                               SuccessCallback { },
                               FailureCallbackWithPayload { failureMessage: UserMessage ->
                                   Toast.makeText(
                                       this@FruitCollectorView.context,
                                       "Fetch 2 Failed, reason:" + failureMessage,
                                       Toast.LENGTH_LONG
                                   ).show()
                               })
        }

        fruit_go3_button.setOnClickListener {
            fruitCollectorModel.fetchFruits3(
                               !fruit_citrus3_switch.isChecked,
                               SuccessCallback { },
                               FailureCallbackWithPayload { failureMessage: UserMessage ->
                                   Toast.makeText(
                                       this@FruitCollectorView.context,
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

        fetchingStartedSyncTrigger = SyncTrigger(SyncTrigger.DoThisWhenTriggered {
            winAnimation.playTogether(ObjectAnimator.ofFloat(fruit_fetchingfruitmessage_txt, "alpha", 0f, 1f))
            winAnimation.start()
        }, SyncTrigger.CheckTriggerThreshold { fruitCollectorModel.anyNetworkThreadsBusy() })

        fetchingStoppedSyncTrigger = SyncTrigger(SyncTrigger.DoThisWhenTriggered {
            // temporarily make fetchingFruitMessage visible before we fade it, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            fruit_fetchingfruitmessage_txt.visibility = View.VISIBLE
            winAnimation.playTogether(ObjectAnimator.ofFloat(fruit_fetchingfruitmessage_txt, "alpha", 1f, 0f))
            winAnimation.addListener(SyncerAnimationComplete(this@FruitCollectorView))//onAnimationEnd() -> syncView()
            winAnimation.start()
        }, SyncTrigger.CheckTriggerThreshold { !fruitCollectorModel.anyNetworkThreadsBusy() })

    }


    //data binding stuff below

    override fun syncView() {

        fruit_go1_button.visibility = if (fruitCollectorModel.isBusy1) View.INVISIBLE else View.VISIBLE
        fruit_busy1_prog.visibility = if (fruitCollectorModel.isBusy1) View.VISIBLE else View.INVISIBLE
        fruit_go2_button.visibility = if (fruitCollectorModel.isBusy2) View.INVISIBLE else View.VISIBLE
        fruit_busy2_prog.visibility = if (fruitCollectorModel.isBusy2) View.VISIBLE else View.INVISIBLE
        fruit_go3_button.visibility = if (fruitCollectorModel.isBusy3) View.INVISIBLE else View.VISIBLE
        fruit_busy3_prog.visibility = if (fruitCollectorModel.isBusy3) View.VISIBLE else View.INVISIBLE
        fruit_fetchingfruitmessage_txt.visibility = if (fruitCollectorModel.anyNetworkThreadsBusy()) View.VISIBLE else View.INVISIBLE
        fruit_clear_button.isEnabled = fruitCollectorModel.fruitListSize > 0
        fruit_totalcount_txt.text = "" + fruitCollectorModel.totalFruitCount
        fruit_totalcitruscount_txt.text = "" + fruitCollectorModel.totalCitrusFruitCount
        fruit_citrus1_switch.isEnabled = !fruitCollectorModel.isBusy1
        fruit_citrus2_switch.isEnabled = !fruitCollectorModel.isBusy2
        fruit_citrus3_switch.isEnabled = !fruitCollectorModel.isBusy3

        fetchingStartedSyncTrigger.checkLazy()
        fetchingStoppedSyncTrigger.checkLazy()

        fruitCollectorAdapter.notifyDataSetChangedAuto()
    }


    /**
     * For these examples we are managing fore observers at the **Fragment**
     * level, so the below is not required
     */
    //    @Override
    //    protected void onAttachedToWindow() {
    //        super.onAttachedToWindow();
    //        fruitCollectorModel.addObserver(observer);
    //        syncView();  // <- don't forget this
    //    }
    //
    //
    //    @Override
    //    protected void onDetachedFromWindow() {
    //        super.onDetachedFromWindow();
    //        fruitCollectorModel.removeObserver(observer);
    //    }

}
