package gui;

import util.World;

import javax.swing.*;
import java.awt.*;

public class Setup extends JFrame {
    private JPanel mainPanel;
    private JButton submitButton;
    private JRadioButton depthFirstSearchRadioButton;
    private JRadioButton breadthFirstSearchRadioButton;
    private JRadioButton dijkstraShortestPathRadioButton;
    private JSpinner airplanesNumberSpinner;
    private JSpinner minFuelSpinner;
    private JSpinner maxFuelSpinner;
    private JCheckBox zielonaGoraCheckBox;
    private JCheckBox gorzowWielokopolskiCheckBox;
    private JCheckBox szczecinCheckBox;
    private JCheckBox gdanskCheckBox;
    private JCheckBox olsztynCheckBox;
    private JCheckBox bialystokCheckBox;
    private JCheckBox warszawaCheckBox;
    private JCheckBox bydgoszczCheckBox;
    private JCheckBox torunCheckBox;
    private JCheckBox poznanCheckBox;
    private JCheckBox lodzCheckBox;
    private JCheckBox wroclawCheckBox;
    private JCheckBox opoleCheckBox;
    private JCheckBox katowiceCheckBox;
    private JCheckBox krakowCheckBox;
    private JCheckBox rzeszowCheckBox;
    private JCheckBox kielceCheckBox;
    private JCheckBox lublinCheckBox;
    private JCheckBox selectAllAirportsCheckBox;
    private World world;

