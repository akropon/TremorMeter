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
        ((Button)findViewById(R.id.btn_as_mode1)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_mode2)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_as_mode3)).setOnClickListener(this);
    }

    /** Обработчик нажатия кнопок активности
     *
     * @param view
     */
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

    /** Процедура запуска активности измерения и передача ей информации по выбранному режиму
     *   измерения
     *
     * @param mode - режим измерения
     */
    private void launchMeasureActivityWithMode(int mode) {
        Intent intent = new Intent(StartActivity.this, MeasureActivity.class);
        Mem.mode = mode;
        startActivity(intent);
    }
}
