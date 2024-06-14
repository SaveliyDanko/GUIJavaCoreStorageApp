package com.savadanko.common.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/*
 * This class represents a house.
 *
 * @author Savadanko
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
public class House implements Serializable {

    private long id;

    /*
     * The name of the house.
     */
    private String name;

    /*
     * The year the house was built.
     */
    private Long year;

    /*
     * The number of floors in the house.
     */
    private Long numberOfFloors;

    /*
     * The number of flats on each floor.
     */
    private int numberOfFlatsOnFloor;

    /*
     * The number of lifts in the house.
     */
    private int numberOfLifts;

    public House(String name, Long year, Long numberOfFloors, int numberOfFlatsOnFloor, int numberOfLifts) {
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        this.numberOfLifts = numberOfLifts;
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFloors=" + numberOfFloors +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                ", numberOfLifts=" + numberOfLifts +
                '}';
    }
}
