package tutu.kyadjust;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import junit.framework.Assert;

public class ImeUtil {

    public static final String TAG = "IME";

    public interface ImeStateObserver {
        void onImeStateChanged(boolean imeOpen,int keyBoardHeight);
    }

    public interface ImeStateHost {
        void onDisplayHeightChanged(int heightMeasureSpec);
        void registerImeStateObserver(ImeUtil.ImeStateObserver observer);
        void unregisterImeStateObserver(ImeUtil.ImeStateObserver observer);
        boolean isImeOpen();
    }

    public interface LayoutStateHost {
        boolean onDisplayHeightChanged(int heightMeasureSpec);
    }

    public static void hideImeKeyboard(@NonNull final Context context, @NonNull final View v) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(v);

        final InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0 /* flags */);
        }
    }

    public static void showImeKeyboard(@NonNull final Context context, @NonNull final View v) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(v);

        final InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            v.requestFocus();
            inputMethodManager.showSoftInput(v, 0 /* flags */);
        }
    }

   /* public static void hideSoftInput(@NonNull final Context context, @NonNull final View v) {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }*/

}
