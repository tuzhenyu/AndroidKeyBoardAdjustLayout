package tutu.kyadjust;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ImeDetectLinearLayout extends LinearLayout {
    public ImeDetectLinearLayout(Context context) {
        super(context);
    }

    public ImeDetectLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImeDetectLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int measuredHeight = getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measuredHeight != getMeasuredHeight() && getContext() instanceof ImeUtil.LayoutStateHost) {
            Log.d(ImeUtil.TAG,"measuredHeight != getMeasuredHeight() ,getMeasuredHeight()  - measuredHeight = " + (getMeasuredHeight()  - measuredHeight));
             boolean needMeasure = ((ImeUtil.LayoutStateHost) getContext()).onDisplayHeightChanged(heightMeasureSpec);
             if(needMeasure){
                 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
             }
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
