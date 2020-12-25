package com.simulation.lift.panel;

import com.simulation.lift.api.LiftListener;
import com.simulation.lift.model.Building;
import com.simulation.lift.model.Floor;
import com.simulation.lift.model.Lift;
import com.simulation.lift.model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class BuildingPanel extends JPanel implements LiftListener {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Color backgroundColor;
    private final Building building;

    private final LiftPanel[] liftPanels;
    private final FloorPanel[] floorPanels;

    private Random random;
    private Timer passengerTimer;
    private Timer timer;
    private int refreshCounter;
    private int timeElapsedInSecs;
    private final int passengerVolume;
    private final int maxFloor;
    private final int maxLift;
    private final int liftWeight;

    private final boolean isAuto;

    /**
     * Constructs an instance of the object containing x, y, width, height,
     * backgroundColor, maxFloor, maxLift, liftWeight and passengerVolume arguments.
     *
     * @param x                         panel's starting x value
     * @param y                         panel's starting y value
     * @param width                     panel's width
     * @param height                    panel's height
     * @param backgroundColor           panel's background colour
     * @param maxFloor                  maximum floor number
     * @param liftWeight                maximum lift capacity
     * @param passengerVolume           volume of passengers
     * @param isAuto                    is the program auto
     */
    public BuildingPanel(int x, int y, int width, int height, Color backgroundColor, int maxFloor, int maxLift, int liftWeight, int passengerVolume, boolean isAuto) {
        this.isAuto = isAuto;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height - 20;
        this.backgroundColor = backgroundColor;
        this.building = new Building( maxFloor, this.height / maxFloor, width/2, maxLift, this.height / maxFloor,width/(2*maxLift), liftWeight, this.isAuto);

        this.floorPanels = new FloorPanel[maxFloor];
        this.liftPanels = new LiftPanel[maxLift];

        this.passengerVolume = passengerVolume;
        this.refreshCounter = 0;
        this.timeElapsedInSecs = 0;
        this.maxFloor = maxFloor;
        this.maxLift = maxLift;
        this.liftWeight = liftWeight;

        this.random = new Random();

        setLayout(null);
        setBounds(x, y, width, height);
        repaint();
        init();

        if(this.isAuto) {
            this.passengerTimer = new Timer(100, (e) -> {
                this.refreshCounter += 100;
                if (this.refreshCounter / 1000 == 1) {
                    this.refreshCounter = 0;
                    this.timeElapsedInSecs++;
                    if (this.timeElapsedInSecs % 5 == 0) {
                        generateNewPassengers();
                    }
                }
            });
        }
        else {
            setPassengerFloor(passengerVolume);
        }

        this.timer = new Timer(100, (e) -> {
            repaint();
        });
    }

    /**
     * Method starts the project, by initialising the threads.
     */
    public void start() {
        if(this.isAuto) {
            //starting the timer's thread.
            Runnable passengerRunnable = () -> this.passengerTimer.start();
            Thread passengerThread = new Thread(passengerRunnable);
            passengerThread.setDaemon(true);
            passengerThread.setName("PassengerTimer");
            passengerThread.start();
        }

        //starting the cost updater thread.
        Runnable runnable = () -> this.timer.start();
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("Timer");
        thread.start();

        //starting the lifts' threads.
        for (int i = 0; i < this.liftPanels.length; i++) {
            Lift lift = this.liftPanels[i].getLift();
            runnable = () -> lift.start();
            thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName(lift.getName());
            thread.start();
        }
    }

    /**
     * Method stops the project, by terminate the the threads.
     */
    public void stop() {
        if(this.isAuto) {
            this.passengerTimer.stop();
        }
        this.timer.stop();
        for (int i = 0; i < this.liftPanels.length; i++) {
            this.liftPanels[i].getLift().stop();
        }
    }

    /**
     * Method initialise the content of the class in the construct.
     */
    private void init() {
        int x = 5;
        int y = 5;
        int w = this.width/2;
        int h = this.height / this.maxFloor;
        createFloors(x, y + 20, w, Color.ORANGE);
        createLifts(x + w, y + 20, h, Color.DARK_GRAY);
    }

    /**
     * Method draws the lift panels on top of the building panel.
     *
     * @param x                         panel's starting x value
     * @param y                         panel's starting y value
     * @param height                    panel's height
     * @param color                     panel's background colour
     */
    private void createLifts(int x, int y, int height, Color color) {
        int w1 = this.building.getLiftWidth();

       for (int i = 1; i <= this.building.getMaxLift(); i++) {
           LiftPanel liftPanel = new LiftPanel(x, y, w1-(50/this.maxFloor), height, color, this.building.getLifts()[this.building.getMaxLift()-i]);
           liftPanel.getLift().setListener(this);
           this.liftPanels[this.building.getMaxLift() - i] = liftPanel;
           add(liftPanel);
           x += w1;
       }
    }

    /**
     * Method draws the floor panels on top of the building panel.
     *
     * @param x                         panel's starting x value
     * @param y                         panel's starting y value
     * @param width                     panel's width
     * @param color                     panel's background colour
     */
    private void createFloors(int x, int y, int width, Color color) {
        int h1 = this.building.getFloorHeight();

        for (int i = 1; i <= this.building.getMaxFloor(); i++) {
            this.floorPanels[this.building.getMaxFloor() - i] = new FloorPanel(x, y, width, h1-(50/this.maxFloor), color, this.building.getFloors()[this.building.getMaxFloor()-i]);
            add(this.floorPanels[this.building.getMaxFloor()-i]);
            y += h1;
        }
    }

    /**
     * Method paints the panel of this class.
     *
     * @param g                         Graphic object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backgroundColor);

        g.setColor(Color.BLUE);
        g.drawRect(5, 2, this.width / 8, 20);
        g.drawRect(this.width / 8 + 5, 2, this.width / 4, 20);
        g.drawRect(this.width / 8 + this.width / 4 + 5, 2, this.width / 8, 20);

        g.setColor(Color.BLACK);
        g.drawString("Alight", 40, 17);
        g.drawString("Waiting Passengers", 150, 17);
        g.drawString("Board", 355, 17);

        g.setColor(Color.RED);
        g.drawRect(this.width / 2 + 10, 2, 135, 20);
        g.drawRect(this.width / 2 + 150, 2, 135, 20);

        g.setColor(Color.BLUE);
        g.drawString("Total Board: " + this.building.calculateTotalBoardPassengers(), this.width / 2 + 15, 17);
        g.drawString("Total Alight: " + this.building.calculateTotalAlightPassengers(), this.width / 2 + 155, 17);

        g.setColor(Color.BLUE);
        g.drawRect(this.width / 2 + 300, 2, 115, 20);

        g.setColor(Color.red);
        g.drawString("Total Cost: " + this.building.calculateCost(), this.width / 2 + 310, 17);
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

        LiftPanel liftPanel = findLiftByName(liftName);
        liftPanel.moveUp(moveTo);
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

        LiftPanel liftPanel = findLiftByName(liftName);
        liftPanel.moveDown(moveTo);
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

        FloorPanel floorPanel = findFloorPannel(floorNo);
        if(!isFull) {
            Passenger passenger = floorPanel.getFloor().removePassenger();
            if(passenger == null) {
                return null;
            }
            floorPanel.flashBoardFloor(passenger);
            return passenger;
        }

        LiftPanel liftPanel = findLiftByName(liftName);
        floorPanel.getFloor().addReturnPassenger(lastPassenger);
        liftPanel.getLift().setArriveFloor(floorNo);
        floorPanel.flashFloor();
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

        FloorPanel floorPanel = findFloorPannel(floorNo);
        floorPanel.flashAlightFloor(passenger);
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

        LiftPanel liftPanel = findLiftByName(liftName);
        liftPanel.openDoor();
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

        LiftPanel liftPanel = findLiftByName(liftName);
        liftPanel.closeDoor();
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

    /**
     * Method randomly allocate source and destination floors to the new passengers.
     */
    private synchronized void generateNewPassengers() {
        System.out.println("Source aaaaaaaaa");
        int numOfPassenger = random.nextInt(this.passengerVolume) + 1;

        setPassengerFloor(numOfPassenger);
    }

    /**
     * Method sets the passenger's source and destination floor.
     *
     * @param PassengerNo                   number of passengers
     */
    private void setPassengerFloor(int PassengerNo) {
        for (int i = 0; i < PassengerNo; i++) {
            int sourceFloor;
            do {
                sourceFloor = this.random.nextInt(this.maxFloor) + 1;
            } while (sourceFloor < 1 || sourceFloor > this.maxFloor);

            int liftNo;
            do {
                liftNo = this.random.nextInt(this.maxLift) + 1;
            } while (liftNo < 1 || liftNo > this.maxLift);

            int destinationFloor;
            do {
                destinationFloor = this.random.nextInt(this.maxFloor)+ 1;
            } while(destinationFloor == sourceFloor || destinationFloor < 1 || destinationFloor > this.maxFloor);

            Floor floor = this.floorPanels[sourceFloor-1].getFloor();

            if(floor.getPassengers().size() < this.passengerVolume) {

                int weight;
                do {
                    weight = this.random.nextInt(120) + 50;
                } while (weight > this.liftWeight);

                floor.addNewPassenger(new Passenger(weight, sourceFloor, destinationFloor));
                this.floorPanels[sourceFloor-1].flashFloor();

                System.out.println("Source "+sourceFloor);

                this.liftPanels[liftNo-1].getLift().setArriveFloor(sourceFloor);
            }
        }
    }

    /**
     * Method used to finds a given lift by it's name.
     *
     * @param liftName                      name of the lift
     * @return                              LiftPanel object
     */
    private LiftPanel findLiftByName(String liftName) {
        return Arrays.asList(this.liftPanels).stream().filter(l -> l.getLift().getName().equals(liftName)).findAny().get();
    }

    /**
     * Method used to find a given floor by a number.
     *
     * @param floorNo                       floor number
     * @return                              FloorPanel object
     */
    private FloorPanel findFloorPannel(int floorNo) {
        return this.floorPanels[floorNo-1];
    }

    private LiftPanel findLiftPannel(int liftNo) {
        return this.liftPanels[liftNo-1];
    }

}
