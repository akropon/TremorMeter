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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/** Класс активности измерения
 *
 * Обеспечивает измерение и запуск активности вывода результата
 *
 */
public class MeasureActivity extends AppCompatActivity implements View.OnClickListener{

    /** Кнопка запуска измерения */
    Button btn_start;
    /** Информационное текстовое поле */
    TextView txt_am_info;
    /** Статусное текстовое поле */
    TextView txt_am_status;
    /** Основной лэйоут */
    LinearLayout layoutMain;

    /** Состояние таймера (true - запущен и работает, false -  не запущен) */
    boolean isTimerWorking;
    /** Сколько времени до конца измерения (включая  осталось в секундах */
    int timeLeft;
    /** Объект таймера */
    Timer timer;
    /** Вызывался ли onResume() раньше */
    boolean isZeroTimesResumed;

    /** Ссылка на менеджер сенсоров устройства */
    SensorManager sensorManager;
    /** Ссылка на сенсор ускорения */
    Sensor sensorAcc;
    /** Ссылка на обработчик события изменения сенсора ускорения */
    SensorEventListener listenerAcc;

    /** onCreate
     *
     * Вызывается при создании активности.
     * Привязывает нужный лейоут.
     * Привязывает переменные к элементам активности.
     * Задает начальные значения остальных переменных.
     * Задает начальные состояния элементов активности.
     * Настраивает сенсор и обработчик сенсора (но не запускает их).
     * Привязывает обработчик нажатий к элементам лейоута.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        btn_start = findViewById(R.id.btn_am_start);
        txt_am_info = findViewById(R.id.txt_am_info);
        txt_am_status = findViewById(R.id.txt_am_status);
        layoutMain = findViewById(R.id.lyt_am_main);

        btn_start.setOnClickListener(this);

        txt_am_info.setText("Выбранный режим: "+Mem.mode
                +"\nНажмите СТАРТ чтобы начать замер.");


        isTimerWorking = false;
        timeLeft = 0;
        timer = null;
        isZeroTimesResumed = true;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        listenerAcc = new AccListner();
    }

    /** Обработчик нажатия кнопок активности
     *
     * @param view
     */
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

    /**
     * Процедура обработки нажатия кнопки запуска замера.
     *
     * Инициализирует начальные переменные слежения за временем.
     * Обновляет информацию на экране.
     * Создает и запускает таймер слежения за временем замера и обеспечением обновления информации
     *   на экране.
     * Привязывает обработчик событий изменения сенсора ускорения к сенсору ускорения, инициируя тем
     *   самым начало измерения.
     */
    private void onClickBtnStart() {

        if (isTimerWorking==false) {
            isTimerWorking = true;
            timeLeft = Mem.measureTimeInit + Cnst.delayTimeInit;

            //updateUI_sync();
            btn_start.setTextSize(60);
            btn_start.setText(String.valueOf(Cnst.delayTimeInit));
            layoutMain.setBackgroundResource(R.color.bckgndDelay);
            txt_am_status.setText("ЗАДЕРЖКА");

            Mem.accMeasuredArray.clear();

            timer = new Timer();
            TimerTask task = new TimerTaskRealization();
            timer.schedule(task, 1000, 1000); // первое выполнение через 1 сек, затем выполнение через каждую 1 сек



            /*  // проверочка для отладки
            if (success)
                Log.i("[akropon]", "accSensorListener succesfully attached");
            else
                Log.e("[akropon]", "accSensorListener unsuccesfully attached");*/


        }

    }

    /**
     * Переопределение метода onPause()
     *
     * Останавливает таймер и "обнуляет" переменную его состояния
     */
    @Override
    protected void onPause() {
        if (isTimerWorking) {
            timer.cancel();
            sensorManager.unregisterListener(listenerAcc);
        }

        super.onPause();
    }

    /**
     * Переопределение метода onResume()
     *
     * Если активность встает в фокус на в первый раз, то сразу же закрывается.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (isZeroTimesResumed) {
            isZeroTimesResumed = false;
        } else {
            this.finish();
        }
    }

    /**
     * Переопрадаление метода, вызывающегося при нажатии кнопки "Назад"
     *
     * Закрываем активность.
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * Метод, вызывающийся по окончанию процедуры измерения, когда все значения уже сняты
     *   с сенсора и записаны в соответствующие массивы. (синхронизированный).
     *
     * Метод обеспечивает переход к активности вывода результатов.
     *
     * Метод можно вызывать из любых других потоков.
     */
    private void endOfMesuring_sync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MeasureActivity.this, ResultActivity.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Реализация таймера с интервалом в секунду, занимающийся отсчетом оставшегося времени
     *   измерения и обновлением состояния выводимой на экран информации
     */
    private class TimerTaskRealization extends TimerTask {
        @Override
        public void run() {
            timeLeft--;

            if (timeLeft == Mem.measureTimeInit) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        txt_am_status.setText("ЗАМЕР");
                        layoutMain.setBackgroundResource(R.color.bckgndMeasure);
                        sensorManager.registerListener(
                                listenerAcc, sensorAcc, SensorManager.SENSOR_DELAY_FASTEST);

                    }
                });
            }

            if (timeLeft > Mem.measureTimeInit) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btn_start.setText(String.valueOf(timeLeft - Mem.measureTimeInit));

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        btn_start.setText(String.valueOf(timeLeft));

                    }
                });
            }


            if (timeLeft == 0) {
                isTimerWorking = false; // уведомляем о завершении работы таймера
                timer.cancel(); // снимаем задачу таймера
            }


        }
    }


    /**
     * Реализация обработчика событий изменения сенсора.
     *
     * Предназначен для сбора информации с сенсоров и записи ее в соответствующие массивы,
     * пока это необходимо. По окончанию необходимости сбора информации, обработчик сам себя
     * отвязывает от сенсора.
     */
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
