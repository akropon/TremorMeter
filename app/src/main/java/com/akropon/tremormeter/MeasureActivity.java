package com.akropon.tremormeter;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MeasureActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_start;
    TextView txt_am_info;

    boolean isTimerWorking;
    int measureTimeLeft;
    Timer timer;

    SensorManager sensorManager;
    Sensor sensorAcc;
    SensorEventListener listenerAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        btn_start = findViewById(R.id.btn_am_start);
        txt_am_info = findViewById(R.id.txt_am_info);

        btn_start.setOnClickListener(this);

        txt_am_info.setText("Выбранный режим: "+Mem.mode
                +"\nНажмите СТАРТ чтобы начать замер.");


        boolean isTimerWorking = false;
        int measureTimeLeft = 0;
        Timer timer = null;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        listenerAcc = new AccListner();
        int a = 0;
        a++;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_am_start:
                onClickBtnStart();
                break;
            default:
                break;
        }
    }

    private void onClickBtnStart() {
        Mem.measureTimeInit = 3; // TODO это должно чем-то определяться как-то иначе

        if (isTimerWorking==false) {
            isTimerWorking = true;
            measureTimeLeft = Mem.measureTimeInit;

            updateUI_sync();

            Mem.accMeasuredArray.clear();

            timer = new Timer();
            TimerTask task = new TimerTaskRealization();
            timer.schedule(task, 1000, 1000); // первое выполнение через 1 сек, затем выполнение через каждую 1 сек

            boolean success = sensorManager.registerListener(
                    listenerAcc, sensorAcc, SensorManager.SENSOR_DELAY_FASTEST);


            /*  // проверочка для отладки
            if (success)
                Log.i("[akropon]", "accSensorListener succesfully attached");
            else
                Log.e("[akropon]", "accSensorListener unsuccesfully attached");*/


        }

    }

    @Override
    protected void onPause() {
        timer.cancel();
        isTimerWorking = false;
        super.onPause();
    }



    private void updateUI_sync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isTimerWorking) {
                    txt_am_info.setText("time left = "+measureTimeLeft);
                } else {
                    txt_am_info.setText("timer is OFF");
                }

            }
        });
    }

    private void endOfMesuring_sync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MeasureActivity.this, ResultActivity.class);
                startActivity(intent);

            }
        });
    }

    private class TimerTaskRealization extends TimerTask {
        @Override
        public void run() {
            measureTimeLeft--;
            updateUI_sync();

            if (measureTimeLeft == 0) {
                isTimerWorking = false; // уведомляем о завершении работы таймера
                timer.cancel(); // снимаем задачу таймера
            }
        }
    }


    private class AccListner implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (isTimerWorking) {
                Mem.accMeasuredArray.add(new PointV3(sensorEvent.values));
            } else {
                sensorManager.unregisterListener(listenerAcc); // отвязываем лиснер
                endOfMesuring_sync();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
}
