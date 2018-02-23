package com.akropon.tremormeter;

/**
 * Точка с координатами и значением времени
 * Наследует класс PointV3
 *
 * Created by akropon on 31.01.18.
 */
public class TimePointV3 extends PointV3 {

    /** Координата по оси времени */
    float t;

    /** Конструктор без аргументов
     *  Создает точку со всеми нулевыми координатами
     */
    public TimePointV3() {
        super();
        t = 0;
    }

    /** Конструктор с заданными координатами
     *
     * @param t - координата времени
     * @param x - координата по оси X
     * @param y - координата по оси Y
     * @param z - координата по оси Z
     */
    public TimePointV3(float t, float x, float y, float z) {
        super(x,y,z);
        this.t = t;
    }

    /** Контруктор с координатами
     *
     * Координаты задаются массивом со следующим порядком:
     * (time, x, y, z)
     *
     * @param vectorV4 - массив координат длиной не менее 4
     */
    public TimePointV3(float[] vectorV4) {
        super(vectorV4[1], vectorV4[2], vectorV4[3]);
        t = vectorV4[0];
    }

    /** Возвращает координату по ее порядковому номеру
     *
     * Порядковый номер ~ коодината:
     *   0 - t
     *   1 - x
     *   2 - y
     *   3-  z
     *
     * @param index - порядковый номер координаты
     * @return - соответствующая координата
     */
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
