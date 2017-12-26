package tutu.kyadjust;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/26.
 *
 */

public class ImeStateHostImp implements ImeUtil.ImeStateHost {

    private AppCompatActivity mContext;

    // Used to determine if a onDisplayHeightChanged was due to the IME opening or rotation of the
    // device
    private int mLastScreenHeight;

    // Tracks the soft keyboard display state
    private boolean mImeOpen;

    // Tracks the list of observers opting in for IME state change.
    private final Set<ImeUtil.ImeStateObserver> mImeStateObservers = new HashSet<>();

    public ImeStateHostImp(AppCompatActivity context) {
        this.mContext = context;
        mLastScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override

    public void onDisplayHeightChanged(int heightMeasureSpec) {
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;

        if (screenHeight != mLastScreenHeight) {
            // Appears to be an orientation change, don't fire ime updates
            mLastScreenHeight = screenHeight;
            return;
        }
        final ActionBar actionBar = mContext.getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            screenHeight -= actionBar.getHeight();
        }

        screenHeight -= BarUtil.getStatusBarHeight(mContext);

        final int height = View.MeasureSpec.getSize(heightMeasureSpec);

        final boolean imeWasOpen = mImeOpen;
        int durHeight = screenHeight - height;
        Log.d(ImeUtil.TAG,"onDisplayHeightChanged durHeight = " + durHeight);

        mImeOpen = durHeight > 100 && !BarUtil.isNavBarChange(durHeight,mContext);


        if (imeWasOpen != mImeOpen) {
            for (final ImeUtil.ImeStateObserver observer : mImeStateObservers) {
                observer.onImeStateChanged(mImeOpen,durHeight);
            }
        }
    }

    @Override
    public void registerImeStateObserver(ImeUtil.ImeStateObserver observer) {
        mImeStateObservers.add(observer);
    }

    @Override
    public void unregisterImeStateObserver(ImeUtil.ImeStateObserver observer) {
        mImeStateObservers.remove(observer);
    }

    @Override
    public boolean isImeOpen() {
        return mImeOpen;
    }
}
