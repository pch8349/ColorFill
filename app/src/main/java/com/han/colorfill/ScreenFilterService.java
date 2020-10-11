package com.han.colorfill;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



/**
 * Created by sunphiz on 2014. 10. 3..
 */
public class ScreenFilterService extends Service
{

    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    int R1,G1,B1;


    public static final int MSG_REGISTER_CLIENT = 1;
    //public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SEND_TO_SERVICE = 3;
    public static final int MSG_SEND_TO_ACTIVITY = 4;
    private Messenger mClient = null;   // Activity 에서 가져온 Messenger

    @Override
    public void onCreate()
    {
        mView = new MyLoadView( this );

        mParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT );

//        mParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                        | WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                PixelFormat.TRANSLUCENT );

        /// WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        mWindowManager = ( WindowManager ) getSystemService( WINDOW_SERVICE );
        mWindowManager.addView( mView, mParams );

        super.onCreate();
    }

    @Override
    public IBinder onBind( Intent intent )
    {
//        R1 = intent.getIntExtra("R",0);
//        G1 = intent.getIntExtra("G",0);
//        B1 = intent.getIntExtra("B",0);
        return mMessenger.getBinder();
        //return null;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        (( WindowManager ) getSystemService( WINDOW_SERVICE )).removeView(
                mView );
        mView = null;
    }

    public class MyLoadView extends View
    {

        private Paint mPaint;

        public MyLoadView( Context context )
        {

            super( context );
            mPaint = new Paint();
            mPaint.setTextSize( 100 );
            mPaint.setARGB( 200, 10, 10, 10 );

            //            Window window = ((Activity)getApplicationContext()).getWindow();
//            window.setStatusBarColor(Color.rgb(R1,G1,B1));
            //mPaint.setARGB( 200, FilterSettingActivity.R1, FilterSettingActivity..G1, FilterSettingActivity..B1 );
        }

        @Override
        protected void onDraw( Canvas canvas )
        {
            super.onDraw( canvas );
           // canvas.drawARGB( 100, 255, 212, 0 );
           int Strength = (int)((float)FilterSettingActivity.Strength/100.f *255);
           Log.e("HAN","Strength: "+Strength);
           canvas.drawARGB( Strength, FilterSettingActivity.R1, FilterSettingActivity.G1, FilterSettingActivity.B1 );



//            Window window = ((Activity)getApplicationContext()).getWindow();
//            window.setStatusBarColor(Color.rgb(R1,G1,B1));
           //canvas.drawText( getString( R.string.app_name ), 0, 0, mPaint );
        }

        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow()
        {
            super.onDetachedFromWindow();
        }

        @Override
        protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
        {
            super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        }
    }

    /** activity로부터 binding 된 Messenger */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.w("HAN","ControlService - message what : "+msg.what +" , msg.obj "+ msg.obj);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClient = msg.replyTo;  // activity로부터 가져온
                    break;
            }
            return false;
        }
    }));


}