import thirdparty.RotatedIcon;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;

public class AirplanesGUI extends JFrame {

    private final JLayeredPane jLayeredPane = new JLayeredPane();//Main Layer
    ImageIcon airportImageIcon = new ImageIcon("src/images/Airport.png");
    ImageIcon airplaneImageIcon = new ImageIcon("src/images/Airplane.png");

    public AirplanesGUI(){
        setTitle("Airplanes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(697, 605);
        //setLayout(null);
        setResizable(false);
        setVisible(true);

        jLayeredPane.setBounds(0, 0, 697, 605);
        //jLayeredPane.setVisible(true);
        add(jLayeredPane);

        //Adding World map
        ImageIcon worldImageIcon = new ImageIcon("src/images/World.png");
        JLabel worldJLabel = new JLabel(worldImageIcon);
        worldJLabel.setBounds(0, -20, worldImageIcon.getIconWidth(), worldImageIcon.getIconHeight());
        jLayeredPane.add(worldJLabel, JLayeredPane.DEFAULT_LAYER);
    }

    public void drawAirport(int x, int y, String name){
        x = x - (airportImageIcon.getIconWidth() / 2);
        y = y - (airportImageIcon.getIconHeight() / 2);
        //ImageIcon airportImageIcon = new ImageIcon("src/images/Airport.png");
        JLabel airportJLabel = new JLabel(airportImageIcon);
        airportJLabel.setBounds(x, y, airportImageIcon.getIconWidth(), airportImageIcon.getIconHeight());
        jLayeredPane.add(airportJLabel, JLayeredPane.PALETTE_LAYER);
        JLabel airportNameJLabel = new JLabel(name);
        airportNameJLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        airportNameJLabel.setForeground(Color.WHITE);
        if((airportImageIcon.getIconWidth()/2) - (name.length()*7/2) > 0){
            airportNameJLabel.setBounds(x + abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)), y + airportImageIcon.getIconHeight() + 3, name.length()*7, 15);
        } else {
            airportNameJLabel.setBounds(x - abs((airportImageIcon.getIconWidth()/2) - (name.length()*7/2)), y + airportImageIcon.getIconHeight() + 3, name.length()*7, 15);
        }
        jLayeredPane.add(airportNameJLabel, JLayeredPane.PALETTE_LAYER);

        //airportNameJLabel.setBackground(Color.lightGray);
        //airportNameJLabel.setOpaque(true);
    }

    public JLabel drawAirplane(int x, int y) {
        //ImageIcon airplaneImageIcon = new ImageIcon("src/images/Airplane.png");
        JLabel airplaneJLabel = new JLabel(airplaneImageIcon);
        airplaneJLabel.setBounds(x - airplaneImageIcon.getIconWidth()/2, y - airplaneImageIcon.getIconHeight()/2, airplaneImageIcon.getIconWidth(), airplaneImageIcon.getIconHeight());
        jLayeredPane.add(airplaneJLabel, JLayeredPane.MODAL_LAYER);
        return airplaneJLabel;
    }

    public void updateAirplane(JLabel airplaneJLabel, int x, int y, double angle) {
        RotatedIcon airplaneRotetedJLabel = new RotatedIcon(airplaneImageIcon, angle);
        airplaneJLabel.setIcon(airplaneRotetedJLabel);
        airplaneJLabel.setBounds(x - airplaneImageIcon.getIconWidth()/2, y - airplaneImageIcon.getIconHeight()/2, airplaneImageIcon.getIconWidth(), airplaneImageIcon.getIconHeight());

        revalidate();
        validate();
        repaint();
        setVisible(true);
    }

    public JLabel drawMaxFuelStatus(Airplane airplane) {
        JLabel fuelJLabel = new JLabel();
        fuelJLabel.setBounds(airplane.airplaneJLabel.getBounds().x - 5, airplane.airplaneJLabel.getBounds().y + 25, 35, 3);
        fuelJLabel.setBackground(Color.RED);
        fuelJLabel.setOpaque(true);
        jLayeredPane.add(fuelJLabel, JLayeredPane.MODAL_LAYER);

        revalidate();
        validate();
        repaint();
        setVisible(true);
        return fuelJLabel;
    }

    public JLabel drawCurrentFuelStatus(Airplane airplane) {
        JLabel fuelJLabel = new JLabel();
        fuelJLabel.setBounds(airplane.airplaneJLabel.getBounds().x - 5, airplane.airplaneJLabel.getBounds().y + 25, 35, 3);
        fuelJLabel.setBackground(Color.GREEN);
        fuelJLabel.setOpaque(true);
        jLayeredPane.add(fuelJLabel, JLayeredPane.MODAL_LAYER);

        revalidate();
        validate();
        repaint();
        setVisible(true);
        return fuelJLabel;
    }

    public void updateFuelStatusAirplane(Airplane airplane) {
        int width = (int) (35 * airplane.currentfuel / (airplane.fuel));

        airplane.maxfuelJLabel.setBounds(airplane.airplaneJLabel.getBounds().x - 5, airplane.airplaneJLabel.getBounds().y + 25, 35, 3);
        airplane.currentFuelJLabel.setBounds(airplane.airplaneJLabel.getBounds().x - 5, airplane.airplaneJLabel.getBounds().y + 25, width, 3);

        revalidate();
        validate();
        repaint();
        setVisible(true);
    }
}
