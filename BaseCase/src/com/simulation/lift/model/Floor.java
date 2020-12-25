package com.simulation.lift.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Floor {
    private final int floorNo;
    private final int height;
    private final int width;
    private final LinkedList<Passenger> passengers;

    /**
     * Constructs an instance of the object containing floorNo,
     * height and width arguments.
     *
     * @param floorNo               floor number
     * @param height                height of a floor
     * @param width                 width of a floor
     */
    public Floor(int floorNo, int height, int width) {
        this.floorNo = floorNo;
        this.height = height;
        this.width = width;
        this.passengers = new LinkedList<>();
    }

    public int getFloorNo() {
        return floorNo;
    }

    /**
     * Method adding passenger to the floor, who is waiting to aboard a lift.
     *
     * @param passenger             Passenger object
     */
    public void addNewPassenger(Passenger passenger) {
        synchronized (this.passengers) {
            this.passengers.addLast(passenger);
        }
    }

    /**
     * Method returning passenger to the floor, who is waiting to aboard a lift again.
     * This because the passenger was not successful the last time.
     *
     * @param passenger             Passenger object
     */
    public void addReturnPassenger(Passenger passenger) {
        synchronized (this.passengers) {
            this.passengers.addFirst(passenger);
        }
    }

    /**
     * Method gets the list of Passengers' object from the Floor class.
     *
     * @return                      list of passengers
     */
    public List<Passenger> getPassengers() {
        return Collections.unmodifiableList(this.passengers);
    }

    /**
     * Method removes the first passenger in the queue to board the lift.
     *
     * @return                      Passenger object
     */
    public Passenger removePassenger() {
        synchronized (this.passengers) {
            if (!this.passengers.isEmpty()) {
                return this.passengers.removeFirst();
            }
        }
        return null;
    }
}