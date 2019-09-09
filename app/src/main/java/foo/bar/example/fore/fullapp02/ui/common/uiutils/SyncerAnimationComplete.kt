package foo.bar.example.fore.fullapp02.ui.common.uiutils


import android.animation.Animator
import co.early.fore.core.ui.SyncableView

/**
 *
 */
class SyncerAnimationComplete(private val syncableView: SyncableView) : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator) {
        //no opp
    }

    override fun onAnimationEnd(animation: Animator) {
        syncableView.syncView()
    }

    override fun onAnimationCancel(animation: Animator) {
        //no opp
    }

    override fun onAnimationRepeat(animation: Animator) {
        //no opp
    }
}
