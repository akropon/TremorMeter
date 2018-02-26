package com.akropon.tremormeter;

/**
 * Классб описывающий глобальные константы приложения
 *
 * Created by akropon on 31.01.18.
 */

public class Cnst {

    /** Время задержки перед началом замера в секундах */
    public static int delayTimeInit = 2;


    /** Код возврата результата для StartActivity */
    public static final int RC_AS = 1;  // Request Code Activity Start
    /** Код возврата результата для MeasureActivity */
    public static final int RC_AM = 2;  // Request Code Activity Measure
    /** Код возврата результата для ResultActivity */
    public static final int RC_AR = 3;  // Request Code Activity Result
    /** Код возврата результата для SensorNotFoundActivity */
    public static final int RC_ASNF = 4;  // Request Code Activity Sensor Not Found


    public static final int RESULT_NEED_TO_EXIT = 2;
    public static final int RESULT_GO_TO_START_ACTIVITY = 3;

}
