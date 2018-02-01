package com.akropon.tremormeter;

/**
 * Created by akropon on 31.01.18.
 */

public class TimePointV3 extends PointV3 {

    float t;

    public TimePointV3() {
        super();
        t = 0;
    }

    public TimePointV3(float t, float x, float y, float z) {
        super(x,y,z);
        this.t = t;
    }

    public TimePointV3(float[] vectorV4) {
        super(vectorV4[1], vectorV4[2], vectorV4[3]);
        t = vectorV4[0];
    }

    public float get(int index) {
        switch(index) {
            case 0: return t;
            case 1: return x;
            case 2: return y;
            case 3: return z;
            default:
                throw new IllegalArgumentException(
                        "Argument must be in [0,3], but argument = "+index);
        }
    }


}
