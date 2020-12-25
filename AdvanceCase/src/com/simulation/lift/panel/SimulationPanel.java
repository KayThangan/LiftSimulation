package com.simulation.lift.panel;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
    private BuildingPanel building;

    /**
     * Constructs an instance of the object containing maxBuildingFloor,
     * liftWeight, passengerVolume and liftNo arguments.
     *
     * @param maxBuildingFloor              max floor of the building
     * @param liftWeight                    lift weight capacity
     * @param passengerVolume               volume of incoming passenger at each refreshing rate
     * @param isAuto                        is the program auto
     */
    public SimulationPanel(int maxBuildingFloor, int liftWeight, int passengerVolume, int liftNo, Boolean isAuto) {
        setLayout(null);
        setPreferredSize(new Dimension(855, 630));

        this.building = new BuildingPanel(0, 0,850, 620, Color.WHITE, maxBuildingFloor, liftNo, liftWeight, passengerVolume, isAuto);
        //starting all the treads to run in the program.
        this.building.start();

        add(building);
    }

    /**
     * Method stops all the treads in the BuildPanel class the stop the program.
     */
    public void stop() {
        building.stop();
    }
}