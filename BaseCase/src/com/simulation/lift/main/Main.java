package com.simulation.lift.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new Menu());
        jf.setTitle("Main Menu");
        jf.pack();
        jf.setLocation(400,100);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
    }
}
