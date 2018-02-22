package com.akropon.tremormeter;

/**
 * Математические полезности
 */

public class MathUtils {

    /**
     * Сглаживание фукнции по трем точкам методом среднего арифметического
     *
     * @param arr - массив значений фукнции
     * @return - массив значений сглаженной функции того же размера, что и входной массив
     */
    public static float[] makeWindowSmoothing3p(float[] arr) {
        int len = arr.length;
        float[] result = new float[len];

        result[0] = arr[0];
        for (int i = 1; i < len - 1; i++)
            result[i] = (arr[i - 1] + arr[i] + arr[i + 1]) / 3;
        result[len - 1] = arr[len - 1];

        return result;
    }

    /** Среднее арифметичское массива
     *
     * @param arr - массив
     * @return - среднее арифметичское входного массива
     */
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

    /** К каждому значению массива прибавляет константу.
     *
     * Результат операции записывается либо в новый массив, либо во входной.
     * В случае, если результат операции записывается во входной массив, функция возвращает null.
     *
     * @param addictive - константа для прибавляния
     * @param arr - массив
     * @param rewrite - [true - перезапись поверх входного массива][false - запись в новый массив]
     * @return - [null, если rewrite == true][выходной массив, если rewrite == false]
     */
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

    /** Поэлементная сумма двух массивов.
     * Результат записывается в новый массив.
     * Массивы должны быть одного размера
     *
     * @param arr1 - массив1
     * @param arr2 - массив2
     * @return выходной массив того же размера, что и входные
     */
    public static float[] sumEach(float[] arr1, float[] arr2) {
        float[] result = new float[arr1.length];
        for (int i = 0; i < arr1.length; i++)
            result[i] = arr1[i] + arr2[i];
        return result;
    }


    /** Поэлементная разность двух массивов.
     * Результат записывается в новый массив.
     * Массивы должны быть одного размера
     *
     * @param arrFrom - массив, из которого вычитают
     * @param arrWhat - массив, который вычитают
     * @return выходной массив того же размера, что и входные
     */
    public static float[] subEach(float[] arrFrom, float[] arrWhat) {
        float[] result = new float[arrFrom.length];
        for (int i = 0; i < arrFrom.length; i++)
            result[i] = arrFrom[i] - arrWhat[i];
        return result;
    }

    /** Поиск локальных максимумов фукнции
     * Локальным максимумом считается точка, которая является глобальным максимум
     * в своем "окне".
     * "Окно" - локальная часть фукнции.
     * Локальный максимум единственный в своем окне.
     * Окна не пересекаются.
     * [длина окна] = 2 * [радиус окна] + 1
     * Радиус окна задается аргументом.
     * Локальный максимум находится в центре своего окна.
     * Локальный максимум >= все остальные точки окна.
     * Если рядом имеются две одинаковые точки, значения которых равны между собой и
     *   равны локальному масмому в данном окне, то первая точка станет локальным максимумом,
     *   а вторая нет.
     * Окна, которые на полностью покрыты значниями фукнции (на краях фукнции), не рассматриваются.
     *
     *
     * @param arr - массив значений фукнции
     * @param windowRad - радиус окна
     * @return - массив индексов локальных максимумов в порядке возрастания индекса.
     */
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

    /** Поиск локальных минимум фукнции по заданному окну.
     * ...
     * Описание аналогично описанию фукнции "findMaxes(float[] arr, int windowRad)"
     * с инвертированием понятия "максимум" и знака неравенства ">="
     * ...
     *
     * @param arr - массив значений фукнции
     * @param windowRad - радиус окна
     * @return - массив индексов локальных минимумов в порядке возрастания индекса.
     */
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

    /**
     * Сглаживание фукнции по окну методом среднего арифметического
     *
     * Сглаживаемая точка находится в середине окна.
     *
     * Для окон с неопределенными точками (на краях фукнции) учитываются только определенным точки
     *   (т.е. окно как бы становится в этих местах короче)
     *
     * @param arr - массив значений фукнции
     * @param windowWidth - ширина окна
     * @return - массив значений сглаженной функции того же размера, что и входной массив
     */
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

    /** Поэлементный поиск модулей вектором
     *   в 3-мерном евклидовом пространстве по трем координатам.
     *
     * Для трех массивов X, Y и Z одинаковой длины создается массив D, где:
     * D[i] = ( X[i]^2 + Y[i]^2 + Z[i]^2 ) ^ ( 1/2 )
     *
     * @param arr1 - массив X
     * @param arr2 - массив Y
     * @param arr3 - массив Z
     * @return - массив D той же длины, что и входные массивы
     */
    public static float[] module(float[] arr1, float[] arr2, float[] arr3) {
        float[] result = new float[arr1.length];
        for (int i=0; i<arr1.length; i++) {
            result[i] = (float)Math.sqrt(arr1[i]*arr1[i]+arr2[i]*arr2[i]+arr3[i]*arr3[i]);
        }
        return result;
    }

    /** Модуль векора по трем координатам в 3-мерном евклидовом пространстве
     *
     * d = ( x^2 + y^2 + z^2 ) ^ ( 1/2 )
     *
     * @param var1 - x
     * @param var2 - y
     * @param var3 - z
     * @return - d
     */
    public static float module(float var1, float var2, float var3) {
        return (float)Math.sqrt(var1*var1+var2*var2+var3*var3);
    }
}
