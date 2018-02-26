package com.akropon.tremormeter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

/**
 * Класс активности результата измерения.
 *
 * Обеспечивает анализ данных измерения и вывод результатов на экран.
 */
public class ResultActivity extends AppCompatActivity {

    /** Поле графика */
    GraphView graphView1;

    TextView txt_ar_rate;
    TextView txt_ar_amp;

    /** Класс, обеспечивающий асинхронный анализ данных */
    AnalysisExicutor analysisExicutor;

    /** onCreate
     *
     * Вызывается при создании активности.
     * Привязывает нужный лейоут.
     * Привязывает переменные к элементам активности.
     * Задает начальные состояния элементов активности.
     * Запускает асинхронный анализ данных замера.
     * Настраивает поле графика
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        graphView1 = findViewById(R.id.graphView1);
        txt_ar_rate = findViewById(R.id.txt_ar_rate);
        txt_ar_amp = findViewById(R.id.txt_ar_amp);

        analysisExicutor = new AnalysisExicutor();
        analysisExicutor.start();

        graphView1.setBackgroundColor(getResources().getColor(R.color.softLightGreen));
    }

    /**
     * Переопрадаление метода, вызывающегося при нажатии кнопки "Назад"
     *
     * Закрываем активность.
     */
    @Override
    public void onBackPressed() {
        setResult(Cnst.RESULT_GO_TO_START_ACTIVITY);
        finish();
    }

    /** Метод для вывода результатов анализа данных. (синхронизирован)
     *
     *  Может вызываться из любых потоков.
     */
    protected void endOfAnalysis_sync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // TODO

                showGraphFor(Mem.arrTime, Mem.arrAccX, Color.RED, graphView1);
                showGraphFor(Mem.arrTime, Mem.arrAccY, Color.GREEN, graphView1);
                showGraphFor(Mem.arrTime, Mem.arrAccZ, Color.BLUE, graphView1);

                //showGraphFor(Mem.arrTime, Mem.arrAccModule, Color.GRAY, graphView1);
                float[] sinAcc = new float[Mem.arrTime.length];
                double timeMoment;
                double steps_per_second = (double)Mem.arrTime.length / Mem.measureTimeInit;
                for (int i=0; i<Mem.arrTime.length; i++) {
                    timeMoment = i / steps_per_second;
                    sinAcc[i] = (float)(Mem.ampAcc*Math.sin(2*Math.PI*Mem.rate*timeMoment+0));
                }
                showGraphFor(Mem.arrTime, sinAcc, Color.GRAY, graphView1);


                graphView1.getViewport().setScalable(true);
                graphView1.getViewport().setScrollable(true);
                graphView1.getViewport().setScalableY(true);
                graphView1.getViewport().setScrollableY(true);


