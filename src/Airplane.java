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

    public Airplane(Airport airport, String name, double fuel, AirplanesGUI airplanesGUI, World world, String algorithmName){
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
        //printfPossibleAirports();
    }

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
            System.out.println(name + ": New track has been set up: " + this.airport.name + trackDisplay(track)
                    + " For a fly: " + this.airport.name + " -> " + this.targetAirport.name);
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

    private String trackDisplay(LinkedList<Airport[]> track){
        StringBuilder trackString = new StringBuilder();
        for (Airport[] airports : track){
            trackString.append(" -> ").append(airports[1].name);
        }
        return trackString.toString();
    }

    private void fly(Airport targetAirport) throws InterruptedException {
        this.airport = null;
        this.airplaneJLabel.setVisible(true);
        currentFuelJLabel.setVisible(true);
        maxFuelJLabel.setVisible(true);
        double distance = Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
        double xMultiplier = (abs(targetAirport.x - x) / distance);
        double yMultiplier = (abs(targetAirport.y - y) / distance);
        currentFuel = fuel;

        double angle = Math.toDegrees(Math.atan2((y - targetAirport.y), (x - targetAirport.x))) - 90;
        if(angle < 0){
            angle += 360;
        }

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
            currentFuel = currentFuel - Math.sqrt(Math.pow((xBackup - x), 2) + Math.pow((yBackup - y), 2));
            Airplane thisAirplane = this;
            double finalAngle = angle;
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
        while (true){
            if(this.targetAirport==null){
                do{
                    targetAirport = world.airportsArrayList.get(random.nextInt(world.airportsArrayList.size()));
                }
                while (targetAirport.x == this.x && targetAirport.y == this.y);
                System.out.println(name + ": New target: " + targetAirport.name);
                System.out.println(name + ": Searching flight: " + this.airport.name + " -> "  + targetAirport.name);
                this.setTarget(targetAirport);
            }
        }
    }
}