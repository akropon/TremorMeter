package com.akropon.tremormeter;

import java.util.ArrayList;

/**
 * Memory Module
 * Модуль памяти
 *
 * Здесь собраны глобальные переменные
 *
 */
public class Mem {

    /** Выбранный режим измерения (# от носа, на вытянутой руки и тп) */
    public static int mode = 0;
    /** Массив измеренных значений */
    public static ArrayList<PointV3> accMeasuredArray = new ArrayList<>();

    /** Длительность замера в секундах */
    public static int measureTimeInit;

    /** Массив моментов времени */
    public static float[] arrTime;
    /** Массив ускорений по оси X */
    public static float[] arrAccX;
    /** Массив ускорений по оси Y */
    public static float[] arrAccY;
    /** Массив ускорений по оси Z */
    public static float[] arrAccZ;
    /** Массив модулей векторов ускорений */
    public static float[] arrAccModule;

    /** Амплитуда ускорения (усредненная) */
    public static float ampAcc;

    /** Амплитуда позиции (усредненная) */
    public static float ampPos;
    /** Частота колебаний */
    public static float rate;
}
