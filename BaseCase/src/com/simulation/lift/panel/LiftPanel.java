package com.simulation.lift.panel;

import com.simulation.lift.model.Lift;

import javax.swing.*;
import java.awt.*;

public class LiftPanel extends JPanel {
    private final int x;
    private int y;
    private final int width;
    private final int height;
    private int doorWidth;
    private final Color backgroundColor;
    private final Lift lift;

    private int ty;

    /**
     * Constructs an instance of the object containing x, y, width, height,
     * backgroundColor and lift arguments.
     *
     * @param x                         panel's starting x value
     * @param y                         panel's starting y value
     * @param width                     panel's width
     * @param height                    panel's height
     * @param backgroundColor           panel's background colour
     * @param lift                      Lift object
     */
    public LiftPanel(int x, int y, int width, int height, Color backgroundColor, Lift lift) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.doorWidth = width / 2;
        this.backgroundColor = backgroundColor;
        this.lift = lift;

        setLayout(null);
        setBounds(x, y, width, height * lift.getMaxMoveDistance());
        repaint();
    }

    /**
     * Method gets the Lift object from this class.
     *
     * @return                      Lift object
     */
    public Lift getLift() {
        return lift;
    }

    /**
     * Method paints the lift moving up.
     *
     * @param move                  number of moves
     */
    public void moveUp(int move) {
        if (move > 0 ) {
            int count = 0;
            int tmp = this.ty;
            while (count < this.lift.getHeight() * move) {
                repaint();
                this.ty += 5;
                count += 5;
                Lift.waitFor();
            }
            this.ty = tmp + this.lift.getHeight() * move;
        }
        repaint();
    }

    /**
     * Method paints the lift moving down.
     *
     * @param move                  number of moves
     */
    public void moveDown(int move) {
        int count = 0;
        int tmp = this.ty;
        while(count < this.lift.getHeight()*move) {
            repaint();
            Lift.waitFor();
            this.ty -= 5;
            count += 5;
        }
        this.ty = tmp - this.lift.getHeight()*move;
        repaint();
    }

    /**
     * Method paints the door of a lift opening.
     */
    public void openDoor() {
        this.doorWidth = width / 2;
        while(this.doorWidth > 0) {
            this.doorWidth-=5;
            repaint();
            Lift.waitFor();
        }
        this.doorWidth = 0;
        repaint();
    }

    /**
     * Method paints the door of a lift closing.
     */
    public void closeDoor() {
        while(this.doorWidth < width / 2) {
            this.doorWidth+=5;
            repaint();
            Lift.waitFor();
        }
        this.doorWidth = width / 2;
        repaint();
    }

    /**
     * Method gets the x-axis of the lift.
     *
     * @return                      x-axis
     */
    public int getX() {
        return x;
    }

    /**
     * Method paints the panel of a LiftPanel object.
     *
     * @param g                     Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backgroundColor);

        int tw = this.lift.getWidth() - 2;
        int ty1 = (this.lift.getMaxFloor() - this.lift.getMinFloor()) * this.lift.getHeight();
        g.setColor(Color.RED);
        g.drawRect(0, ty1 - this.ty, tw, this.lift.getHeight() - (50 / this.lift.getMaxFloor()));
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(1, ty1-this.ty, this.doorWidth - 1, this.lift.getHeight() - (50 / this.lift.getMaxFloor()));
        g.fillRect(tw - this.doorWidth + 1, ty1-this.ty, this.doorWidth - 1, this.lift.getHeight() - (50 / this.lift.getMaxFloor()));
    }
}
