package com.simulation.lift.model;

import com.simulation.lift.api.LiftListener;

import java.util.Arrays;
import java.util.Random;

public class Building implements LiftListener {

    private final int maxFloor;
    private final int liftWeight;
    private final int liftHeight;
    private final int liftWidth;
    private final int floorHeight;
    private final int floorWidth;

    private final boolean isAuto;
    private final boolean isBackEnd;

    private Lift lift;
    private Floor[] floors;

    /**
     * Constructs an instance of the object containing maxFloor, floorHeight,
     * floorWidth, maxLift, liftHeight, liftWidth, liftWeight and isAuto arguments.
     *
     * @param maxFloor          maximum floor number
     * @param floorHeight       height of a floor
     * @param floorWidth        width of a floor
     * @param liftHeight        height of a lift
     * @param liftWidth         width of a lift
     * @param liftWeight        maximum lift capacity
     * @param isAuto            is the program auto
     */
    public Building(int maxFloor, int floorHeight, int floorWidth, int liftHeight, int liftWidth, int liftWeight, boolean isAuto) {
        this.maxFloor = maxFloor;
        this.liftWeight = liftWeight;
        this.floors = new Floor[maxFloor];
        this.liftHeight = liftHeight;
        this.liftWidth = liftWidth;
        this.floorHeight = floorHeight;
        this.floorWidth = floorWidth;

        this.isAuto = isAuto;
        this.isBackEnd = false;

        init();
    }

    /**
     * Constructs an instance of the object containing maxFloor, floorHeight,
     * floorWidth, maxLift, liftHeight, liftWidth, liftWeight, passengerNo and isAuto arguments.
     *
     * @param maxFloor          maximum floor number
     * @param floorHeight       height of a floor
     * @param floorWidth        width of a floor
     * @param liftHeight        height of a lift
     * @param liftWidth         width of a lift
     * @param liftWeight        maximum lift capacity
     * @param passengerNo       number of passenger
     * @param isAuto            is the program auto
     */
    public Building(int maxFloor, int floorHeight, int floorWidth, int liftHeight, int liftWidth, int liftWeight, int passengerNo, boolean isAuto) {
        this.maxFloor = maxFloor;
        this.liftWeight = liftWeight;
        this.floors = new Floor[maxFloor];
        this.liftHeight = liftHeight;
        this.liftWidth = liftWidth;
        this.floorHeight = floorHeight;
        this.floorWidth = floorWidth;

        this.isAuto = isAuto;
        this.isBackEnd = true;

        init();

        setPassengerFloor(passengerNo);
    }

    /**
     * Method starts the project, by initialising the threads.
     */
    public void start() {
        //starting the lifts' threads.
        this.lift.setListener(this);
        this.lift.setLiftMode(Lift.LiftMode.UP);
        this.lift.start();
    }

    /**
     * Method gets the list of Lifts' object from the Building class.
     *
     * @return                  list of lifts
     */
    public Lift getLift() {
        return lift;
    }

    /**
     * Method gets the list of Floors' object from the Building class.
     *
     * @return                  list of floors
     */
    public Floor[] getFloors() {
        return floors;
    }

    /**
     * Method gets the building's maximum number floors.
     *
     * @return                  maximum floor number
     */
    public int getMaxFloor() {
        return maxFloor;
    }

    /**
     * Method gets the building's floor height.
     *
     * @return                  height of a floor
     */
    public int getFloorHeight() {
        return floorHeight;
    }

    /**
     * Method gets the building's lift width.
     *
     * @return                  width of a lift
     */
    public int getLiftWidth() {
        return liftWidth;
    }

    /**
     * Method calculate the total passengers who have boarded the lifts.
     *
     * @return                          total boarded passengers
     */
    public int calculateTotalBoardPassengers() {
        return this.lift.getTotalBoardPassengers();
    }

    /**
     * Method calculate the total passengers who have alighted the lifts.
     *
     * @return                          total alighted passengers
     */
    public int calculateTotalAlightPassengers() {
        return this.lift.getTotalAlightPassengers();
    }

    /**
     * Method calculate the total moves performed by the lifts.
     *
     * @return                          total lift's moves
     */
    private int calculateTotalLiftMoves() {
        return this.lift.getTotalMoves();
    }

    /**
     * Method calculate the total cost of the program.
     *
     * @return                          total cost
     */
    public int calculateCost() {
        if(calculateTotalAlightPassengers() == 0) {
            return 0;
        }
        return calculateTotalLiftMoves() / calculateTotalAlightPassengers();
    }

