package tutu.kyadjust;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import tutu.kayboardadjustlayout.R;

/**
 * Created by tutu on 2017/12/10.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,ImeUtil.LayoutStateHost,ImeUtil.ImeStateObserver{

    View emojiBtn;
    View fakePanel;
    EditText editText;
    ImeStateHostImp imeStateHostImp;
    View forwardBtn;
    Runnable runnable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_fragment);
        forwardBtn = findViewById(R.id.forward_btn);
        emojiBtn = findViewById(R.id.emo_btn);
        fakePanel = findViewById(R.id.fake_panel);
        editText = (EditText) findViewById(R.id.compose_message_text);
        emojiBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        runnable = new Runnable() {
            @Override
            public void run() {
                imeStateHostImp = new ImeStateHostImp(MainActivity.this);
                imeStateHostImp.registerImeStateObserver(MainActivity.this);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(runnable != null){
            runnable.run();
            runnable = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imeStateHostImp.unregisterImeStateObserver(this);
    }

    boolean showEmoji;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emo_btn:
                if(showEmoji){
                    showEmoji = false;
                    if(!imeStateHostImp.isImeOpen()){
                        ImeUtil.showImeKeyboard(this,editText);
                    }
                }else{
                    showEmoji = true;
                    if(!imeStateHostImp.isImeOpen()){
                        fakePanel.setVisibility(View.VISIBLE);
                    }else{
                        ImeUtil.hideImeKeyboard(this,editText);
                    }
                }

                break;
            case R.id.forward_btn:
                break;
        }
    }

    @Override
    public boolean onDisplayHeightChanged(int heightMeasureSpec) {
        if(imeStateHostImp != null){
            imeStateHostImp.onDisplayHeightChanged(heightMeasureSpec);
            return true;
        }
        return false;
    }

    @Override
    public void onImeStateChanged(int state,int keyBoardHeight) {

        Log.d(ImeUtil.TAG,"onImeStateChanged IMoPEN = " + state);
        switch (state){
            case ImeUtil.IME_STATE_OPEN:
                fakePanel.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = fakePanel.getLayoutParams();
                layoutParams.height = keyBoardHeight;
                fakePanel.setLayoutParams(layoutParams);
                break;
            case ImeUtil.IME_STATE_HIDE:
                if(showEmoji){
                    fakePanel.setVisibility(View.VISIBLE);
                }else{
                    fakePanel.setVisibility(View.GONE);
                }
                break;
            case ImeUtil.IME_STATE_ADJUST:
                ViewGroup.LayoutParams layoutParamsAdjust = fakePanel.getLayoutParams();
                layoutParamsAdjust.height = keyBoardHeight;
                fakePanel.setLayoutParams(layoutParamsAdjust);
                break;
        }
    }
}
