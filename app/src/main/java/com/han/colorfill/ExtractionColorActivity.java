package com.han.colorfill;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class ExtractionColorActivity extends Activity {

    private final int GET_GALLERY_IMAGE = 200;
    @BindView(R.id.iv_image) ImageView iv_image;
    @BindView(R.id.iv_color) ImageView iv_color;
    @BindView(R.id.tv_r) TextView tv_r;
    @BindView(R.id.tv_g) TextView tv_g;
    @BindView(R.id.tv_b) TextView tv_b;
    @BindView(R.id.tv_alpha) TextView tv_alpha;
    @BindView(R.id.tv_result) TextView tv_result;

    @BindView(R.id.ll_filter0) LinearLayout ll_filter0;
    @BindView(R.id.ll_filter1) LinearLayout ll_filter1;
    @BindView(R.id.ll_filter2) LinearLayout ll_filter2;
    @BindView(R.id.ll_filter3) LinearLayout ll_filter3;

    @BindView(R.id.tv_filter0) TextView tv_filter0;
    @BindView(R.id.tv_filter1) TextView tv_filter1;
    @BindView(R.id.tv_filter2) TextView tv_filter2;
    @BindView(R.id.tv_filter3) TextView tv_filter3;

    Context mContext;
    public static int R1=12;
    public static int G1 = 213;
    public static int B1=23;



    double[] A = new double[3];
    double[] B = new double[3];
    double[] C = new double[3];
    Bitmap ReferenceBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        ReferenceBitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
    }

    @OnClick({R.id.ll_back,R.id.tv_gallery}) void ClickBtn(View v){

        if(v.getId()==R.id.ll_back){
            finish();
        }else if(v.getId()==R.id.tv_gallery){
            LoadGallery();
        }
    }


    @OnTouch (R.id.iv_image) void GetRgb(View v, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:

                Drawable d = iv_image.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                int x = (int) event.getX();
                int y = (int) event.getY();

                Log.e("HAN","bitmap.getWidth(): "+bitmap.getWidth());
                Log.e("HAN","bitmap.getHeight(): "+bitmap.getHeight());
                Log.e("HAN","x: "+x);
                Log.e("HAN","y: "+y);
//                int x = (int) event.getRawX();
//                int y = (int) event.getRawY();
                int color = bitmap.getPixel(x, y);
                tv_r.setText(getResources().getString(R.string.rgb_info_r)+Integer.toString(Color.red(color)));
                tv_g.setText(getResources().getString(R.string.rgb_info_g)+Integer.toString(Color.green(color)));
                tv_b.setText(getResources().getString(R.string.rgb_info_b)+Integer.toString(Color.blue(color)));
                String Total =  getResources().getString(R.string.rgb_info_total)+Integer.toHexString(Color.red(color))+Integer.toHexString(Color.green(color))+Integer.toHexString(Color.blue(color));
                tv_alpha.setText(Total);
                iv_color.setBackgroundColor(color);
                tv_result.setText(Algorithm(color));

                break;
        }

    }
 

    @OnClick({R.id.ll_filter0,R.id.ll_filter1,R.id.ll_filter2,R.id.ll_filter3}) void Filter(View v){

        if(v.getId()==R.id.ll_filter0){
            ResetUI();
            ll_filter0.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter0.setTextColor(getColor(R.color.colorWhite));
            iv_image.setImageBitmap(REDTransform());  //TODO:  필터 변환
        }else if(v.getId()==R.id.ll_filter1){
            ResetUI();
            ll_filter1.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter1.setTextColor(getColor(R.color.colorWhite));
            iv_image.setImageBitmap(Transform(0.15f,0.5f,0.1f)); //TODO:   필터 변환
        }else if(v.getId()==R.id.ll_filter2){
            ResetUI();
            ll_filter2.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter2.setTextColor(getColor(R.color.colorWhite));
            iv_image.setImageBitmap(Transform(0.85f,0.6f,0.1f));  //TODO:  필터 변환
        }else if(v.getId()==R.id.ll_filter3){
            ResetUI();
            ll_filter3.setBackground(getDrawable(R.drawable.outline_textview_black_background));
            tv_filter3.setTextColor(getColor(R.color.colorWhite));
            iv_image.setImageBitmap(Transform(0.85f,0.5f,0.9f));  //TODO:  필터 변환
        }
    }

    public Bitmap REDTransform(){

        //Bitmap bitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
        Bitmap bitmap = ReferenceBitmap;

        Bitmap bit = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bitmap.getHeight(); i++) { // TODO: 적록색약 필터. YELLOW 값이 이상하게 변환되는 문제 발생. 대략 R>G,B && 7/8R < G && B < 1/5G 정도로 조건 줘보자
            for (int j = 0; j < bitmap.getWidth(); j++) {
//                int bWhite = bitmap.getPixel(j, i) & 0x000000;
                int bValue = bitmap.getPixel(j, i) & 0x000000FF;
                int gValue = (bitmap.getPixel(j, i) & 0x0000FF00) >> 8;
                int rValue = (bitmap.getPixel(j, i) & 0x00FF0000) >> 16;
                int t;
                if (gValue>200 && rValue>200 && bValue<rValue * 0.7) {
                    t = rValue;
                    rValue = (int) ((float) rValue);
                    bValue = (int) ((float) bValue);
                    gValue = (int) ((float) gValue);
                }
                else if (gValue>rValue && gValue>bValue && rValue>bValue) {
                    t = rValue;
                    rValue = (int) ((float) bValue);
                    bValue = (int) ((float) t);
                    gValue = (int) ((float) gValue);
                }
                else if (rValue>gValue && rValue>bValue && 2*rValue/3<gValue && bValue*2<gValue){
                    rValue = (int) ((float) rValue);
                    bValue = (int) ((float) bValue);
                    gValue = (int) ((float) gValue);
                }
                else if (rValue>gValue && rValue>bValue && gValue>bValue) {
                    t = bValue;
                    rValue = (int) ((float) rValue);
                    bValue = (int) ((float) gValue);
                    gValue = (int) ((float) t);
                }
                else {
                    rValue = (int) ((float) rValue);
                    bValue = (int) ((float) bValue);
                    gValue = (int) ((float) gValue);
                }
                int color = Color.rgb(rValue,gValue,bValue);
                bit.setPixel(j,i,color);
            }
        }
        return bit;
    }

    public Bitmap Transform(float R1, float G1, float B1){

        //Bitmap bitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
        Bitmap bitmap = ReferenceBitmap;

        Bitmap bit = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
//                int bWhite = bitmap.getPixel(j, i) & 0x000000;
                int bValue = bitmap.getPixel(j, i) & 0x000000FF;
                int gValue = (bitmap.getPixel(j, i) & 0x0000FF00) >> 8;
                int rValue = (bitmap.getPixel(j, i) & 0x00FF0000) >> 16;
                rValue = (int)((float)rValue*R1);
                bValue = (int)((float)bValue*G1);
                gValue = (int)((float)gValue*B1);
                int color = Color.rgb(rValue,gValue,bValue);
                bit.setPixel(j,i,color);
                if (gValue == 255 && bValue == 255 && rValue == 255) {
                }
            }
        }
        return bit;
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


    public void LoadGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }


    void ApplyFillter(){
        Intent service = new Intent( this, ScreenFilterService.class );
        startService(service);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            iv_image.setImageURI(selectedImageUri);
            ReferenceBitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
        }
    }

    //This Function For Algorithm

    public String Algorithm(int color){


        A[0] = not_RED((double)(Color.red(color)));
        A[1] = alittle_RED((double)(Color.red(color)));
        A[2] = almost_RED((double)(Color.red(color)));
        B[0] = not_GREEN((double)(Color.green(color)));
        B[1] = alittle_GREEN((double)(Color.green(color)));
        B[2] = almost_GREEN((double)(Color.green(color)));
        C[0] = not_BLUE((double)(Color.blue(color)));
        C[1] = alittle_BLUE((double)(Color.blue(color)));
        C[2] = almost_BLUE((double)(Color.blue(color)));

        double[] min = new double[27];
        int cnt = 0;
        double max = 0;
        int num=0; //min[27] 중 최댓값의 위치를 저장할 변수

        //for문이 27번 돌아가는 동안 min[] 배열에 저장됨과 동시에 최댓값을 찾는다.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    min[cnt] = min_(A[i], B[j], C[k]);

                    if (min[cnt] > max) {
                        max = min[cnt];
                        num = cnt;
                    }
                    cnt++;
                }
            }
        }

        if (num == 26)
            return "흰색";
        else if (num == 9 || num == 18 || num == 19 || num == 21 || num == 22)
            return "빨간색";
        else if (num == 12 || num == 24 || num == 25)
            return "노란색";
        else if (num == 3 || num == 4 || num == 6 || num == 7 || num == 15 || num == 16)
            return "초록색";
        else if (num == 1 || num == 2 || num == 5 || num == 8 || num == 17)
            return "파란색";
        else if (num == 10 || num == 11 || num == 14 || num == 20 || num == 23)
            return "보라색";
        else
            return "검은색";

    }
    double not_RED(double x) { // A[0]
        if (x < 50) return 1;
        else if (x >= 50 && x < 100) return (-x + 100) / 50;
        else return 0;
    }

    double alittle_RED(double x) { //A[1]
        if (x > 50 && x < 100) return (x - 50) / 50;
        else if (x >= 100 && x < 150) return 1;
        else if (x >= 150 && x < 200) return (-x + 200) / 50;
        else return 0;
    }

    double almost_RED(double x) { //A[2]
        if (x > 150 && x < 200) return (x - 150) / 50;
        else if (x >= 200) return 1;
        else return 0;
    }

    //G 퍼지집합
    double not_GREEN(double x) { //B[0]
        if (x < 50) return 1;
        else if (x >= 50 && x < 100) return (-x + 100) / 50;
        else return 0;
    }

    double alittle_GREEN(double x) { //B[1]
        if (x > 50 && x < 100) return (x - 50) / 50;
        else if (x >= 100 && x < 150) return 1;
        else if (x >= 150 && x < 200) return (-x + 200) / 50;
        else return 0;
    }

    double almost_GREEN(double x) { //B[2]
        if (x > 150 && x < 200) return (x - 150) / 50;
        else if (x >= 200) return 1;
        else return 0;
    }

    //B 퍼지집합
    double not_BLUE(double x) { //C[0]
        if (x < 50) return 1;
        else if (x >= 50 && x < 100) return (-x + 100) / 50;
        else return 0;
    }

    double alittle_BLUE(double x) { //C[1]
        if (x > 50 && x < 100) return (x - 50) / 50;
        else if (x >= 100 && x < 150) return 1;
        else if (x >= 150 && x < 200) return (-x + 200) / 50;
        else return 0;
    }

    double almost_BLUE(double x) { //C[2]
        if (x > 150 && x < 200) return (x - 150) / 50;
        else if (x >= 200) return 1;
        else return 0;
    }

    //세 값 중 최솟값을 찾아주는 함수
    double min_(double a, double b, double c) {
        double d;
        if (a < b)
            d = a;
        else
            d = b;
        if (d < c)
            return d;
        else
            return c;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1234) {
//            Intent service = new Intent( this, ScreenFilterService.class );
//            startService(service);
//        }
//    }

//    public void onClick( View v )
//    {
//
//
//        ToggleButton tb = (ToggleButton) v;
//        Intent service = new Intent( this, ScreenFilterService.class );
//
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
//    }

}
