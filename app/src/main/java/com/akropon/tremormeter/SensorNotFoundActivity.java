package com.akropon.tremormeter;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SensorNotFoundActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_not_found);


        ((Button)findViewById(R.id.btn_asnf_exit)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_asnf_exit:
                setResult(Cnst.RESULT_NEED_TO_EXIT);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Cnst.RESULT_NEED_TO_EXIT);
        finish();
    }
}
