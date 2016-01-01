package proj.me.colorgame.helper;

import android.animation.AnimatorSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by root on 28/11/15.
 */
public class ViewTouchListener implements View.OnTouchListener {
    private FragmentCallback fragmentCallback;
    private AnimatorSet tapAnim;
    public ViewTouchListener(FragmentCallback fragmentCallback, AnimatorSet tapAnim){
        this.fragmentCallback = fragmentCallback;
        this.tapAnim = tapAnim;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(fragmentCallback.getCurrentTappedId() == fragmentCallback.getCurrentGreyId()) {
                //fragmentCallback.setCurrentTappedId(0);
                return true;
            }
            tapAnim.setTarget(v);
            tapAnim.start();
            fragmentCallback.setCurrentTappedId(v.getId());
        }
        return true;
    }
}
