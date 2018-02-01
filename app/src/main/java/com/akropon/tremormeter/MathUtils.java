package com.akropon.tremormeter;

/**
 * Created by akropon on 31.01.18.
 */

public class MathUtils {

    /*public static float[] makeWindowSmoothing(float[] arr, int windowWidth) {

        if (arr.length < 2*windowWidth )
            throw new IllegalArgumentException("arr.length < 2*windowWidth. " +
                    "arr.length="+arr.length+" windowWidth="+windowWidth);

        int len = arr.length;
        float[] result = new float[len];
        int winWidthLeft = windowWidth / 2;
        int winWidthRight = windowWidth - winWidthLeft;

        for (int cursor=0; cursor<windowWidth; cursor++) {

        }
    }*/

    public static float[] makeWindowSmoothing3p(float[] arr) {
        int len = arr.length;
        float[] result = new float[len];

        result[0] = arr[0];
        for (int i = 1; i < len - 1; i++)
            result[i] = (arr[i - 1] + arr[i] + arr[i + 1]) / 3;
        result[len - 1] = arr[len - 1];

        return result;
    }

    public static float getAverage(float[] arr) {
        float result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i] / (float) arr.length;
        }
        /*for (int i=0; i<arr.length; i++) {
            result += arr[i];
        }
        result /= (float)arr.length;*/

        /*for (int i=0; i<arr.length; i++) {
            result += (result*i + arr[i])/(float)(i+1);
        }*/

        return result;
    }

    public static float[] addToEach(float addictive, float[] arr, boolean rewrite) {
        if (rewrite) {
            for (int i = 0; i < arr.length; i++)
                arr[i] += addictive;
            return null;
        } else {
            float[] result = new float[arr.length];
            for (int i = 0; i < arr.length; i++)
                result[i] = arr[i] + addictive;
            return result;
        }
    }

    public static float[] sumEach(float[] arr1, float[] arr2) {
        float[] result = new float[arr1.length];
        for (int i = 0; i < arr1.length; i++)
            result[i] = arr1[i] + arr2[i];
        return result;
    }

    public static float[] subEach(float[] arrFrom, float[] arrWhat) {
        float[] result = new float[arrFrom.length];
        for (int i = 0; i < arrFrom.length; i++)
            result[i] = arrFrom[i] - arrWhat[i];
        return result;
    }

    public static int[] findMaxes(float[] arr, int windowRad) {
        int[] resultContainer = new int[arr.length / (2 * windowRad - 1) + 2];
        int resultSize = 0;
        int i, j;
        boolean isOk;

        for (i = windowRad; i < arr.length - windowRad; ) {
            isOk = true;
            for (j = 1; j <= windowRad; j++) {
                if (arr[i + j] > arr[i + j - 1]) {
                    isOk = false;
                    //i += j+windowRad-1;
                    i++;
                    break;
                }
                if (arr[i - j] > arr[i - j + 1]) {
                    isOk = false;
                    //i += j+windowRad;
                    i++;
                    break;
                }
            }

            if (isOk) {
                resultContainer[resultSize] = i;
                resultSize++;
                //i += 2*windowRad;
                i++;
            }
        }


        int[] result = new int[resultSize];
        for (i = 0; i < resultSize; i++)
            result[i] = resultContainer[i];
        return result;
    }

    public static int[] findMines(float[] arr, int windowRad) {
        int[] resultContainer = new int[arr.length / (2 * windowRad - 1) + 2];
        int resultSize = 0;
        int i, j;
        boolean isOk;

        for (i = windowRad; i < arr.length - windowRad; ) {
            isOk = true;
            for (j = 1; j <= windowRad; j++) {
                if (arr[i + j] < arr[i + j - 1]) {
                    isOk = false;
                    //i += j+windowRad-1;
                    i++;
                    break;
                }
                if (arr[i - j] < arr[i - j + 1]) {
                    isOk = false;
                    //i += j+windowRad;
                    i++;
                    break;
                }
            }

            if (isOk) {
                resultContainer[resultSize] = i;
                resultSize++;
                //i += 2*windowRad;
                i++;
            }
        }


        int[] result = new int[resultSize];
        for (i = 0; i < resultSize; i++)
            result[i] = resultContainer[i];
        return result;
    }


    public static float[] getFilteredArr(float[] arr, int windowWidth) {
        float[] result = new float[arr.length];
        float windowSum = 0;
        int windowStart = 0;
        int winWidthDiv2 = windowWidth / 2;

        for (int i=0; i<windowWidth; i++)
            windowSum += arr[i];

        // здесь применяем интерполяцию от 0 до среднего по окну
        // от нулевого элемента до центрального в окне
        for (int i=0; i<=winWidthDiv2; i++) {
            result[i] = (windowSum/windowWidth)*(i/(float)winWidthDiv2);
        }

        // двигаем окно и работает с центральными элементами
        for (windowStart=1; windowStart<arr.length-windowWidth+1; windowStart++ ) {
            windowSum -= arr[windowStart-1];
            windowSum += arr[windowStart+windowWidth-1];
            result[windowStart+winWidthDiv2] = windowSum / windowWidth;
        }

        // здесь применяем интерполяцию от среднего по окну до 0
        // от (центрального+1) элемента в окне до последнего
        for (int i=0; i<(windowWidth-winWidthDiv2); i++) {
            result[arr.length-1-i] = (windowSum / windowWidth) * (i / (float) (windowWidth-winWidthDiv2));
        }

        return result;
    }

    public static float[] module(float[] arr1, float[] arr2, float[] arr3) {
        float[] result = new float[arr1.length];
        for (int i=0; i<arr1.length; i++) {
            result[i] = (float)Math.sqrt(arr1[i]*arr1[i]+arr2[i]*arr2[i]+arr3[i]*arr3[i]);
        }
        return result;
    }

    public static float module(float var1, float var2, float var3) {
        return (float)Math.sqrt(var1*var1+var2*var2+var3*var3);
    }
}
