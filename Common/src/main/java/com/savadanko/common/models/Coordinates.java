package com.savadanko.common.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/*
 * Класс, представляющий координаты.
 *
 * @author savadanko
 * @version 1.0
 */
@Setter
@Getter
@NoArgsConstructor
public class Coordinates implements Serializable {

    private long id;
    /*
     * Координата X.
     */
    private float x;

    /*
     * Координата Y.
     */
    private Long y;

    /*
     * Конструктор класса Coordinates.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Coordinates(float x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
