package proj.me.colorgame.helper;

/**
 * Created by root on 28/11/15.
 */
public interface FragmentCallback {
    void setCurrentTappedId(int viewId);
    int getCurrentTappedId();
    int getCurrentGreyId();
}
