package com.simulation.lift.model;

import com.simulation.lift.api.LiftListener;

import java.util.*;
import java.util.stream.Collectors;

public class Lift {
    /**
     * Enum presenting the states of a lift.
     */
    public enum LiftMode {
        UP,
        DOWN,
        OPEN,
        CLOSE,
        WAIT,
        BOARED,
        ALIGHT,
        FULL
    }

    private int floor;

    private final int maxFloor;
    private final int minFloor;
    private int maxWeight;
    private final int height;
    private final int width;
    private final String name;

    private int totalBoardPassengers;
    private int totalAlightPassengers;
    private int totalMoves;

    private LiftMode liftMode;
    private LiftMode liftDirection;

    private final List<Passenger> passengers;
    private final static List<Integer> arriveFloors = Collections.synchronizedList(new ArrayList<>());
    private final List<Integer> departFloors;

    private LiftListener listener;

    private boolean isRunning;

    private Object object = new Object();

    private final boolean isAuto;
    private final boolean isBackEnd;

    /**
     * Constructs an instance of the object containing maxFloor, minFloor,
     * maxWeight, height, width and name arguments.
     *
     * @param maxFloor              maximum floor number
     * @param minFloor              minimum floor number
     * @param maxWeight             maximum capacity
     * @param height                height of a lift
     * @param width                 width of a lift
     * @param name                  name of the lift
     * @param isAuto                is the program auto
     * @param isBackEnd             is the program only for back-end
     */
    public Lift(int maxFloor, int minFloor, int maxWeight, int height, int width, String name, boolean isAuto, boolean isBackEnd) {
        this.floor = minFloor;

        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.maxWeight = maxWeight;
        this.height = height;
        this.width = width;

        this.name = name;

        this.isAuto = isAuto;
        this.isBackEnd = isBackEnd;

        this.liftMode = LiftMode.WAIT;
        this.liftDirection = LiftMode.WAIT;

        if(!this.isAuto) {
            this.liftMode = LiftMode.UP;
        }

        this.passengers = new ArrayList<>();
        this.departFloors = new ArrayList<>();
    }

    /**
     * Method sets a listener object to the Lift class.
     *
     * @param listener              listener object
     */
    public void setListener(LiftListener listener) {
        this.listener = listener;
    }

    public void setLiftMode(LiftMode mode) {
        this.liftMode = mode;
    }

    /**
     * Method gets the height of a lift.
     *
     * @return                      lift's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method gets the width of a lift.
     *
     * @return                      lift's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Method gets the maximum floor the lift can travel to.
     *
     * @return                      lift's maximum floor
     */
    public int getMaxFloor() {
        return maxFloor;
    }

    /**
     * Method gets the minimum floor the lift can travel to.
     *
     * @return                      lift's minimum floor
     */
    public int getMinFloor() {
        return minFloor;
    }

    /**
     * Method gets the name of the lift.
     *
     * @return                      lift's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method gets the total number of passengers, who have boarded the lift.
     *
     * @return                      total boarded passengers
     */
    public int getTotalBoardPassengers() {
        return totalBoardPassengers;
    }

    /**
     * Method gets the total number of passengers, who have alighted from the lift.
     *
     * @return                      total alighted passengers
     */
    public int getTotalAlightPassengers() {
        return totalAlightPassengers;
    }

    /**
     * Method gets the total moves completed by the lift.
     *
     * @return                      total moves
     */
    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * Method to start the lift simulation in the back-end.
     */
    public void start() {
        this.isRunning = true;
        liftOperation();
    }

    /**
     * Method to stop the lift simulation in the back-end.
     */
    public void stop() {
        this.isRunning = false;
        synchronized (object) {
            object.notifyAll();
        }
    }

    public int getFloor() {
        return floor;
    }

    /**
     * Method gets the maximum distance between the maxFloor and minFloor.
     *
     * @return                          maximum distance
     */
    public int getMaxMoveDistance() {
        return this.maxFloor - (this.minFloor - 1);
    }

    /**
     * Method boards passengers to the lift.
     *
     * @param passenger                 Passenger object
     * @return                          whether or not the passenger boarded
     */
    private boolean boardPassenger(Passenger passenger){
        if (this.maxWeight - passenger.getWeight() < 0 ) {
            return true;
        }

        if (this.passengers.add(passenger)) {
            this.maxWeight -= passenger.getWeight();
            if (!this.departFloors.contains(passenger.getDestinationFloor())) {
                this.departFloors.add(passenger.getDestinationFloor());
                this.departFloors.sort(Comparator.naturalOrder());
            }
        }
        return false;
    }

