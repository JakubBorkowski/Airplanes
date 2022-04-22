package gui;

import lombok.Getter;
import thirdparty.RotatedIcon;
import util.Airplane;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Graphic interface on which airplanes and airports will be displayed.
 */
public class AirplanesGUI {
    @Getter private final static JLayeredPane jLayeredPane = new JLayeredPane();//Main Layer
    private final ImageIcon airportImageIcon = new ImageIcon(Objects.requireNonNull(
            AirplanesGUI.class.getResource("/images/Airport.png")));
    @Getter private final static ImageIcon airplaneImageIcon = new ImageIcon(Objects.requireNonNull(
            AirplanesGUI.class.getResource("/images/Airplane.png")));
    @Getter private final static ImageIcon worldImageIcon = new ImageIcon(Objects.requireNonNull(
            AirplanesGUI.class.getResource("/images/World.png")));

    /**
     * Main JFrame of an app.
     */
    public AirplanesGUI(){
        //Adding main JLayerPane//
        jLayeredPane.setBounds(0, 0, worldImageIcon.getIconWidth(), worldImageIcon.getIconHeight());
        //Adding World map//
        JLabel worldJLabel = new JLabel(worldImageIcon);
        worldJLabel.setBounds(0, 0, worldImageIcon.getIconWidth(), worldImageIcon.getIconHeight());
        jLayeredPane.add(worldJLabel, JLayeredPane.DEFAULT_LAYER);
    }

    public void addBackButton(JButton backButton) {
        jLayeredPane.add(backButton, JLayeredPane.DRAG_LAYER);
        jLayeredPane.revalidate();
        jLayeredPane.validate();
    }