    /**
     * Method sets the passenger's source and destination floor.
     *
     * @param PassengerNo                   number of passengers
     */
    private void setPassengerFloor(int PassengerNo) {
        Random random = new Random();
        for (int i = 0; i < PassengerNo; i++) {
            int sourceFloor;
            do {
                sourceFloor = random.nextInt(this.maxFloor) + 1;
            } while (sourceFloor < 1 || sourceFloor > this.maxFloor);

            int destinationFloor;
            do {
                destinationFloor = random.nextInt(this.maxFloor)+ 1;
            } while(destinationFloor == sourceFloor || destinationFloor < 1 || destinationFloor > this.maxFloor);

            if(this.floors[sourceFloor - 1].getPassengers().size() < PassengerNo) {

                int weight;
                do {
                    weight = random.nextInt(120) + 50;
                } while (weight > this.liftWeight);

                this.floors[sourceFloor - 1].addNewPassenger(new Passenger(weight, sourceFloor, destinationFloor));

                System.out.println("Source "+sourceFloor);

                this.lift.setArriveFloor(sourceFloor);
            }
        }
    }

    /**
     * Method initialise the content of the class in the construct.
     */
    private void init() {
        for (int i = 0; i < this.maxFloor; i++) {
            this.floors[i] = new Floor(i+1, this.floorHeight, this.floorWidth);
        }
        this.lift = new Lift(this.maxFloor, 1, this.liftWeight, this.liftHeight, this.liftWidth, "Lift-"+(1), this.isAuto, this.isBackEnd);
    }

    /**
     * Method moves the lift up by certain floors.
     *
     * @param liftName                      name of the lift
     * @param fromFloorNo                   source floor
     * @param moveTo                        destination floor
     */
    @Override
    public void upMoves(String liftName, int fromFloorNo, int moveTo) {
        String s = String.format(" liftName: %s, fromFloorNo: %s, moveTo: %s", liftName, fromFloorNo, moveTo);
        System.out.println("********* : upMoves : "+s);
    }

    /**
     * Method moves the lift down by certain floors.
     *
     * @param liftName                      name of the lift
     * @param fromFloorNo                   source floor
     * @param moveTo                        destination floor
     */
    @Override
    public void downMoves(String liftName, int fromFloorNo, int moveTo) {
        String s = String.format(" liftName: $s, fromFloorNo: %s, moveTo: %s", liftName, fromFloorNo, moveTo);
        System.out.println("********* : downMoves : "+ s);
    }

    /**
     * Method adds the passengers who are boarding a lift.
     *
     * @param liftName                      name of the lift
     * @param floorNo                       floor number
     * @param lastPassenger                 previous passenger
     * @param isFull                        is the lift full
     * @return                              passenger who could not board
     */
    @Override
    public Passenger getBoardPassenger(String liftName, int floorNo, Passenger lastPassenger, boolean isFull) {
        String s = String.format(" liftName: %s, floorNo: %s, lastPassenger: %s", liftName, floorNo, lastPassenger != null ? lastPassenger.toString(): null);
        System.out.println("********* : getBoardPassenger : "+s);
        System.out.println("********* : getBoardPassenger : isFull: "+isFull);

        Floor floor = findFloor(floorNo);
        if(!isFull) {
            Passenger passenger = floor.removePassenger();
            return passenger;
        }

        Lift lift = findLiftByName(liftName);
        floor.addReturnPassenger(lastPassenger);
        lift.setArriveFloor(floorNo);
        return null;
    }

    /**
     * Method removes the passenger who are alighting from a lift.
     *
     * @param liftName                      name of the lift
     * @param floorNo                       floor number
     * @param passenger                     alighting passenger
     */
    @Override
    public void alightPassenger(String liftName, int floorNo, Passenger passenger) {
        String s = String.format(" liftName: %s, floorNo: %s, lastPassenger: %s", liftName, floorNo, passenger != null ? passenger.toString() : null);
        System.out.println("********* : alightPassenger : "+s);
    }

    /**
     * Method to open the lift's door.
     *
     * @param liftName                      name of the lift
     * @param floorNo                       floor number
     */
    @Override
    public void openDoor(String liftName, int floorNo) {
        String s = String.format(" liftName: %s, floorNo: %s", liftName, floorNo);
        System.out.println("********* : openDoor :"+s);
    }

    /**
     * Method to close the lift's door.
     *
     * @param liftName                      name of the lift
     * @param floorNo                       floor number
     */
    @Override
    public void closeDoor(String liftName, int floorNo) {
        String s = String.format(" liftName: %s, floorNo: %s", liftName, floorNo);
        System.out.println("********* : closeDoor :"+s);
    }

    /**
     * Method to display the lift's thread has initialised in the console.
     *
     * @param liftName                      name of the lift
     */
    @Override
    public void started(String liftName) {
        System.out.println("********* : started : "+liftName);
    }

    /**
     * Method to display the lift's tread has terminated in the console.
     *
     * @param liftName                      name of the lift
     */
    @Override
    public void stopped(String liftName) {
        System.out.println("********* : stopped : "+liftName);
    }


    private Lift findLiftByName(String liftName) {
        return Arrays.asList(this.lift).stream().filter(l -> l.getName().equals(liftName)).findAny().get();
    }

    private Floor findFloor(int floorNo) {
        for(Floor floor : this.floors){
            if(floor.getFloorNo() == floorNo){
                return floor;
            }
        }
        return null;
    }
}