    /**
     * Method returns a list of alighting passenger from the lift.
     *
     * @return                          list of passengers
     */
    private List<Passenger> alight() {

        List<Passenger> alightPassenger = this.passengers.stream()
                .filter(p->this.floor == p.getDestinationFloor())
                .map(p -> {
                    this.maxWeight += p.getWeight();
                    return p;
                })
                .collect(Collectors.toList());

        this.passengers.removeAll(alightPassenger);

        return alightPassenger;
    }

    /**
     * Method sets the arrival floors of passengers.
     *
     * @param floorNO               floor number
     */
    public void setArriveFloor(int floorNO) {
        synchronized (arriveFloors) {
            if (!arriveFloors.contains(floorNO)) {
                arriveFloors.add(Integer.valueOf(floorNO));
                arriveFloors.sort(Comparator.naturalOrder());
            }
        }

        synchronized (object) {
            object.notifyAll();
        }
    }

    /**
     * Method controls the states of the lift.
     */
    private void liftOperation() {
        //Start Lift
        this.listener.started(this.name);

        boolean isWaitUp = false;
        boolean isWaitDown = false;
        boolean isFull = false;

        while(isRunning) {
            System.out.println("Thread "+this.name+ " LiftMode: "+this.liftMode.name());

            try {
                switch (this.liftMode) {
                    case FULL:
                        int move = getUpDownMove(liftDirection, this.floor);
                        if(move == -1){
                            liftDirection = liftDirection == LiftMode.UP ? LiftMode.DOWN : LiftMode.UP;
                        }
                        else if (liftDirection == LiftMode.UP) {
                            isFull = false;
                            this.listener.upMoves(this.name, this.floor, move);

                            this.floor += move;
                            arriveFloors.remove(Integer.valueOf(this.floor));
                            this.totalMoves += move;
                            this.liftMode = LiftMode.OPEN;
                        } else {
                            isFull = false;
                            this.listener.downMoves(this.name, this.floor, move);

                            this.floor -= move;
                            arriveFloors.remove(Integer.valueOf(this.floor));
                            this.totalMoves += move;
                            this.liftMode = LiftMode.OPEN;
                        }
                        break;

                    case WAIT:
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        this.liftMode = LiftMode.UP;
                        isWaitDown = false;
                        isWaitUp = false;
                        break;

                    case UP:
                        if (this.floor == this.maxFloor) {
                            this.liftMode = LiftMode.DOWN;
                            isWaitUp = true;
                        } else {
                            move = getUpMove(this.floor);
                            if (move == -1) {
                                if(passengers.isEmpty() && totalBoardPassengers == totalAlightPassengers && (arriveFloors.isEmpty() && departFloors.isEmpty())) {
                                    if(!this.isAuto) {
                                        stop();
                                        break;
                                    }
                                    this.liftMode = LiftMode.WAIT;
                                }
                                else {
                                    move = maxFloor - floor;
                                    this.listener.upMoves(this.name, this.floor, move);
                                    this.floor += move;
                                    this.totalMoves += move;

                                    this.liftMode = LiftMode.DOWN;
                                    isWaitUp = true;
                                }

                            } else {
                                this.listener.upMoves(this.name, this.floor, move);

                                this.floor += move;
                                this.totalMoves += move;
                                liftDirection = LiftMode.UP;
                                this.liftMode = LiftMode.OPEN;
                            }
                        }

                        break;

                    case DOWN:
                        if (this.floor == this.minFloor) {
                            this.liftMode = LiftMode.UP;
                            isWaitDown = true;
                        } else {
                            move = getDownMove(this.floor);
                            if (move == -1) {
                                if(passengers.isEmpty() && totalBoardPassengers == totalAlightPassengers && (arriveFloors.isEmpty() && departFloors.isEmpty())) {
                                    if(!this.isAuto) {
                                        stop();
                                        break;
                                    }
                                    this.liftMode = LiftMode.WAIT;
                                }
                                else {
                                    move = floor - minFloor;
                                    this.listener.downMoves(this.name, this.floor, move);
                                    this.floor -= move;
                                    this.totalMoves += move;

                                    this.liftMode = LiftMode.UP;
                                    isWaitDown = true;
                                }

                            } else {
                                this.listener.downMoves(this.name, this.floor, move);

                                this.floor -= move;
                                this.totalMoves += move;
                                liftDirection = LiftMode.DOWN;
                                this.liftMode = LiftMode.OPEN;
                            }
                        }

                        break;

                    case OPEN:
                        isWaitDown = false;
                        isWaitUp = false;

                        this.listener.openDoor(this.name, this.floor);

                        this.liftMode = LiftMode.ALIGHT;
                        break;

                    case ALIGHT:
                        //Get off passenger
                        for(Passenger passenger : alight()) {
                            this.totalAlightPassengers++;
                            this.listener.alightPassenger(this.name, this.floor, passenger);
                            if(!isBackEnd) {
                                waitFor();
                            }
                        }

                        departFloors.remove(Integer.valueOf(this.floor));
                        this.liftMode = LiftMode.BOARED;
                        break;

                    case BOARED:
                        //Get in passenger
                        Passenger ps = this.listener.getBoardPassenger(this.name, this.floor, null, false);
                        while(ps != null) {
                            isFull = boardPassenger(ps);
                            ps = this.listener.getBoardPassenger(this.name, this.floor, ps, isFull);
                            this.totalBoardPassengers++;
                            if(isFull) {
                                this.totalBoardPassengers--;
                            }
                        }
                        this.liftMode = LiftMode.CLOSE;
                        break;

                    case CLOSE:
                        this.listener.closeDoor(this.name, this.floor);

                        if (isFull) {
                            this.liftMode = LiftMode.FULL;
                        } else {
                            this.liftMode = liftDirection;
                        }

                        isWaitDown = false;
                        isWaitUp = false;
                }
            } catch(Throwable t) {
                t.printStackTrace();
            }

            if (isWaitUp && isWaitDown) {
                this.liftMode = LiftMode.WAIT;
            }
        }

        //Stop Lift
        this.listener.stopped(this.name);
    }

