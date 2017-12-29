package tutu.kyadjust;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
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
    private boolean oldImeOpened ;
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
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                //等主线程的UI绘制完了再注册监听器
                imeStateHostImp = new ImeStateHostImp(MainActivity.this);
                imeStateHostImp.registerImeStateObserver(MainActivity.this);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(oldImeOpened){
           Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
               @Override
               public boolean queueIdle() {
                   ImeUtil.showImeKeyboard(MainActivity.this,editText);
                   return false;
               }
           });
       }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onPause() {
        super.onPause();
        oldImeOpened = imeStateHostImp.isImeOpen();
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
                Intent intent = new Intent(this,SecondActivity.class);
                startActivity(intent);
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
