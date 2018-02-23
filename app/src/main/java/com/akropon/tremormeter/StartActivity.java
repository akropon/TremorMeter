package com.akropon.tremormeter;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Класс стартовой активности.
 *
 * Активность обеспечивает выбор режима замера и переход к активности измерения.
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    /** onCreate
     *
     * Вызывается при создании активности.
     * Привязывает нужный лейоут. Привязывает обработчик нажатий к элементам лейоута.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((Button)findViewById(R.id.btn_as_1)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_2)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_3)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_4)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_5)).setOnClickListener(this);
    }

    /** Обработчик нажатия кнопок активности
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_as_1:
                launchMeasureActivityWithTime(3);
                break;
            case R.id.btn_as_2:
                launchMeasureActivityWithTime(5);
                break;
            case R.id.btn_as_3:
                launchMeasureActivityWithTime(10);
                break;
            case R.id.btn_as_4:
                launchMeasureActivityWithTime(15);
                break;
            case R.id.btn_as_5:
                launchMeasureActivityWithTime(20);
                break;
            default:
                break;
        }
    }


    /** Процедура запуска активности измерения и передача ей информации по выбранному времени
     *   измерения
     *
     * @param time - время измерения
     */
    private void launchMeasureActivityWithTime(int time) {
        Intent intent = new Intent(StartActivity.this, MeasureActivity.class);
        Mem.measureTimeInit = time;
        startActivity(intent);
    }
}
