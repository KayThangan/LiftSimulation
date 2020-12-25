package com.simulation.lift.panel;

import com.simulation.lift.model.Floor;
import com.simulation.lift.model.Lift;
import com.simulation.lift.model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FloorPanel extends JPanel {
    /**
     * Enum presenting the states of a passenger.
     */
    private enum PP {
        WAIT,
        BOARD,
        ALIGHT
    }

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Color backgroundColor;
    private final Floor floor;

    private List<Passenger> passengers;
    private Passenger movePassenger;
    private PP pp;

    /**
     * Constructs an instance of the object containing x, y, width, height,
     * backgroundColor and floor arguments.
     *
     * @param x                         panel's starting x value
     * @param y                         panel's starting y value
     * @param width                     panel's width
     * @param height                    panel's height
     * @param backgroundColor           panel's background colour
     * @param floor                     floor number
     */
    public FloorPanel(int x, int y, int width, int height, Color backgroundColor, Floor floor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.floor = floor;
        this.passengers = new ArrayList<>();
        this.pp = PP.WAIT;

        setLayout(null);
        setBounds(x, y, width, height);
        repaint();
    }

    /**
     * Method gets the floor number.
     *
     * @return                          floor number
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * Method paints the passengers who are waiting on the floor.
     */
    public void flashFloor() {
        this.passengers = new ArrayList<>(this.floor.getPassengers());
        this.pp = PP.WAIT;
        repaint();
    }

    /**
     * Method paints the passenger who is boarding a lift.
     *
     * @param passenger                 Passenger object
     */
    public void flashBoardFloor(Passenger passenger) {
        this.movePassenger = passenger;
        int count = 3;
        while(count > 0) {
            this.pp = PP.BOARD;
            repaint();
            Lift.waitFor();
            count--;
        }
        Lift.waitFor();
        flashFloor();
    }

    /**
     * Method paints the passenger who is alighting from a lift.
     * @param passenger                 Passenger object
     */
    public void flashAlightFloor(Passenger passenger) {
        this.movePassenger = passenger;
        int count = 3;
        while(count > 0) {
            this.pp = PP.ALIGHT;
            repaint();
            Lift.waitFor();
            count--;
        }
        Lift.waitFor();
        flashFloor();
    }

    /**
     * Method paints moving passengers to/from lifts.
     *
     * @param g                         Graphics object
     * @param x                         x axis move
     */
    private void drawMovePassengers(Graphics g, int x) {
        g.fillRect(x, this.height / 2, 20, this.height / 2);
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.drawString("W: " + this.movePassenger.getWeight(), x - 2, this.height / 2 - 18);
        g.drawString("S: " + (this.movePassenger.getSourceFloor()), x - 2, this.height / 2 - 10);
        g.drawString("D: " + (this.movePassenger.getDestinationFloor()), x - 2, this.height / 2 - 20);
    }

    /**
     * Method paints the PassengerPanel objects.
     *
     * @param g                         Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backgroundColor);

        int count = 0;
        int tx =  this.width * 3 / 4 - 25;
        while (tx > (this.width / 4) && count < this.passengers.size()) {
            Passenger p = this.passengers.get(count);
            g.fillRect(tx, this.height / 2, 20, this.height /2);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.drawString("W: " + p.getWeight(), tx - 2, this.height / 2 - 18);
            g.drawString("S: " + (p.getSourceFloor()), tx - 2, this.height / 2 - 10);
            g.drawString("D: " + (p.getDestinationFloor()), tx - 2, this.height / 2 - 2);
            tx -= 30;
            count++;
        }

        if (this.pp != PP.WAIT) {
            if (this.pp == PP.BOARD) {
                tx = this.width * 3 / 4;
                while (tx < this.width) {
                    drawMovePassengers(g, tx);
                    tx += 5;
                }
            } else if (this.pp == PP.ALIGHT) {
                tx = this.width/4 - 20;
                while (tx > 0) {
                    drawMovePassengers(g, tx);
                    tx -= 5;
                }
            }
        }
    }

}