                txt_ar_rate.setText(String.format("%.1f", Mem.rate));
                float apmPos_mm = Mem.ampPos*1000;  // "*1000" - переводим метры в мм
                if ( apmPos_mm < 0.5f) {
                    txt_ar_amp.setText(String.format("менее"+" 0.5"));
                } else {
                    txt_ar_amp.setText(String.format("%.1f", apmPos_mm));
                }
            }
        });
    }


    /**
     * Класс, обеспечивающий выполнение анализа данных замера асинхронно.
     */
    private class AnalysisExicutor extends Thread {
        @Override
        public void run() {

            Mem.arrTime = new float[Mem.accMeasuredArray.size()];
            Mem.arrAccX = new float[Mem.accMeasuredArray.size()];
            Mem.arrAccY = new float[Mem.accMeasuredArray.size()];
            Mem.arrAccZ = new float[Mem.accMeasuredArray.size()];

            PointV3 point;
            for (int i = 0; i < Mem.arrTime.length; i++) {
                point = Mem.accMeasuredArray.get(i);
                Mem.arrTime[i] = i;
                Mem.arrAccX[i] = point.x;
                Mem.arrAccY[i] = point.y;
                Mem.arrAccZ[i] = point.z;
            }

            float[] arrVar;
            int[] arrVarI;
            float var;
            int windowWidth;

            var = MathUtils.getAverage(Mem.arrAccX);
            MathUtils.addToEach(-var, Mem.arrAccX, true);
            var = MathUtils.getAverage(Mem.arrAccY);
            MathUtils.addToEach(-var, Mem.arrAccY, true);
            var = MathUtils.getAverage(Mem.arrAccZ);
            MathUtils.addToEach(-var, Mem.arrAccZ, true);


            windowWidth = 3;
            Mem.arrAccX = MathUtils.getFilteredArr(Mem.arrAccX, windowWidth);
            Mem.arrAccY = MathUtils.getFilteredArr(Mem.arrAccY, windowWidth);
            Mem.arrAccZ = MathUtils.getFilteredArr(Mem.arrAccZ, windowWidth);

            Mem.arrAccModule = MathUtils.module(Mem.arrAccX, Mem.arrAccY, Mem.arrAccZ);

            /*int filterWindowWidth = 60;
            arrVar = MathUtils.getFilteredArr(Mem.arrAccX, filterWindowWidth);
            Mem.arrAccX = MathUtils.subEach(Mem.arrAccX, arrVar);
            arrVar = MathUtils.getFilteredArr(Mem.arrAccY, filterWindowWidth);
            Mem.arrAccY = MathUtils.subEach(Mem.arrAccY, arrVar);
            arrVar = MathUtils.getFilteredArr(Mem.arrAccZ, filterWindowWidth);
            Mem.arrAccZ = MathUtils.subEach(Mem.arrAccZ, arrVar);*/

            float avgAccXMaxes;
            float avgAccXMines;
            float avgAccYMaxes;
            float avgAccYMines;
            float avgAccZMaxes;
            float avgAccZMines;

            int accXMaxesAmount;
            int accXMinesAmount;
            int accYMaxesAmount;
            int accYMinesAmount;
            int accZMaxesAmount;
            int accZMinesAmount;

            // windowWidth - в названии "длина", но использоватьсо будет как "радиус"
            // N_rad = окр.вверх( N_all / 12 Гц / 4 четверти периода / T_all)
            windowWidth = (int)(Math.ceil( Mem.arrTime.length / 12.0 / 4 / Mem.measureTimeInit ));
            Log.i("[akropon]", "AnalysisExicutor.run(): smoothingWindowRadius="+windowWidth);

            arrVarI = MathUtils.findMaxes(Mem.arrAccX, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccX[arrVarI[i]];
            avgAccXMaxes = MathUtils.getAverage(arrVar);
            accXMaxesAmount = arrVarI.length;

            arrVarI = MathUtils.findMines(Mem.arrAccX, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccX[arrVarI[i]];
            avgAccXMines = MathUtils.getAverage(arrVar);
            accXMinesAmount = arrVarI.length;


            arrVarI = MathUtils.findMaxes(Mem.arrAccY, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccY[arrVarI[i]];
            avgAccYMaxes = MathUtils.getAverage(arrVar);
            accYMaxesAmount = arrVarI.length;

            arrVarI = MathUtils.findMines(Mem.arrAccY, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccY[arrVarI[i]];
            avgAccYMines = MathUtils.getAverage(arrVar);
            accYMinesAmount = arrVarI.length;


            arrVarI = MathUtils.findMaxes(Mem.arrAccZ, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccZ[arrVarI[i]];
            avgAccZMaxes = MathUtils.getAverage(arrVar);
            accZMaxesAmount = arrVarI.length;

            arrVarI = MathUtils.findMines(Mem.arrAccZ, windowWidth);
            arrVar = new float[arrVarI.length];
            for (int i=0; i<arrVarI.length; i++)
                arrVar[i] = Mem.arrAccZ[arrVarI[i]];
            avgAccZMines = MathUtils.getAverage(arrVar);
            accZMinesAmount = arrVarI.length;

            float ampAccX = (avgAccXMaxes-avgAccXMines) / 2f;
            float ampAccY = (avgAccYMaxes-avgAccYMines) / 2f;
            float ampAccZ = (avgAccZMaxes-avgAccZMines) / 2f;

            // примерная амплитуда
            float ampAccMod = MathUtils.module(ampAccX, ampAccY, ampAccZ);
            // примерная частота
            float rate =
                    (accXMaxesAmount+accXMinesAmount
                    +accYMaxesAmount+accYMinesAmount
                    +accZMaxesAmount+accZMinesAmount) / 6f / Mem.measureTimeInit;

            // acc(t) = Amp_acc * sin(w*t+w0_acc)
            // pos(t) = Amp_pos * sin(w*t+w0_pos)
            // pos(t) = acc(t) / dt*dt
            // acc(t) / dt*dt = Amp_acc*w*w * (-1)*sin(w*t+w0_acc)
            // Amp_pos = Amp_acc/w/w = Amp_acc / w^2 = Amp_acc / (2*PI*rate)^2

            double _2_PI_rate = 2 * Math.PI * rate;
            float ampPosMod = (float)(ampAccMod / _2_PI_rate / _2_PI_rate);

            Mem.ampAcc = ampAccMod;
            Mem.ampPos = ampPosMod;
            Mem.rate = rate;

            endOfAnalysis_sync();



        }


    }

    /** Построение графика функции в виде линий
     *
     * @param arrX - массив значений по оси абсцисс
     * @param arrY - массив значений по оси ординат
     * @param color - цвет
     * @param graphView - поле графика на активности
     */
    private void showGraphFor(float[] arrX, float[] arrY, int color, GraphView graphView) {
        DataPoint[] dataPoints = new DataPoint[arrX.length];
        for (int i = 0; i < arrX.length; i++)
            dataPoints[i] = new DataPoint(arrX[i], arrY[i]);
        LineGraphSeries<DataPoint> series
                = new LineGraphSeries<>(dataPoints);
        series.setColor(color);
        graphView.addSeries(series);
    }

    /** Построение графика функции в виде отдельных точек
     *
     * @param arrX - массив значений по оси абсцисс
     * @param arrY - массив значений по оси ординат
     * @param color - цвет
     * @param graphView - поле графика на активности
     */
    private void showPointsFor(float[] arrX, float[] arrY, int color, GraphView graphView) {
        DataPoint[] dataPoints = new DataPoint[arrX.length];
        for (int i = 0; i < arrX.length; i++)
            dataPoints[i] = new DataPoint(arrX[i], arrY[i]);
        PointsGraphSeries<DataPoint> series
                = new PointsGraphSeries<>(dataPoints);
        series.setColor(color);
        graphView.addSeries(series);
    }

    /** Метод Sleep(...) для вызывающего потока.
     *
     * Обертка для метода Thread.sleep(long millis), заглушающая исключения
     * @param millis - время в миллисекундах
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }
}
