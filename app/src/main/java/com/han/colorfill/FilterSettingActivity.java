package com.han.colorfill;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class FilterSettingActivity extends Activity {

    @BindView(R.id.ll_filter0)
    LinearLayout ll_filter0;
    @BindView(R.id.ll_filter1)
    LinearLayout ll_filter1;
    @BindView(R.id.ll_filter2)
    LinearLayout ll_filter2;
    @BindView(R.id.ll_filter3)
    LinearLayout ll_filter3;

    @BindView(R.id.tv_filter0)
    TextView tv_filter0;
    @BindView(R.id.tv_filter1)
    TextView tv_filter1;
    @BindView(R.id.tv_filter2)
    TextView tv_filter2;
    @BindView(R.id.tv_filter3)
    TextView tv_filter3;

    Context mContext;

    public static int R1=12;
    public static int G1 = 213;
    public static int B1=23;
    public static int Strength=5;

    private Messenger mServiceMessenger = null;
    private boolean mIsBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_setting);
        ButterKnife.bind(this);
        mContext = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        AdjustStrength();
    }

    @OnClick({R.id.ll_filter0,R.id.ll_filter1,R.id.ll_filter2,R.id.ll_filter3,R.id.ll_apply,R.id.ll_reset,R.id.ll_back}) void SelectFilter(View v){

        if(v.getId()==R.id.ll_filter0){
            ResetUI();
            R1 = 255; G1 = 10; B1 = 10;
            ll_filter0.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter0.setTextColor(getColor(R.color.colorWhite));
        }else if(v.getId()==R.id.ll_filter1){
            ResetUI();
            R1 = 1; G1 = 255;B1 = 1;
            ll_filter1.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter1.setTextColor(getColor(R.color.colorWhite));
        }else if(v.getId()==R.id.ll_filter2){
            ResetUI();
            R1 = 53; G1 = 51; B1 = 51;
            ll_filter2.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter2.setTextColor(getColor(R.color.colorWhite));
        }else if(v.getId()==R.id.ll_filter3){
            ResetUI();
            R1 = 13; G1 = 35; B1 = 233;
            ll_filter3.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter3.setTextColor(getColor(R.color.colorWhite));
        }else if(v.getId()==R.id.ll_reset){
            Log.e("HAN","reset");
            ResetFilter(mContext);
        }else if(v.getId()==R.id.ll_apply){
            ApplyFilter(mContext,R1,G1,B1);
            Log.e("HAN","apply");
        }else if(v.getId()==R.id.ll_back){
            finish();
        }
    }

    void ResetUI(){
        ll_filter0.setBackground(getDrawable(R.drawable.outline_textview));
        ll_filter1.setBackground(getDrawable(R.drawable.outline_textview));
        ll_filter2.setBackground(getDrawable(R.drawable.outline_textview));
        ll_filter3.setBackground(getDrawable(R.drawable.outline_textview));
        tv_filter0.setTextColor(getColor(R.color.colorBlack));
        tv_filter1.setTextColor(getColor(R.color.colorBlack));
        tv_filter2.setTextColor(getColor(R.color.colorBlack));
        tv_filter3.setTextColor(getColor(R.color.colorBlack));
    }

    void ApplyFilter(Context mContext, int R , int G , int B){
        Intent service = new Intent( mContext, ScreenFilterService.class );
        stopService(service);
        service.putExtra("R",R);
        service.putExtra("G",G);
        service.putExtra("B",B);

        //bindService(new Intent(this, ScreenFilterService.class), mConnection, Context.BIND_AUTO_CREATE);
        //mIsBound = true;

        startService(service);
        //getWindow().setStatusBarColor(Color.rgb(R,G,B));
    }

    void ResetFilter(Context mContext){
        Intent service = new Intent( mContext, ScreenFilterService.class );
        stopService(service);
    }

    void AdjustStrength(){
        SeekBar sb  = (SeekBar) findViewById(R.id.seekbar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Strength = progress;
                //ApplyFilter(mContext,R1,G1,B1);
                //sendMessageToService("from main");
                Log.e("HAN","Strength: "+Strength);
            }
        });
    }

//    private void setStartService() {
//        startService(new Intent(FilterSettingActivity.this, ScreenFilterService.class));
//        bindService(new Intent(this, ScreenFilterService.class), mConnection, Context.BIND_AUTO_CREATE);
//        mIsBound = true;
//    }
//
//    private void sendMessageToService(String str) {
//        if (mIsBound) {
//            if (mServiceMessenger != null) {
//                try {
//                    Message msg = Message.obtain(null, ScreenFilterService.MSG_SEND_TO_SERVICE, str);
//                    msg.replyTo = mMessenger;
//                    mServiceMessenger.send(msg);
//                } catch (RemoteException e) {
//                }
//            }
//        }
//    }
//
//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.d("HAN","onServiceConnected");
//            mServiceMessenger = new Messenger(iBinder);
//            try {
//                Message msg = Message.obtain(null, ScreenFilterService.MSG_REGISTER_CLIENT);
//                msg.replyTo = mMessenger;
//                mServiceMessenger.send(msg);
//            }
//            catch (RemoteException e) {
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//        }
//    };
//
//    /** Service 로 부터 message를 받음 */
//    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            Log.i("HAN","act : what "+msg.what);
//            switch (msg.what) {
//                case ScreenFilterService.MSG_SEND_TO_ACTIVITY:
//                    int value1 = msg.getData().getInt("fromService");
//                    String value2 = msg.getData().getString("test");
//                    Log.i("HAN","act : value1 "+value1);
//                    Log.i("HAN","act : value2 "+value2);
//                    break;
//            }
//            return false;
//        }
//    }));



    //        if( tb.isChecked() )
//        {
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (!Settings.canDrawOverlays(this)) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, 1234);
//                }
//            } else {
//                startService(service);
//            }
//        }
//        else
//        {
//            stopService( service );
//        }
        }