package com.simulation.lift.model;

import java.util.Objects;

public class Passenger {
    private final int weight;
    private final int sourceFloor;
    private final int destinationFloor;

    /**
     * Constructs an instance of the object containing
     * weight, sourceFloor and destinationFloor arguments.
     *
     * @param weight                weight of a passenger
     * @param sourceFloor           source floor of the passenger
     * @param destinationFloor      destination floor of the passenger
     */
    public Passenger(int weight, int sourceFloor, int destinationFloor) {
        this.weight = weight;

        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
    }

    /**
     * Method get the weight of the passenger.
     *
     * @return                      passenger's weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Method gets the source floor of the passenger.
     *
     * @return                      passenger's source floor
     */
    public int getSourceFloor() {
        return sourceFloor;
    }

    /**
     * Method gets the destination floor of the passenger.
     *
     * @return                      passenger's destination floor
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Methods checks whether if a given passenger object exists.
     *
     * @param o                     object parameter
     * @return                      isEqual
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return weight == passenger.weight &&
                sourceFloor == passenger.sourceFloor &&
                destinationFloor == passenger.destinationFloor;
    }

    /**
     * Method returns the hash code of a non-null argument and 0 for a null argument.
     *
     * @return                      hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(weight, sourceFloor, destinationFloor);
    }

    /**
     * Method combines some of the Passenger's attributes into a String.
     *
     * @return                      passenger's attributes as String
     */
    @Override
    public String toString() {
        return "Passenger{" +
                "weight=" + weight +
                ", sourceFloor=" + sourceFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }
}