    /**
     * Draws airport in specified position.
     * @param x X-axis position of the airport.
     * @param y Y-axis position of the airport.
     * @param name Name of airport.
     */
    public void drawAirport(int x, int y, String name){
        //Centering position of airport image//
        x = x - (airportImageIcon.getIconWidth() / 2);
        y = y - (airportImageIcon.getIconHeight() / 2);
        //Drawing airport image//
        JLabel airportJLabel = new JLabel(airportImageIcon);
        airportJLabel.setBounds(x, y, airportImageIcon.getIconWidth(), airportImageIcon.getIconHeight());
        jLayeredPane.add(airportJLabel, JLayeredPane.PALETTE_LAYER);
        //Drawing airport name//
        JLabel airportNameJLabel = new JLabel(name);
        airportNameJLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        airportNameJLabel.setForeground(Color.WHITE);
        if((airportImageIcon.getIconWidth()/2) - (name.length()*7/2) > 0){
            airportNameJLabel.setBounds(x + abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)),
                    y + airportImageIcon.getIconHeight() + 3, name.length()*7, 15);
        } else {
            airportNameJLabel.setBounds(x - abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)),
                    y + airportImageIcon.getIconHeight() + 3, name.length()*7, 15);
        }
        jLayeredPane.add(airportNameJLabel, JLayeredPane.PALETTE_LAYER);
        //Drawing airport name background//
        drawAirportNameBackground(x, y, name);
    }

    /**
     * Draws dark background behind airport name.
     * @param x X-axis position of the airport.
     * @param y Y-axis position of the airport.
     * @param name Name of airport.
     */
    private void drawAirportNameBackground(int x, int y, String name){
        ImageIcon airportNameBackground = new ImageIcon(Objects.requireNonNull(
                AirplanesGUI.class.getResource("/images/Background.png")));
        Image image = airportNameBackground.getImage();
        Image scaledImage = image.getScaledInstance(name.length()*7+5, 15,  java.awt.Image.SCALE_SMOOTH);
        airportNameBackground = new ImageIcon(scaledImage);
        JLabel airportNameBackgroundJLabel = new JLabel(airportNameBackground);
        if((airportImageIcon.getIconWidth()/2) - (name.length()*7/2) > 0){
            airportNameBackgroundJLabel.setBounds(
                    x + abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)) - 3,
                    y + airportImageIcon.getIconHeight() + 2,
                    airportNameBackground.getIconWidth(), airportNameBackground.getIconHeight());
        } else {
            airportNameBackgroundJLabel.setBounds(
                    x - abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)) - 3,
                    y + airportImageIcon.getIconHeight() + 2,
                    airportNameBackground.getIconWidth(), airportNameBackground.getIconHeight());
        }
        jLayeredPane.add(airportNameBackgroundJLabel, JLayeredPane.PALETTE_LAYER);
    }

    /**
     * Creates airplane JLabel in specified position.
     * @param x X-axis position of airplane.
     * @param y Y-axis position of airplane.
     * @return created JLabel of airplane.
     */
    public JLabel drawAirplane(int x, int y) {
        JLabel airplaneJLabel = new JLabel(airplaneImageIcon);
        airplaneJLabel.setBounds(x - airplaneImageIcon.getIconWidth()/2, y - airplaneImageIcon.getIconHeight()/2,
                airplaneImageIcon.getIconWidth(), airplaneImageIcon.getIconHeight());
        jLayeredPane.add(airplaneJLabel, JLayeredPane.MODAL_LAYER);
        airplaneJLabel.setVisible(false);
        return airplaneJLabel;
    }

    /**
     * Updates position of specified airplane JLabel.
     * @param airplaneJLabel JLabel of airplane to update.
     * @param x X-axis position in which airplane should be.
     * @param y Y-axis position in which airplane should be.
     * @param angle Angel at which airplane should be rotated.
     */
    public void updateAirplane(JLabel airplaneJLabel, int x, int y, double angle) {
        RotatedIcon airplaneRotatedIcon = new RotatedIcon(airplaneImageIcon, angle);
        airplaneJLabel.setIcon(airplaneRotatedIcon);
        airplaneJLabel.setBounds(x - airplaneImageIcon.getIconWidth()/2, y - airplaneImageIcon.getIconHeight()/2,
                airplaneImageIcon.getIconWidth(), airplaneImageIcon.getIconHeight());
    }

    /**
     * Creates JLabel indicated maximum fuel of specified airplane.
     * @param airplane Airplane for which maxFuel JLabel will be created.
     * @return created maxFuel JLabel for airplane.
     */
    public JLabel drawMaxFuelStatus(Airplane airplane) {
        JLabel fuelJLabel = new JLabel();
        fuelJLabel.setBounds(airplane.getAirplaneJLabel().getBounds().x - 5,
                airplane.getAirplaneJLabel().getBounds().y + 25, 35, 3);
        fuelJLabel.setBackground(Color.RED);
        jLayeredPane.add(fuelJLabel, JLayeredPane.MODAL_LAYER);
        return fuelJLabel;
    }

    /**
     * Creates JLabel indicated current fuel of specified airplane.
     * @param airplane Airplane for which currentFuel JLabel will be created.
     * @return created currentFuel JLabel for airplane.
     */
    public JLabel drawCurrentFuelStatus(Airplane airplane) {
        JLabel fuelJLabel = new JLabel();
        fuelJLabel.setBounds(airplane.getAirplaneJLabel().getBounds().x - 5,
                airplane.getAirplaneJLabel().getBounds().y + 25, 35, 3);
        fuelJLabel.setBackground(Color.GREEN);
        jLayeredPane.add(fuelJLabel, JLayeredPane.MODAL_LAYER);
        return fuelJLabel;
    }

    /**
     * Updates position and width of maximum and current fuel JLabels of specified airplane.
     * @param airplane Airplane for which fuel JLabels will be updated.
     */
    public void updateFuelStatusAirplane(Airplane airplane) {
        int width = (int) (35 * airplane.getCurrentFuel() / (airplane.getFuel()));
        airplane.getMaxFuelJLabel().setBounds(airplane.getAirplaneJLabel().getBounds().x - 5,
                airplane.getAirplaneJLabel().getBounds().y + 25, 35, 3);
        airplane.getMaxFuelJLabel().setOpaque(true);
        airplane.getCurrentFuelJLabel().setBounds(airplane.getAirplaneJLabel().getBounds().x - 5,
                airplane.getAirplaneJLabel().getBounds().y + 25, width, 3);
        airplane.getCurrentFuelJLabel().setOpaque(true);
    }
}