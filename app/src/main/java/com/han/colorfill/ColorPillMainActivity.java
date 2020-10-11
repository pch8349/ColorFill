package com.han.colorfill;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class ColorPillMainActivity extends Activity {

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pill_main);
        ButterKnife.bind(this);
        mContext = this;


        //Permission();
}

    @OnClick ({R.id.ll_tab0,R.id.ll_tab1,R.id.ll_tab2})void SelectType(View v){

        if(Permission()) {
            if (v.getId() == R.id.ll_tab0) {
                Intent intent = new Intent(mContext, FilterSettingActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.ll_tab1) {
                Intent intent = new Intent(mContext, ExtractionColorActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.ll_tab2) {
                Intent intent = new Intent(mContext, WebPageActivity.class);
                startActivity(intent);
            }
        }
    }



    public boolean Permission(){
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1234);
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {

        }
    }
}
