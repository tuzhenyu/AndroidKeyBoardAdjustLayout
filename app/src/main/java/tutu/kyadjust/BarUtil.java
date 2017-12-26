package tutu.kyadjust;

import android.content.Context;
import android.support.annotation.MainThread;

import tutu.kayboardadjustlayout.R;


/**
 * Created by 涂臻宇 on 2017/12/6.
 * 用于检查布局高度变化是否由虚拟导航栏或者状态栏引起的
 */

public class BarUtil {
    private static final String TAG = "BarUtil";

    private final static String BAR_DEF_PACKAGE = "android";
    private final static String BAR_DEF_TYPE = "dimen";
    private final static String NAV_BAR_NAME = "navigation_bar_height";

    private final static String STATUS_BAR_NAME = "status_bar_height";

    private static int mNvaBarHeight = 0;

    private static int mStatusBarHeight = 0;

    @MainThread
    public static synchronized int getNvaBarHeight(Context context) {
        if(mNvaBarHeight > 0){
            return mNvaBarHeight;
        }

        int resourceId = context.getResources().
                getIdentifier(NAV_BAR_NAME, BAR_DEF_TYPE, BAR_DEF_PACKAGE);
        int NAV_BAR_HEIGHT ;
        if (resourceId > 0) {
            NAV_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
        }else{
            NAV_BAR_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.reco_navigation_bar_height);
        }
        mNvaBarHeight = NAV_BAR_HEIGHT;
        return mNvaBarHeight;
    }

    @MainThread
    public static synchronized int getStatusBarHeight(Context context){
        if(mStatusBarHeight > 0){
            return mStatusBarHeight;
        }

        int resourceId = context.getResources().
                getIdentifier(STATUS_BAR_NAME, BAR_DEF_TYPE, BAR_DEF_PACKAGE);
        int STATUS_BAR_HEIGHT ;
        if (resourceId > 0) {
            STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
        }else{
            STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.reco_status_bar_height);
        }
        mStatusBarHeight = STATUS_BAR_HEIGHT;
        return mStatusBarHeight;
    }

    /**
     * 判断布局变化是否是虚拟导航栏引起的
     * @param nowH 现在的布局高度差
     * */
    @MainThread
    public static boolean isNavBarChange(int nowH, Context context){
        int nvaBarHeight = getNvaBarHeight(context);
        return Math.abs(Math.abs(nowH) -nvaBarHeight ) <= 8;
    }

    /**
     * 判断布局变化是否是状态栏引起的
     * @param nowH 现在的布局高度差
     * */
    @MainThread
    public static boolean isStatusBarChange(int nowH, Context context){
        int statusBarHeight = getStatusBarHeight(context);
        return Math.abs(Math.abs(nowH) -statusBarHeight ) <= 8;
    }

}
