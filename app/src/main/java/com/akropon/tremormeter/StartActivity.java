package com.akropon.tremormeter;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((Button)findViewById(R.id.btn_as_mode1)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_mode2)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_mode3)).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_as_mode1:
                launchMeasureActivityWithMode(Cnst.Modes.FIRST);
                break;
            case R.id.btn_as_mode2:
                launchMeasureActivityWithMode(Cnst.Modes.SECOND);
                break;
            case R.id.btn_as_mode3:
                launchMeasureActivityWithMode(Cnst.Modes.THIRD);
                break;
            default:
                break;
        }
    }

    private void launchMeasureActivityWithMode(int mode) {
        Intent intent = new Intent(StartActivity.this, MeasureActivity.class);
        Mem.mode = mode;
        startActivity(intent);
    }
}
