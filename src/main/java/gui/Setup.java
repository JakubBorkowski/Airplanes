package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import util.World;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.LinkedList;
import java.util.Locale;

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
     * LinkedList of all airports JCheckBox
     */
    private final LinkedList<JCheckBox> polishAirportsCheckBoxes = new LinkedList<>();

    /**
     * Creates main JFrame of an app
     */
    public Setup() {
        //Setting up JFrame//
        initializeJFrame();
        //Centering window on screen//
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - this.getSize().width / 2, screenSize.height / 2 - this.getSize().height / 2);
        //Setting up JFrame components//
        //Setting up minFuelSpinner and maxFuelSpinner//
        SpinnerModel value = new SpinnerNumberModel(1, 1, 99, 1);
        airplanesNumberSpinner.setModel(value);
        SpinnerModel value2 = new SpinnerNumberModel(200, 1, 999, 1);
        minFuelSpinner.setModel(value2);
        SpinnerModel value3 = new SpinnerNumberModel(200, 1, 999, 1);
        maxFuelSpinner.setModel(value3);
        //Adding ChangeListeners to Spinners//
        minFuelSpinner.addChangeListener(e -> {
            if ((Integer) maxFuelSpinner.getValue() < (Integer) minFuelSpinner.getValue()) {
                maxFuelSpinner.setValue(minFuelSpinner.getValue());
            }
        });
        maxFuelSpinner.addChangeListener(e -> {
            if ((Integer) maxFuelSpinner.getValue() < (Integer) minFuelSpinner.getValue()) {
                minFuelSpinner.setValue(maxFuelSpinner.getValue());
            }
        });
        //Adding airports checkboxes to LinkedList
        polishAirportsCheckBoxes.add(zielonaGoraCheckBox);
        polishAirportsCheckBoxes.add(gorzowWielokopolskiCheckBox);
        polishAirportsCheckBoxes.add(szczecinCheckBox);
        polishAirportsCheckBoxes.add(gdanskCheckBox);
        polishAirportsCheckBoxes.add(olsztynCheckBox);
        polishAirportsCheckBoxes.add(bialystokCheckBox);
        polishAirportsCheckBoxes.add(warszawaCheckBox);
        polishAirportsCheckBoxes.add(bydgoszczCheckBox);
        polishAirportsCheckBoxes.add(torunCheckBox);
        polishAirportsCheckBoxes.add(poznanCheckBox);
        polishAirportsCheckBoxes.add(lodzCheckBox);
        polishAirportsCheckBoxes.add(wroclawCheckBox);
        polishAirportsCheckBoxes.add(opoleCheckBox);
        polishAirportsCheckBoxes.add(katowiceCheckBox);
        polishAirportsCheckBoxes.add(krakowCheckBox);
        polishAirportsCheckBoxes.add(rzeszowCheckBox);
        polishAirportsCheckBoxes.add(kielceCheckBox);
        polishAirportsCheckBoxes.add(lublinCheckBox);
        //Adding ActionListener to selectAllAirportsCheckBox//
        selectAllAirportsCheckBox.addActionListener(e -> {
            if (selectAllAirportsCheckBox.isSelected()) {
                for (JCheckBox airportCheckBox : polishAirportsCheckBoxes) {
                    airportCheckBox.setSelected(true);
                }
            } else {
                for (JCheckBox airportCheckBox : polishAirportsCheckBoxes) {
                    airportCheckBox.setSelected(false);
                }
            }
        });
        //Adding ActionListeners to airports checkboxes//
        for (JCheckBox airportCheckBox : polishAirportsCheckBoxes) {
            airportCheckBox.addActionListener(e -> {
                if (!airportCheckBox.isSelected()) {
                    selectAllAirportsCheckBox.setSelected(false);
                } else {
                    if (areAllAirportsCheckBoxesSelected()) {
                        selectAllAirportsCheckBox.setSelected(true);
                    }
                }
            });
        }
        //Adding ActionListener to submitButton//
        submitButton.addActionListener(e -> {
            //Determining algorithm name//
            String algorithmName = "DIJKSTRA";
            if (depthFirstSearchRadioButton.isSelected()) {
                algorithmName = "DFS";
            } else if (breadthFirstSearchRadioButton.isSelected()) {
                algorithmName = "BFS";
            } else if (dijkstraShortestPathRadioButton.isSelected()) {
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
            setLocation(this.getX(), Math.max(this.getY() - (this.getHeight() / 4) + 25, 0));
            //Adding airports and airplanes
            addAirports();
            world.addAirplanes((Integer) airplanesNumberSpinner.getValue(), (Integer) minFuelSpinner.getValue(),
                    (Integer) maxFuelSpinner.getValue(), algorithmName);
        });
    }

    /**
     * Creates JFrame with world map, skipping the setting page
     *
     * @param numberOfAirplanes Number of airplanes to create.
     * @param minFuel           Minimal number of fuel which airplane can have.
     * @param maxFuel           Maximum number of fuel which airplane can have.
     * @param algorithmName     name of algorithm which will be used by airplane to find path.
     *                          Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public Setup(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName) {
        //Displaying provided values//
        displayValues(numberOfAirplanes, minFuel, maxFuel, algorithmName);
        //Setting up JFrame//
        initializeJFrame();
        setSize(AirplanesGUI.getWorldImageIcon().getIconWidth(), AirplanesGUI.getWorldImageIcon().getIconHeight());
        //Centering window on screen//
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - this.getSize().width / 2, screenSize.height / 2 - this.getSize().height / 2);
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
    private void initializeJFrame() {
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
     *
     * @param numberOfAirplanes Number of airplanes which will be created.
     * @param minFuel           Minimal number of fuel which airplane can have.
     * @param maxFuel           Maximum number of fuel which airplane can have.
     * @param algorithmName     name of algorithm which will be used by airplane to find path.
     *                          Available names: "BFS", "DFS", "DIJKSTRA".
     */
    private void displayValues(int numberOfAirplanes, int minFuel, int maxFuel, String algorithmName) {
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
    private void addAirports() {
        if (zielonaGoraCheckBox.isSelected()) {
            world.addAirport(101, 319, "Zielona Góra");
        }
        if (gorzowWielokopolskiCheckBox.isSelected()) {
            world.addAirport(81, 238, "Gorzów Wielokopolski");
        }
        if (szczecinCheckBox.isSelected()) {
            world.addAirport(44, 163, "Szczecin");
        }
        if (gdanskCheckBox.isSelected()) {
            world.addAirport(301, 71, "Gdańsk");
        }
        if (olsztynCheckBox.isSelected()) {
            world.addAirport(413, 138, "Olsztyn");
        }
        if (bialystokCheckBox.isSelected()) {
            world.addAirport(578, 203, "Białystok");
        }
        if (warszawaCheckBox.isSelected()) {
            world.addAirport(443, 286, "Warszawa");
        }
        if (bydgoszczCheckBox.isSelected()) {
            world.addAirport(253, 196, "Bydgoszcz");
        }
        if (torunCheckBox.isSelected()) {
            world.addAirport(299, 209, "Toruń");
        }
        if (poznanCheckBox.isSelected()) {
            world.addAirport(189, 272, "Poznań");
        }
        if (lodzCheckBox.isSelected()) {
            world.addAirport(348, 346, "Łódź");
        }
        if (wroclawCheckBox.isSelected()) {
            world.addAirport(200, 399, "Wrocław");
        }
        if (opoleCheckBox.isSelected()) {
            world.addAirport(252, 455, "Opole");
        }
        if (katowiceCheckBox.isSelected()) {
            world.addAirport(319, 498, "Katowice");
        }
        if (krakowCheckBox.isSelected()) {
            world.addAirport(376, 501, "Kraków");
        }
        if (rzeszowCheckBox.isSelected()) {
            world.addAirport(516, 517, "Rzeszów");
        }
        if (kielceCheckBox.isSelected()) {
            world.addAirport(422, 385, "Kielce");
        }
        if (lublinCheckBox.isSelected()) {
            world.addAirport(545, 393, "Lublin");
        }
    }

    /**
     * Checks if all JCheckBoxes in polishAirportsCheckBoxes LinkedList are selected
     *
     * @return True if all JCheckBoxes in polishAirportsCheckBoxes LinkedList are selected
     * or false, if at least one of them is not selected
     */
    private boolean areAllAirportsCheckBoxesSelected() {
        boolean areAllAirportsCheckBoxesSelected = true;
        for (JCheckBox airportCheckBox : polishAirportsCheckBoxes) {
            if (!airportCheckBox.isSelected()) {
                areAllAirportsCheckBoxesSelected = false;
                break;
            }
        }
        return areAllAirportsCheckBoxesSelected;
    }

    /**
     * @return backButton that allows to back to setting (changes ContentPane of JFrame to mainPanel)
     */
    private JButton backButton() {
        JButton jButton = new JButton();
        jButton.setText("Back");
        jButton.setVisible(true);
        jButton.setSize(70, 20);
        jButton.addActionListener(e -> {
            getContentPane().removeAll();
            setContentPane(mainPanel);
            setSize(mainPanel.getPreferredSize());
            //Setting up location on screen//
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int y = (this.getY() + (this.getHeight() / 4) + 56);
            if (y >= screenSize.getHeight()) {
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(16, 5, new Insets(0, 10, 10, 10), -1, -1));
        mainPanel.setMaximumSize(new Dimension(650, 650));
        mainPanel.setMinimumSize(new Dimension(650, 290));
        mainPanel.setPreferredSize(new Dimension(650, 325));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 0, 5), -1, -1));
        mainPanel.add(panel1, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dijkstraShortestPathRadioButton = new JRadioButton();
        dijkstraShortestPathRadioButton.setEnabled(true);
        dijkstraShortestPathRadioButton.setSelected(true);
        dijkstraShortestPathRadioButton.setText("Dijkstra's shortest path algorithm");
        dijkstraShortestPathRadioButton.setMnemonic('D');
        dijkstraShortestPathRadioButton.setDisplayedMnemonicIndex(0);
        dijkstraShortestPathRadioButton.setToolTipText("Algorithm guarantee to find the shortest path relatively quick.");
        panel1.add(dijkstraShortestPathRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        breadthFirstSearchRadioButton = new JRadioButton();
        breadthFirstSearchRadioButton.setText("Breadth-first search");
        breadthFirstSearchRadioButton.setMnemonic('B');
        breadthFirstSearchRadioButton.setDisplayedMnemonicIndex(0);
        breadthFirstSearchRadioButton.setToolTipText("Algorithm don't guarantee finding the shortest path in terms of overall distance traveled, but number of airports to which airplane will stop by will be the smallest.");
        panel1.add(breadthFirstSearchRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        depthFirstSearchRadioButton = new JRadioButton();
        depthFirstSearchRadioButton.setText("Depth-first search");
        depthFirstSearchRadioButton.setMnemonic('F');
        depthFirstSearchRadioButton.setDisplayedMnemonicIndex(6);
        depthFirstSearchRadioButton.setToolTipText("Algorithm guarantee to find the best path, but it's highly ineffective.");
        panel1.add(depthFirstSearchRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 5, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(14, 0, 1, 5, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        submitButton = new JButton();
        Font submitButtonFont = this.$$$getFont$$$(null, Font.BOLD, -1, submitButton.getFont());
        if (submitButtonFont != null) submitButton.setFont(submitButtonFont);
        submitButton.setText("Submit");
        submitButton.setMnemonic('S');
        submitButton.setDisplayedMnemonicIndex(0);
        panel2.add(submitButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectAllAirportsCheckBox = new JCheckBox();
        Font selectAllAirportsCheckBoxFont = this.$$$getFont$$$(null, Font.BOLD, -1, selectAllAirportsCheckBox.getFont());
        if (selectAllAirportsCheckBoxFont != null) selectAllAirportsCheckBox.setFont(selectAllAirportsCheckBoxFont);
        selectAllAirportsCheckBox.setSelected(true);
        selectAllAirportsCheckBox.setText("Select all airports");
        selectAllAirportsCheckBox.setMnemonic('A');
        selectAllAirportsCheckBox.setDisplayedMnemonicIndex(7);
        panel2.add(selectAllAirportsCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, new GridConstraints(3, 0, 1, 5, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        mainPanel.add(separator2, new GridConstraints(5, 0, 1, 5, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        mainPanel.add(separator3, new GridConstraints(13, 0, 1, 5, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 4, new Insets(0, 5, 0, 5), -1, -1, true, false));
        mainPanel.add(panel3, new GridConstraints(7, 0, 5, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        zielonaGoraCheckBox = new JCheckBox();
        zielonaGoraCheckBox.setSelected(true);
        zielonaGoraCheckBox.setText("Zielona Góra");
        panel3.add(zielonaGoraCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gorzowWielokopolskiCheckBox = new JCheckBox();
        gorzowWielokopolskiCheckBox.setSelected(true);
        gorzowWielokopolskiCheckBox.setText("Gorzów Wielokopolski");
        panel3.add(gorzowWielokopolskiCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        szczecinCheckBox = new JCheckBox();
        szczecinCheckBox.setSelected(true);
        szczecinCheckBox.setText("Szczecin");
        panel3.add(szczecinCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gdanskCheckBox = new JCheckBox();
        gdanskCheckBox.setSelected(true);
        gdanskCheckBox.setText("Gdańsk");
        panel3.add(gdanskCheckBox, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        olsztynCheckBox = new JCheckBox();
        olsztynCheckBox.setSelected(true);
        olsztynCheckBox.setText("Olsztyn");
        panel3.add(olsztynCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bialystokCheckBox = new JCheckBox();
        bialystokCheckBox.setSelected(true);
        bialystokCheckBox.setText("Białystok");
        panel3.add(bialystokCheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warszawaCheckBox = new JCheckBox();
        warszawaCheckBox.setSelected(true);
        warszawaCheckBox.setText("Warszawa");
        panel3.add(warszawaCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bydgoszczCheckBox = new JCheckBox();
        bydgoszczCheckBox.setSelected(true);
        bydgoszczCheckBox.setText("Bydgoszcz");
        panel3.add(bydgoszczCheckBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        torunCheckBox = new JCheckBox();
        torunCheckBox.setSelected(true);
        torunCheckBox.setText("Toruń");
        panel3.add(torunCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        poznanCheckBox = new JCheckBox();
        poznanCheckBox.setSelected(true);
        poznanCheckBox.setText("Poznań");
        panel3.add(poznanCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lodzCheckBox = new JCheckBox();
        lodzCheckBox.setSelected(true);
        lodzCheckBox.setText("Łódź");
        panel3.add(lodzCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wroclawCheckBox = new JCheckBox();
        wroclawCheckBox.setSelected(true);
        wroclawCheckBox.setText("Wrocław");
        panel3.add(wroclawCheckBox, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        opoleCheckBox = new JCheckBox();
        opoleCheckBox.setSelected(true);
        opoleCheckBox.setText("Opole");
        panel3.add(opoleCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        katowiceCheckBox = new JCheckBox();
        katowiceCheckBox.setSelected(true);
        katowiceCheckBox.setText("Katowice");
        panel3.add(katowiceCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        krakowCheckBox = new JCheckBox();
        krakowCheckBox.setSelected(true);
        krakowCheckBox.setText("Kraków");
        panel3.add(krakowCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rzeszowCheckBox = new JCheckBox();
        rzeszowCheckBox.setSelected(true);
        rzeszowCheckBox.setText("Rzeszów");
        panel3.add(rzeszowCheckBox, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kielceCheckBox = new JCheckBox();
        kielceCheckBox.setSelected(true);
        kielceCheckBox.setText("Kielce");
        panel3.add(kielceCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lublinCheckBox = new JCheckBox();
        lublinCheckBox.setSelected(true);
        lublinCheckBox.setText("Lublin");
        panel3.add(lublinCheckBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 8, new Insets(5, 0, 5, 0), 8, -1));
        mainPanel.add(panel4, new GridConstraints(4, 0, 1, 5, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        airplanesNumberSpinner = new JSpinner();
        airplanesNumberSpinner.setToolTipText("Number of airplanes to create, that will be distributed equally across airports.");
        panel4.add(airplanesNumberSpinner, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Number of airplanes:");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Minimal fuel:");
        panel4.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minFuelSpinner = new JSpinner();
        minFuelSpinner.setToolTipText("Minimal fuel that created airplanes can have.");
        panel4.add(minFuelSpinner, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Maximal fuel:");
        panel4.add(label3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxFuelSpinner = new JSpinner();
        maxFuelSpinner.setToolTipText("Maximal fuel that created airplanes can have.");
        panel4.add(maxFuelSpinner, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Map:");
        panel4.add(label4, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JComboBox comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Poland");
        comboBox1.setModel(defaultComboBoxModel1);
        comboBox1.setToolTipText("Feature not yet implemented.");
        panel4.add(comboBox1, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Airports:");
        mainPanel.add(label5, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, -1, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Available algorithms:");
        mainPanel.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(depthFirstSearchRadioButton);
        buttonGroup.add(breadthFirstSearchRadioButton);
        buttonGroup.add(dijkstraShortestPathRadioButton);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}