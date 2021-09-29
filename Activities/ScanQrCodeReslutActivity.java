package com.rogerthat.rlvltd.com.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.R;

public class ScanQrCodeReslutActivity extends AppCompatActivity {
    public TextView tvresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_code_reslut);
        tvresult = (TextView) findViewById(R.id.txt_qr_result);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        if (result != null) {
            tvresult.setText(result);
        }
    }
}