package com.savadanko.common.models;

/*
 * The means of transport available near the flat.
 *
 * @author Savadanko
 * @version 1.0
 */
public enum Transport{

    /*
     * There are very few transport options available.
     */
    FEW,

    /*
     * There are no transport options available within walking distance.
     */
    NONE,

    /*
     * There are some transport options available within walking distance.
     */
    LITTLE,

    /*
     * There are plenty of transport options available within walking distance.
     */
    NORMAL;
}