    /**
     * Creates main JFrame of an app
     */
    public Setup() {
        //Setting up JFrame//
        initializeJFrame();
        //Centering window on screen//
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);
        //Setting up JFrame components//
        //Setting up minFuelSpinner and maxFuelSpinner//
        SpinnerModel value = new SpinnerNumberModel(1,1,99,1);
        airplanesNumberSpinner.setModel(value);
        SpinnerModel value2 = new SpinnerNumberModel(200,1,999,1);
        minFuelSpinner.setModel(value2);
        SpinnerModel value3 = new SpinnerNumberModel(200,1,999,1);
        maxFuelSpinner.setModel(value3);
        //Adding ChangeListeners to Spinners//
        minFuelSpinner.addChangeListener(e -> {
            if((Integer) maxFuelSpinner.getValue() < (Integer) minFuelSpinner.getValue()){
                maxFuelSpinner.setValue(minFuelSpinner.getValue());
            }
        });
        maxFuelSpinner.addChangeListener(e -> {
            if((Integer) maxFuelSpinner.getValue() < (Integer) minFuelSpinner.getValue()){
                minFuelSpinner.setValue(maxFuelSpinner.getValue());
            }
        });
        //Adding ActionListener to submitButton//
        submitButton.addActionListener(e -> {
            //Determining algorithm name//
            String algorithmName = "DIJKSTRA";
            if(depthFirstSearchRadioButton.isSelected()){
                algorithmName = "DFS";
            }
            else if(breadthFirstSearchRadioButton.isSelected()){
                algorithmName = "BFS";
            }
            else if(dijkstraShortestPathRadioButton.isSelected()){
                algorithmName = "DIJKSTRA";
            }
            //Displaying provided values//
            displayValues((Integer) airplanesNumberSpinner.getValue(), (Integer) minFuelSpinner.getValue(),
                          (Integer) maxFuelSpinner.getValue(), algorithmName);
            //Creating new world//
            world = new World();
            //Adding back button//
            world.getAirplanesGUI().addBackButton(backButton());
            //Changing JFrame settings//
            setContentPane(AirplanesGUI.getJLayeredPane());
            setSize(AirplanesGUI.getWorldImageIcon().getIconWidth(), AirplanesGUI.getWorldImageIcon().getIconHeight());
            setLocation(this.getX(), Math.max(this.getY() - (this.getHeight()/4) + 25, 0));
            //Adding airports and airplanes
            addAirports();
            world.addAirplanes((Integer) airplanesNumberSpinner.getValue(), (Integer) minFuelSpinner.getValue(),
                               (Integer) maxFuelSpinner.getValue(), algorithmName);
        });
        //Adding ActionListener to selectAllAirportsCheckBox//
        selectAllAirportsCheckBox.addActionListener(e -> {
            if(selectAllAirportsCheckBox.isSelected()){
                zielonaGoraCheckBox.setSelected(true);
                gorzowWielokopolskiCheckBox.setSelected(true);
                szczecinCheckBox.setSelected(true);
                gdanskCheckBox.setSelected(true);
                olsztynCheckBox.setSelected(true);
                bialystokCheckBox.setSelected(true);
                warszawaCheckBox.setSelected(true);
                bydgoszczCheckBox.setSelected(true);
                torunCheckBox.setSelected(true);
                poznanCheckBox.setSelected(true);
                lodzCheckBox.setSelected(true);
                wroclawCheckBox.setSelected(true);
                opoleCheckBox.setSelected(true);
                katowiceCheckBox.setSelected(true);
                krakowCheckBox.setSelected(true);
                rzeszowCheckBox.setSelected(true);
                kielceCheckBox.setSelected(true);
                lublinCheckBox.setSelected(true);
            } else {
                zielonaGoraCheckBox.setSelected(false);
                gorzowWielokopolskiCheckBox.setSelected(false);
                szczecinCheckBox.setSelected(false);
                gdanskCheckBox.setSelected(false);
                olsztynCheckBox.setSelected(false);
                bialystokCheckBox.setSelected(false);
                warszawaCheckBox.setSelected(false);
                bydgoszczCheckBox.setSelected(false);
                torunCheckBox.setSelected(false);
                poznanCheckBox.setSelected(false);
                lodzCheckBox.setSelected(false);
                wroclawCheckBox.setSelected(false);
                opoleCheckBox.setSelected(false);
                katowiceCheckBox.setSelected(false);
                krakowCheckBox.setSelected(false);
                rzeszowCheckBox.setSelected(false);
                kielceCheckBox.setSelected(false);
                lublinCheckBox.setSelected(false);
            }
        });
    }

    /**
     * Creates JFrame with world map, skipping the setting page
     * @param numberOfAirplanes Number of airplanes to create.
     * @param minFuel Minimal number of fuel which airplane can have.
     * @param maxFuel Maximum number of fuel which airplane can have.
     * @param algorithmName name of algorithm which will be used by airplane to find path.
     *                      Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public Setup(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName){
        //Displaying provided values//
        displayValues(numberOfAirplanes, minFuel, maxFuel, algorithmName);
        //Setting up JFrame//
        initializeJFrame();
        setSize(AirplanesGUI.getWorldImageIcon().getIconWidth(), AirplanesGUI.getWorldImageIcon().getIconHeight());
        //Centering window on screen//
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);
        //Displaying world map//
        world = new World();
        setContentPane(AirplanesGUI.getJLayeredPane());
        //Adding airports and airplanes//
        addAirports();
        world.addAirplanes(numberOfAirplanes, minFuel, maxFuel, algorithmName);
    }

    /**
     * Configures main JFrame
     */
    private void initializeJFrame(){
        setTitle("Airplanes");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(AirplanesGUI.getAirplaneImageIcon().getImage());
        setSize(mainPanel.getPreferredSize());
        setContentPane(mainPanel);
        setVisible(true);
    }

    /**
     * Displays provided values
     * @param numberOfAirplanes Number of airplanes which will be created.
     * @param minFuel Minimal number of fuel which airplane can have.
     * @param maxFuel Maximum number of fuel which airplane can have.
     * @param algorithmName name of algorithm which will be used by airplane to find path.
     *                      Available names: "BFS", "DFS", "DIJKSTRA".
     */
    private void displayValues(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName){
        System.out.println();
        System.out.println("Number of airplanes: " + numberOfAirplanes);
        System.out.println("Minimum fuel       : " + minFuel);
        System.out.println("Maximum fuel       : " + maxFuel);
        System.out.println("Name of algorithm  : " + algorithmName.toUpperCase());
        System.out.println();
    }

    /**
     * Adds selected airports to the world
     */
    private void addAirports(){
        if(zielonaGoraCheckBox.isSelected()){
            world.addAirport(101, 319, "Zielona Góra");
        }
        if(gorzowWielokopolskiCheckBox.isSelected()){
            world.addAirport(81, 238, "Gorzów Wielokopolski");
        }
        if(szczecinCheckBox.isSelected()){
            world.addAirport(44, 163, "Szczecin");
        }
        if(gdanskCheckBox.isSelected()){
            world.addAirport(301, 71, "Gdańsk");
        }
        if(olsztynCheckBox.isSelected()){
            world.addAirport(413, 138, "Olsztyn");
        }
        if(bialystokCheckBox.isSelected()){
            world.addAirport(578, 203, "Białystok");
        }
        if(warszawaCheckBox.isSelected()){
            world.addAirport(443, 286, "Warszawa");
        }
        if(bydgoszczCheckBox.isSelected()){
            world.addAirport(253, 196, "Bydgoszcz");
        }
        if(torunCheckBox.isSelected()){
            world.addAirport(299, 209, "Toruń");
        }
        if(poznanCheckBox.isSelected()){
            world.addAirport(189, 272, "Poznań");
        }
        if(lodzCheckBox.isSelected()){
            world.addAirport(348, 346, "Łódź");
        }
        if(wroclawCheckBox.isSelected()){
            world.addAirport(200, 399, "Wrocław");
        }
        if(opoleCheckBox.isSelected()){
            world.addAirport(252, 455, "Opole");
        }
        if(katowiceCheckBox.isSelected()){
            world.addAirport(319, 498, "Katowice");
        }
        if(krakowCheckBox.isSelected()){
            world.addAirport(376, 501, "Kraków");
        }
        if(rzeszowCheckBox.isSelected()){
            world.addAirport(516, 517, "Rzeszów");
        }
        if(kielceCheckBox.isSelected()){
            world.addAirport(422, 385, "Kielce");
        }
        if(lublinCheckBox.isSelected()){
            world.addAirport(545, 393, "Lublin");
        }
    }

    /**
     * @return backButton that allows to back to setting (changes ContentPane of JFrame to mainPanel)
     */
    private JButton backButton(){
        JButton jButton = new JButton();
        jButton.setText("Back");
        jButton.setVisible(true);
        jButton.setSize(70,20);
        jButton.addActionListener(e -> {
            getContentPane().removeAll();
            setContentPane(mainPanel);
            setSize(mainPanel.getPreferredSize());
            //Setting up location on screen//
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int y = (this.getY() + (this.getHeight()/4) + 44);
            if(y >= screenSize.getHeight()){
                y = this.getY();
            }
            setLocation(this.getX(), y);
            //Deleting old world//
            world.getAirplanesArrayList().forEach((n) -> (n).setGenerateNewTargets(false));
            world.getAirportsArrayList().forEach((n) -> (n) = null);
            world.getAirplanesArrayList().forEach((n) -> (n) = null);
            world = null;
            //Refreshing gui//
            revalidate();
        });
        return jButton;
    }
}