package com.savadanko.common.models;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.*;

/*
 * This class represents a flat.
 *
 * @author Savadanko
 * @version 1.0
 */

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Flat implements Serializable {
    /*
     * The unique ID of the flat, automatically generated.
     */
    private long id;

    /*
     * The name of the flat.
     */

    private String name;

    /*
     * The coordinates of the flat.
     */
    private Coordinates coordinates;

    /*
     * The date and time the flat was created.
     */
    private ZonedDateTime creationDate = ZonedDateTime.now();

    /*
     * The area of the flat in square meters.
     */
    private float area;

    /*
     * The number of rooms in the flat.
     */
    private long numberOfRooms;

    /*
     * The price of the flat.
     */
    private Float price;

    /*
     * The view from the flat.
     */
    private View view;

    /*
     * The transport accessibility of the flat.
     */
    private Transport transport;

    /*
     * The house the flat is located in.
     */
    private House house;

    private String owner;

    public Flat(String name, Coordinates coordinates, float area, long numberOfRooms, Float price, View view, Transport transport, House house, String owner) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.owner = owner;
    }
}
