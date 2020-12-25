package com.simulation.lift.main;

import com.simulation.lift.panel.SimulationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Hashtable;

import static javax.swing.JOptionPane.showMessageDialog;

public class Menu extends JPanel {

    /**
     * Constructs an instance of the object with no arguments.
     */
    public Menu() {
        initializeMenu();
    }

    /**
     * Method initialize label in the Menu.
     *
     * @param y       y-axis of the label
     * @param width   width of the label
     * @param message message to display in the label
     */
    private void initializeLabel(int x, int y, int width, String message) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, 20);
        add(panel);
        panel.setLayout(null);

        JLabel label = new JLabel(message);
        label.setBounds(0, 0, width, 20);
        panel.add(label);
    }

    /**
     * Method initialize user input form in the Menu.
     */
    private void initializeInput() {
        JPanel panel = new JPanel();
        panel.setBounds(120, -5, 50, 30);
        add(panel);

        JTextField floorNo = new JTextField(3);
        panel.add(floorNo);

        JPanel panel1 = new JPanel();
        panel1.setBounds(250, 33, 100, 30);
        add(panel1);

        JTextField passengerNo = new JTextField(6);
        panel1.add(passengerNo);

        JPanel panel2 = new JPanel();
        panel2.setBounds(160, 60, 190, 60);
        add(panel2);

        JSlider passengerVolume = new JSlider();
        passengerVolume.setMajorTickSpacing(50);
        passengerVolume.setPaintTicks(true);
        passengerVolume.setPaintTrack(true);
        passengerVolume.setPaintLabels(true);
        Hashtable position = new Hashtable();
        position.put(0, new JLabel("low"));
        position.put(50, new JLabel("medium"));
        position.put(100, new JLabel("high"));
        passengerVolume.setLabelTable(position);
        passengerVolume.setEnabled(false);
        panel2.add(passengerVolume);

        JPanel panel3 = new JPanel();
        panel3.setBounds(110, 120, 80, 30);
        add(panel3);

        String[] liftWeightList = {"500", "750", "1000", "1250", "1500", "1750", "2000", "2250", "2500", "2750", "3000"};
        JComboBox liftWeight = new JComboBox(liftWeightList);
        panel3.add(liftWeight);

        JPanel panel4 = new JPanel();
        panel4.setBounds(200, 155, 80, 30);
        add(panel4);

        JPanel panel5 = new JPanel();
        panel5.setBounds(30, 32, 50, 30);
        add(panel5);

        JRadioButton isAuto = new JRadioButton();
        isAuto.addActionListener(e -> {
            if(isAuto.isSelected()) {
                passengerVolume.setEnabled(true);
                passengerNo.setText("");
                passengerNo.setEditable(false);
            }
            else {
                passengerVolume.setEnabled(false);
                passengerNo.setEditable(true);
            }
        });
        panel5.add(isAuto);

        JPanel panel6 = new JPanel();
        panel6.setBounds(345, -3, 50, 30);
        add(panel6);

        JRadioButton isGraph = new JRadioButton();
        isGraph.addActionListener(e -> {
            if(isGraph.isSelected()) {
                floorNo.setEditable(false);
                isAuto.setEnabled(false);
                passengerVolume.setEnabled(false);
                passengerNo.setText("");
                passengerNo.setEditable(false);
                liftWeight.setEnabled(false);
            }
            else {
                floorNo.setEditable(true);
                isAuto.setEnabled(true);
                passengerVolume.setEnabled(true);
                passengerNo.setEditable(true);
                liftWeight.setEnabled(true);
            }
        });
        panel6.add(isGraph);


        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (isGraph.isSelected()) {
                try{
//                    String prg = "cost_dict = {}\n" +
//                            "\n" +
//                            "def get_value_by_key(obj: dict, key: str):\n" +
//                            "    try:\n" +
//                            "        return obj[key]\n" +
//                            "    except:\n" +
//                            "        return None\n" +
//                            "\n" +
//                            "def update_dict(lift_capacity, floor_number, passenger_number, cost):\n" +
//                            "\n" +
//                            "    lift_capacity_obj = get_value_by_key(cost_dict, lift_capacity)\n" +
//                            "    if not lift_capacity_obj:\n" +
//                            "        lift_capacity_obj = {lift_capacity: {}}\n" +
//                            "        cost_dict.update(lift_capacity_obj)\n" +
//                            "        lift_capacity_obj = cost_dict[lift_capacity]\n" +
//                            "\n" +
//                            "    floor_number_obj = get_value_by_key(lift_capacity_obj, floor_number)\n" +
//                            "    if not floor_number_obj:\n" +
//                            "        floor_number_obj = {floor_number: {}}\n" +
//                            "        lift_capacity_obj.update(floor_number_obj)\n" +
//                            "        floor_number_obj = lift_capacity_obj[floor_number]\n" +
//                            "\n" +
//                            "    passenger_number_obj = get_value_by_key(floor_number_obj, passenger_number)\n" +
//                            "    if not passenger_number_obj:\n" +
//                            "        passenger_number_obj = {passenger_number: {\"cost\": cost}}\n" +
//                            "        floor_number_obj.update(passenger_number_obj)\n";
//
//                    for(int weightCounter = 500; weightCounter <= 3000; weightCounter+=250) {
//                        for(int floorCounter = 5; floorCounter <= 300; floorCounter+=5) {
//                            for(int passengerCounter = 5; passengerCounter <= 1000; passengerCounter+=5) {
//                                Building building = new Building(floorCounter, 620 / floorCounter, 1, 1, 1, weightCounter, passengerCounter, false);
//                                building.start();
//                                prg = prg + "\n" +
//                                        "update_dict(" + weightCounter + ", " + floorCounter + ", " + passengerCounter + ", " + building.calculateCost() + ")\n";
//                            }
//                        }
//                    }
//
//                    prg = prg + "\nprint(cost_dict)\n";
//
//                    System.out.println(prg);
//
//                    BufferedWriter out = new BufferedWriter(new FileWriter("src/com/simulation/lift/api/CostDict.py"));
//                    out.write(prg);
//                    out.close();

                    Process p = Runtime.getRuntime().exec("python3 src/GraphPanel.py");
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String ret = in.readLine();
                    System.out.println("value is : "+ret);
                }catch(Exception a){
                    a.printStackTrace();
                }
            } else if (checkValidity(floorNo, 3, 600)) {
                if(isAuto.isSelected()) {
                    if (passengerVolume.getValue() >= 0 && passengerVolume.getValue() <= 33) {
                        startLiftSimulation(Integer.parseInt(floorNo.getText()), Integer.parseInt((String) liftWeight.getSelectedItem()), Integer.parseInt(floorNo.getText()) / 2, true);
                    }
                    if (passengerVolume.getValue() > 33 && passengerVolume.getValue() < 67) {
                        startLiftSimulation(Integer.parseInt(floorNo.getText()), Integer.parseInt((String) liftWeight.getSelectedItem()), Integer.parseInt(floorNo.getText()), true);
                    }
                    if (passengerVolume.getValue() >= 67 && passengerVolume.getValue() <= 100) {
                        startLiftSimulation(Integer.parseInt(floorNo.getText()), Integer.parseInt((String) liftWeight.getSelectedItem()), Integer.parseInt(floorNo.getText()) * 2, true);
                    }
                }
                else {
                    if(checkValidity(passengerNo, 1, Integer.MAX_VALUE)) {
                        startLiftSimulation(Integer.parseInt(floorNo.getText()), Integer.parseInt((String) liftWeight.getSelectedItem()), Integer.parseInt(passengerNo.getText()),false);
                    }
                }
            }
        });
        panel4.add(submitButton);
    }

    /**
     * Method initialize the Menu.
     */
    private void initializeMenu() {
        setLayout(null);
        setPreferredSize(new Dimension(500, 195));

        initializeLabel(5, 5, 120, "Number of Floors:");
        initializeLabel(5, 40, 35, "Auto:");
        initializeLabel(150, 40, 120, "Total Passenger:");
        initializeLabel(5, 80, 150, "Volume of Passengers:");
        initializeLabel(5, 130, 105, "Max Lift Weight:");
        initializeLabel(275, 5, 85, "Draw Graph:");

        initializeInput();
    }

    /**
     * Method validate the user input entered in the Menu.
     *
     * @param input                         user input
     * @param min                           minimum accepted value
     * @param max                           maximum accepted value
     * @return                              is the user input valid
     */
    private boolean checkValidity(JTextField input, int min, int max) {
        try {
            Integer.parseInt(input.getText());
        } catch (NumberFormatException e) {
            showMessageDialog(null, "The input must be a number.", "Error", 0);
            return false;
        }
        if (Integer.parseInt(input.getText()) < min || Integer.parseInt(input.getText()) > max) {
            showMessageDialog(null, "The input must be between " + min + " and " + max + ".", "Error", 0);
            return false;
        }
        return true;
    }

    /**
     * Method to start the simulation.
     *
     * @param maxBuildingFloor              max floor of the building
     * @param liftWeight                    lift weight capacity
     * @param passengerVolume               volume of incoming passenger at each refreshing rate
     * @param isAuto                        is the program auto
     */
    private void startLiftSimulation(int maxBuildingFloor, int liftWeight, int passengerVolume, boolean isAuto) {
        JFrame jf = new JFrame();
        SimulationPanel simulationPanel = new SimulationPanel(maxBuildingFloor, liftWeight, passengerVolume, isAuto);
        jf.add(simulationPanel);
        jf.setTitle("Lift Simulator");
        jf.pack();
        jf.setLocation(250,75);
        jf.setVisible(true);
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //stops the thread when the frame is closed.
                simulationPanel.stop();
                System.out.println("Closed");
                e.getWindow().dispose();
            }
        });
        jf.setResizable(false);
    }
}
