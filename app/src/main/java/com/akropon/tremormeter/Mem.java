package com.akropon.tremormeter;

import java.util.ArrayList;

/**
 * Memory Module
 *
 * Created by akropon on 31.01.18.
 */

public class Mem {
    public static int mode = 0;
    public static ArrayList<PointV3> accMeasuredArray = new ArrayList<>();

    public static int measureTimeInit;

    public static float[] arrTime;
    public static float[] arrAccX;
    public static float[] arrAccY;
    public static float[] arrAccZ;
    public static float[] arrAccModule;

    public static float ampAcc;

    public static float ampPos;
    public static float rate;
}
