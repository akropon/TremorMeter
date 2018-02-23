package com.akropon.tremormeter;

/**
 * Точка с тремя координатами
 */
public class PointV3 {
    /** Координата по оси X */
    public float x;
    /** Координата по оси Y */
    public float y;
    /** Координата по оси Z */
    public float z;

    /** Конструктор без аргументов
     *
     * Создает точку с нулевыми координатами
     */
    public PointV3() {
        x = y = z = 0;
    }

    /** Конструктор со значениями координат
     *
     * @param x - координата по оси X
     * @param y - координата по оси Y
     * @param z - координата по оси Z
     */
    public PointV3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Конструктор со значениями координат
     *
     * Координаты записаны в массив длины 3
     *
     * @param floatArrayV3 - массив координат длиной не менее 3
     */
    public PointV3(float[] floatArrayV3) {
        this.x = floatArrayV3[0];
        this.y = floatArrayV3[1];
        this.z = floatArrayV3[2];
    }

    /** Возвращает координату по ее порядковому номеру
     *
     * Порядковый номер ~ коодината:
     *   0 - x
     *   1 - y
     *   2 - z
     *
     * @param index - порядковый номер координаты
     * @return - соответствующая координата
     */
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