package util;

import org.jetbrains.annotations.Nullable;
import gui.AirplanesGUI;
import pathfinding.model.Node;
import pathfinding.model.Path;
import pathfinding.PathFinder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class Airplane implements Runnable {
    @NonNull private final AirplanesGUI airplanesGUI;
    @NonNull private final World world;
    private double x;
    private double y;
    @Getter private final String name;
    @Getter private final double fuel;
    @Getter private double currentFuel;
    @Nullable private Airport airport;
    @Nullable private Airport targetAirport;
    @NonNull @Getter private final JLabel airplaneJLabel;
    @NonNull @Getter private final JLabel maxFuelJLabel;
    @NonNull @Getter private final JLabel currentFuelJLabel;
    @Nullable @Getter private Path path;
    @NonNull private final String algorithmName;
    @Getter @Setter private boolean generateNewTargets;

    /**
     * Creates an airplane.
     * @param airport Airport in which airplane currently is.
     * @param name Name of airplane.
     * @param fuel Maximum fuel of airplane.
     * @param world World object with information about all airports on map.
     * @param algorithmName Name of algorithm which should be used. Available names: "BFS", "DFS", "DIJKSTRA".
     */
    public Airplane(@NonNull Airport airport, @NonNull String name, double fuel, @NonNull String algorithmName,
                    @NonNull World world){
        this.airplanesGUI = world.getAirplanesGUI();
        this.targetAirport = null;
        this.airport = airport;
        this.x = airport.getX();
        this.y = airport.getY();
        this.name = name;
        this.fuel = fuel;
        this.currentFuel = fuel;
        this.world = world;
        this.airplaneJLabel = airplanesGUI.drawAirplane((int) x, (int) y);
        this.currentFuelJLabel = airplanesGUI.drawCurrentFuelStatus(this);
        this.maxFuelJLabel = airplanesGUI.drawMaxFuelStatus(this);
        this.algorithmName = algorithmName;
        this.generateNewTargets = true;
    }

    /**
     * Check if it is possible to travel to specified destination airport.
     * If it is - flies in determined succession to airports, until destination airport is reached.
     * If it isn't - abandon the task.
     * @param targetAirport destination airport.
     */
    public void setTarget(Airport targetAirport){
        assert airport != null;
        PathFinder pathFinder = new PathFinder(airport, targetAirport, fuel, world);
        path = pathFinder.findPath(algorithmName);
        if(generateNewTargets){
            if(path == null){
                System.out.println(name + ": Can't reach " + targetAirport.getName());
                this.targetAirport = null;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(name + ": New track has been set up: " + airport.getName() + path.toString());
                for (Node airports : path){
                    fly(airports.getFinalAirport());
                }
                path.clear();
            }
        }
    }

    /**
     * Displays airplane and updates its position until it reach targetAirport.
     * @param targetAirport airport to which airplane will fly.
     */
    private void fly(Airport targetAirport) {
        airport = null;
        airplaneJLabel.setVisible(true);
        currentFuelJLabel.setVisible(true);
        maxFuelJLabel.setVisible(true);
        currentFuel = fuel;
        double angle = Math.toDegrees(Math.atan2((y - targetAirport.getY()), (x - targetAirport.getX()))) - 90;
        if(angle < 0){
            angle += 360;
        }
        double distance = Math.sqrt(Math.pow((x - targetAirport.getX()), 2) + Math.pow((y - targetAirport.getY()), 2));
        double xMultiplier = (abs(targetAirport.getX() - x) / distance);
        double yMultiplier = (abs(targetAirport.getY() - y) / distance);
        double xBackup;
        double yBackup;
        while(generateNewTargets) {
            xBackup = x;
            yBackup = y;
            if ((x < targetAirport.getX())){
                if(targetAirport.getX() < (x + abs(xMultiplier))){
                    x = targetAirport.getX();
                }
                else {
                    x = (x + abs(xMultiplier));
                }
            }
            else if ((x > targetAirport.getX())){
                if(targetAirport.getX() > (x - abs(xMultiplier))){
                    x = targetAirport.getX();
                }
                else {
                    x = (x - abs(xMultiplier));
                }
            }
            if (y < targetAirport.getY()){
                if(targetAirport.getY() < (y + abs(yMultiplier))){
                    y = targetAirport.getY();
                }
                else {
                    y = (y + abs(yMultiplier));
                }
            }
            else if (y > targetAirport.getY()){
                if(targetAirport.getY() > (y - abs(yMultiplier))){
                    y = targetAirport.getY();
                }
                else {
                    y = (y - abs(yMultiplier));
                }
            }
            currentFuel -= Math.sqrt(Math.pow((xBackup - x), 2) + Math.pow((yBackup - y), 2));
            Airplane thisAirplane = this;
            final double finalAngle = angle;
            SwingUtilities.invokeLater(() -> {
                airplanesGUI.updateAirplane(airplaneJLabel, (int) x, (int) y, finalAngle);
                airplanesGUI.updateFuelStatusAirplane(thisAirplane);
            });
            if(x== targetAirport.getX() && y== targetAirport.getY()){
                land(targetAirport);
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            System.out.println(name + ": Landed in: " + airport.getName());
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
        while(generateNewTargets){
            if(targetAirport==null){
                do{
                    targetAirport = world.getAirportsArrayList().get(
                            random.nextInt(world.getAirportsArrayList().size()));
                }
                while (airport == targetAirport);
                assert airport != null;
                System.out.println(name + ": New target: " + targetAirport.getName());
                System.out.println(name + ": Searching flight: " + airport.getName()
                        + " -> "  + targetAirport.getName());
                setTarget(targetAirport);
            }
        }
    }
}