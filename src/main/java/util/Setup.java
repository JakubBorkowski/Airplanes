package util;

import javax.swing.*;
import java.awt.*;

public class Setup extends JFrame {
    private JPanel mainPanel;
    private JButton submitButton;
    private JRadioButton depthFirstSearchRadioButton;
    private JRadioButton breadthFirstSearchRadioButton;
    private JRadioButton dijkstraSShortestPathRadioButton;
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

    public Setup() {
        setTitle("Airplanes");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(AirplanesGUI.getAirplaneImageIcon().getImage());

        setSize(mainPanel.getPreferredSize());
        setContentPane(mainPanel);
        //Centering window on screen//
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);

        SpinnerModel value = new SpinnerNumberModel(1,1,99,1);
        airplanesNumberSpinner.setModel(value);
        SpinnerModel value2 = new SpinnerNumberModel(200,1,999,1);
        minFuelSpinner.setModel(value2);
        SpinnerModel value3 = new SpinnerNumberModel(200,1,999,1);
        maxFuelSpinner.setModel(value3);
        submitButton.addActionListener(e -> {
            World world =new World();
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
            String algorithmName = "DIJKSTRA";
            if(depthFirstSearchRadioButton.isSelected()){
                algorithmName = "DFS";
            }
            else if(breadthFirstSearchRadioButton.isSelected()){
                algorithmName = "BFS";
            }
            else if(dijkstraSShortestPathRadioButton.isSelected()){
                algorithmName = "DIJKSTRA";
            }
            world.addAirplanes((Integer) airplanesNumberSpinner.getValue(), (Integer) minFuelSpinner.getValue(), (Integer) maxFuelSpinner.getValue(), algorithmName);
            setSize(AirplanesGUI.getWorldImageIcon().getIconWidth(), AirplanesGUI.getWorldImageIcon().getIconHeight());
            setLocation(this.getX(), this.getY()-(this.getHeight()/4)+25);
            setContentPane(AirplanesGUI.getJLayeredPane());
        });

        setVisible(true);
    }
}
