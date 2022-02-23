import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class Airplane implements Runnable {
    private final AirplanesGUI airplanesGUI;
    private final World world;
    private double x;
    private double y;
    final String name;
    final double fuel;
    double currentFuel;
    private Airport airport;
    private Airport targetAirport;
    JLabel airplaneJLabel;
    JLabel maxFuelJLabel;
    JLabel currentFuelJLabel;
    private LinkedList<Airport[]> track = new LinkedList<>();
    String algorithmName;

    /**
     * Creates an airplane.
     * @param airport Airport in which airplane currently is.
     * @param name Name of airplane.
     * @param fuel Maximum fuel of airplane.
     * @param airplanesGUI Application JFrame in which airplane will be displayed.
     * @param world World object with information about all airports on map.
     * @param algorithmName Name of algorithm which should be used. Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public Airplane(Airport airport, String name, double fuel, String algorithmName,
                    AirplanesGUI airplanesGUI, World world){
        this.airplanesGUI = airplanesGUI;
        this.targetAirport = null;
        this.airport = airport;
        this.x = airport.x;
        this.y = airport.y;
        this.name = name;
        this.fuel = fuel;
        this.currentFuel = fuel;
        this.world = world;
        this.airplaneJLabel = airplanesGUI.drawAirplane((int) x, (int) y);
        this.currentFuelJLabel = airplanesGUI.drawCurrentFuelStatus(this);
        this.maxFuelJLabel = airplanesGUI.drawMaxFuelStatus(this);
        this.algorithmName = algorithmName;
    }

    /**
     * Check if it is possible to travel to specified destination airport.
     * If it is - flies in determined succession to airports, until destination airport is reached.
     * If it isn't - abandon the task.
     * @param targetAirport destination airport.
     */
    public void setTarget(Airport targetAirport){
        PathFinder pathFinder = new PathFinder(this.airport, targetAirport, fuel, world);
        track = pathFinder.findPath(algorithmName);
        if(track == null){
            System.out.println(name + ": Can't reach " + targetAirport.name);
            this.targetAirport = null;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(name + ": New track has been set up: " + this.airport.name + trackDisplay(track));
            for (Airport[] airports : track){
                try {
                    fly(airports[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.track.clear();
        }
    }

    /**
     * Converts track to string ready to display.
     * @param track LinkedList of arrays containing nodes. Every node should contain exactly 2 airports.
     *              The second airport in the array should be the same as the first one in the next node.
     *              [Airport1, Airport2], [Airport2, Airport3]...
     * @return track converted to string.
     */
    private String trackDisplay(LinkedList<Airport[]> track){
        StringBuilder trackString = new StringBuilder();
        for (Airport[] airports : track){
            trackString.append(" -> ").append(airports[1].name);
        }
        return trackString.toString();
    }

    /**
     * Displays airplane and updates its position until it reach targetAirport.
     * @param targetAirport airport to which airplane will fly.
     */
    private void fly(Airport targetAirport) throws InterruptedException {
        this.airport = null;
        this.airplaneJLabel.setVisible(true);
        currentFuelJLabel.setVisible(true);
        maxFuelJLabel.setVisible(true);
        currentFuel = fuel;
        double angle = Math.toDegrees(Math.atan2((y - targetAirport.y), (x - targetAirport.x))) - 90;
        if(angle < 0){
            angle += 360;
        }
        double distance = Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
        double xMultiplier = (abs(targetAirport.x - x) / distance);
        double yMultiplier = (abs(targetAirport.y - y) / distance);
        double xBackup;
        double yBackup;
        while(true) {
            xBackup = x;
            yBackup = y;
            if ((x < targetAirport.x)){
                if(targetAirport.x < (x + abs(xMultiplier))){
                    x = targetAirport.x;
                }
                else {
                    x = (x + abs(xMultiplier));
                }
            }
            else if ((x > targetAirport.x)){
                if(targetAirport.x > (x - abs(xMultiplier))){
                    x = targetAirport.x;
                }
                else {
                    x = (x - abs(xMultiplier));
                }
            }
            if (y < targetAirport.y){
                if(targetAirport.y < (y + abs(yMultiplier))){
                    y = targetAirport.y;
                }
                else {
                    y = (y + abs(yMultiplier));
                }
            }
            else if (y > targetAirport.y){
                if(targetAirport.y > (y - abs(yMultiplier))){
                    y = targetAirport.y;
                }
                else {
                    y = (y - abs(yMultiplier));
                }
            }
            currentFuel -= Math.sqrt(Math.pow((xBackup - x), 2) + Math.pow((yBackup - y), 2));
            Airplane thisAirplane = this;
            final double finalAngle = angle;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    airplanesGUI.updateAirplane(airplaneJLabel, (int) x, (int) y, finalAngle);
                    airplanesGUI.updateFuelStatusAirplane(thisAirplane);
                }
            });
            if(x==targetAirport.x && y==targetAirport.y){
                land(targetAirport);
                break;
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    /**
     * Hides airplane image and refuels airplane. If destination airport is reached,
     * updates current airplane airport and set its targetAirport to null.
     * @param targetAirport Airport at which airplane will land.
     */
    private void land(Airport targetAirport) {
        airplaneJLabel.setVisible(false);
        currentFuelJLabel.setVisible(false);
        maxFuelJLabel.setVisible(false);
        if(targetAirport.equals(this.targetAirport)){
            this.airport = targetAirport;
            this.targetAirport = null;
            System.out.println(name + ": Landed in: " + airport.name);
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        while(true){
            if(this.targetAirport==null){
                do{
                    targetAirport = world.airportsArrayList.get(random.nextInt(world.airportsArrayList.size()));
                }
                while (targetAirport.x == this.x && targetAirport.y == this.y);
                System.out.println(name + ": New target: " + targetAirport.name);
                System.out.println(name + ": Searching flight: " + this.airport.name + " -> "  + targetAirport.name);
                setTarget(targetAirport);
            }
        }
    }
}