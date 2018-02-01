package com.akropon.tremormeter;

/**
 * Created by akropon on 31.01.18.
 */

public class PointV3 {
    public float x;
    public float y;
    public float z;

    public PointV3() {
        x = y = z = 0;
    }

    public PointV3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PointV3(float[] floatArrayV3) {
        this.x = floatArrayV3[0];
        this.y = floatArrayV3[1];
        this.z = floatArrayV3[2];
    }

    public float get(int index) {
        switch(index) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default:
                throw new IllegalArgumentException(
                        "Argument must be in [0,2], but argument = "+index);
        }
    }
}