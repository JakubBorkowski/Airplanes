package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import util.Airport;
import util.World;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Arrays;
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
    private JCheckBox selectAllAirportsCheckBox;
    private JComboBox mapJComboBox;
    private JPanel airportsJPanel;
    private World world = new World();

    /**
     * LinkedList of all displayed airports JCheckBox
     */
    private final LinkedList<JCheckBox> airportsCheckBoxes = new LinkedList<>();

    /**
     * LinkedList of all polish airports JCheckBox
     */
    private final LinkedList<Airport> polishAirports = new LinkedList<>(Arrays.asList(
            new Airport(101, 319, "Zielona Góra", world),
            new Airport(81, 238, "Gorzów Wielokopolski", world),
            new Airport(44, 163, "Szczecin", world),
            new Airport(301, 71, "Gdańsk", world),
            new Airport(413, 138, "Olsztyn", world),
            new Airport(578, 203, "Białystok", world),
            new Airport(443, 286, "Warszawa", world),
            new Airport(253, 196, "Bydgoszcz", world),
            new Airport(299, 209, "Toruń", world),
            new Airport(189, 272, "Poznań", world),
            new Airport(348, 346, "Łódź", world),
            new Airport(200, 399, "Wrocław", world),
            new Airport(252, 455, "Opole", world),
            new Airport(319, 498, "Katowice", world),
            new Airport(376, 501, "Kraków", world),
            new Airport(516, 517, "Rzeszów", world),
            new Airport(422, 385, "Kielce", world),
            new Airport(545, 393, "Lublin", world)
    ));

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
        //Adding airports checkboxes to JPanel
        mapJComboBox.setSelectedItem("Poland");
        GridLayout gridLayout = new GridLayout(5, 4);
        gridLayout.setVgap(6);
        airportsJPanel.setLayout(gridLayout);
        polishAirports.forEach((airport) -> airportsCheckBoxes.add(new JCheckBox(airport.getName())));//Creating checkboxes
        airportsCheckBoxes.forEach((checkBox) -> checkBox.setSelected(true));//Selecting all airports checkboxes
        airportsCheckBoxes.forEach((checkBox) -> airportsJPanel.add((checkBox)));
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
        //Adding ActionListener to selectAllAirportsCheckBox//
        selectAllAirportsCheckBox.addActionListener(e -> {
            if (selectAllAirportsCheckBox.isSelected()) {
                for (JCheckBox airportCheckBox : airportsCheckBoxes) {
                    airportCheckBox.setSelected(true);
                }
            } else {
                for (JCheckBox airportCheckBox : airportsCheckBoxes) {
                    airportCheckBox.setSelected(false);
                }
            }
        });
        //Adding ActionListeners to airports checkboxes//
        for (JCheckBox airportCheckBox : airportsCheckBoxes) {
            airportCheckBox.addActionListener(e -> {
                if (!airportCheckBox.isSelected()) {
                    selectAllAirportsCheckBox.setSelected(false);
                } else if (areAllAirportsCheckBoxesSelected()) {
                    selectAllAirportsCheckBox.setSelected(true);
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
            setContentPane(world.getAirplanesGUI().getJLayeredPane());
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
        setContentPane(world.getAirplanesGUI().getJLayeredPane());
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
        airportsCheckBoxes.forEach((checkBox) -> {
            if (checkBox.isSelected()) {
                Airport airport = polishAirports.get(airportsCheckBoxes.indexOf(checkBox));
                world.addAirport(airport.getX(), airport.getY(), airport.getName());
            }
        });
    }

    /**
     * Checks if all JCheckBoxes in polishAirportsCheckBoxes LinkedList are selected
     *
     * @return True if all JCheckBoxes in polishAirportsCheckBoxes LinkedList are selected
     * or false, if at least one of them is not selected
     */
    private boolean areAllAirportsCheckBoxesSelected() {
        boolean areAllAirportsCheckBoxesSelected = true;
        for (JCheckBox airportCheckBox : airportsCheckBoxes) {
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
        panel3.setLayout(new GridLayoutManager(1, 8, new Insets(5, 0, 5, 0), 8, -1));
        mainPanel.add(panel3, new GridConstraints(4, 0, 1, 5, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        airplanesNumberSpinner = new JSpinner();
        airplanesNumberSpinner.setToolTipText("Number of airplanes to create, that will be distributed equally across airports.");
        panel3.add(airplanesNumberSpinner, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Number of airplanes:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Minimal fuel:");
        panel3.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minFuelSpinner = new JSpinner();
        minFuelSpinner.setToolTipText("Minimal fuel that created airplanes can have.");
        panel3.add(minFuelSpinner, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Maximal fuel:");
        panel3.add(label3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxFuelSpinner = new JSpinner();
        maxFuelSpinner.setToolTipText("Maximal fuel that created airplanes can have.");
        panel3.add(maxFuelSpinner, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(35, 24), new Dimension(55, -1), 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Map:");
        panel3.add(label4, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mapJComboBox = new JComboBox();
        mapJComboBox.setEditable(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Poland");
        mapJComboBox.setModel(defaultComboBoxModel1);
        mapJComboBox.setToolTipText("Feature not yet implemented.");
        panel3.add(mapJComboBox, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 5), -1, -1));
        mainPanel.add(panel4, new GridConstraints(7, 0, 5, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        airportsJPanel = new JPanel();
        airportsJPanel.setLayout(new GridBagLayout());
        airportsJPanel.setEnabled(true);
        panel4.add(airportsJPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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