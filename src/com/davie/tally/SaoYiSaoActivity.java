package com.davie.tally;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.FileA3D;
import android.view.View;
import android.widget.Button;
import com.ericssonlabs.BarCodeTestActivity;
import org.apache.http.protocol.RequestConnControl;

/**
 * User: davie
 * Date: 15-4-13
 */
public class SaoYiSaoActivity extends Activity {
    private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saoyisao);

    }

    public void onClick(View view){
        Intent intent = new Intent();
        intent.setClass(this,BarCodeTestActivity.class);
        startActivityForResult(intent,998);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==998&&data!=null){

        }
    }
}