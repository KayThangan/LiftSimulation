package com.simulation.lift.api;

import com.simulation.lift.model.Passenger;

public interface LiftListener {
    /**
     * Method moves the lift up depending on certain number of moves.
     *
     * @param liftName                      name of the lift
     * @param fromFloorNo                   source floor
     * @param moveTo                        destination floor
     */
    void upMoves(String liftName, int fromFloorNo, int moveTo);

    /**
     * Method moves the lift down depending on certain number of moves.
     *
     * @param liftName                      name of the lift
     * @param fromFloorNo                   source floor
     * @param moveTo                        destination floor
     */
    void downMoves(String liftName, int fromFloorNo, int moveTo);

    /**
     * Method gets the passenger who are boarding a lift.
     *
     * @param liftName                      name of the lift
     * @param floorNo                       floor number
     * @param lastPassenger                 previous passenger
     * @param isFull                        is the lift full
     * @return                              Passenger object
     */
    Passenger getBoardPassenger(String liftName, int floorNo, Passenger lastPassenger, boolean isFull);

    /**
     * Method removes the passengers who are alighting from a lift.
     *
     * @param liftName                      name of the life
     * @param floorNo                       floor number
     * @param passenger                     Passenger object
     */
    void alightPassenger(String liftName, int floorNo, Passenger passenger);

    /**
     * Method opens the door of a lift.
     *
     * @param liftName                      lift name
     * @param floorNo                       floor number
     */
    void openDoor(String liftName, int floorNo);

    /**
     * Method closes the door of a lift.
     *
     * @param liftName                      lift name
     * @param floorNo                       floor number
     */
    void closeDoor(String liftName, int floorNo);

    /**
     * Method starts the lift.
     *
     * @param liftName                      lift name
     */
    void started(String liftName);

    /**
     * Method stops the lift.
     *
     * @param liftName                      lift name
     */
    void stopped(String liftName);
}