    /**
     * Method used to delay the simulation to be viewable by humans.
     */
    public static void waitFor() {
        try {
            Thread.sleep(15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method gets the number of moves lift have to perform for passengers to alight.
     * This is because the lift is full, so it needs to empty lift.
     *
     * @param mode                      lift's state
     * @param floorNo                   current floor
     * @return                          moves
     */
    private int getUpDownMove(LiftMode mode, int floorNo) {

        if (mode == LiftMode.UP) {
            for (int i = 0; i < this.departFloors.size(); i++) {
                int dep = this.departFloors.get(i).intValue();
                if (dep > floorNo) {
                    return  dep - floorNo;
                }
            }
        } else {
            for (int i = 1; i <= this.departFloors.size(); i++) {
                int dep = this.departFloors.get(this.departFloors.size() -i).intValue();
                if (dep < floorNo) {
                    return floorNo - dep;
                }
            }
        }
        return -1;
    }

    /**
     * Method gets the number of up moves lift have to perform.
     *
     * @param floorNo                   current floor
     * @return                          moves
     */
    private int getUpMove(int floorNo) {
        int arr = 0;
        for (int i = 0; i < arriveFloors.size(); i++) {
            if (arriveFloors.get(i) >= floorNo) {
                arr = arriveFloors.get(i);
                break;
            }
        }

        int dep = 0;
        for (int i = 0; i < this.departFloors.size(); i++) {
            if (this.departFloors.get(i) >= floorNo) {
                dep = this.departFloors.get(i);
                break;
            }
        }

        if (arr == 0 && dep == 0) {
            return -1;
        } else if (arr == 0) {
            return dep - floorNo;
        } else if (dep == 0) {
            arriveFloors.remove(Integer.valueOf(arr));
            return arr - floorNo;
        } else if (dep >= arr) {
            arriveFloors.remove(Integer.valueOf(arr));
            return arr - floorNo;
        } else {
            return dep - floorNo;
        }
    }

    /**
     * Method gets the number of down moves lift have to perform.
     *
     * @param floorNo                   current floor
     * @return                          moves
     */
    private int getDownMove(int floorNo) {
        int arr = 0;
        for (int i = arriveFloors.size(); i > 0; i--) {
            if (arriveFloors.get(i-1) <= floorNo) {
                arr = arriveFloors.get(i-1);
                break;
            }
        }

        int dep = 0;
        for (int i = this.departFloors.size(); i > 0; i--) {
            if (this.departFloors.get(i-1) <= floorNo) {
                dep = this.departFloors.get(i-1);
                break;
            }
        }

        if (arr == 0 && dep == 0) {
            return -1;
        } else if (arr == 0) {
            return floorNo - dep;
        } else if (dep == 0) {
            arriveFloors.remove(Integer.valueOf(arr));
            return floorNo - arr;
        } else if (dep >= arr) {
            arriveFloors.remove(Integer.valueOf(arr));
            return floorNo - arr;
        } else {
            return floorNo - dep;
        }
    }
}