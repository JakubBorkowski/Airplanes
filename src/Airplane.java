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

    public Airplane(Airport airport, String name, double fuel, AirplanesGUI airplanesGUI, World world){
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
        //printfPossibleAirports();
    }

    public void setTarget(Airport targetAirport){
            LinkedList<LinkedList<Airport[]>> allPossibleTracks = new LinkedList<>();
            allPossibleTracks = startRecursion(allPossibleTracks);
            if(allPossibleTracks.isEmpty()){
                System.out.println(name + ": Can't reach " + targetAirport.name);
                this.targetAirport = null;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                track = sortAllPossibleTracks(allPossibleTracks);
                System.out.println(name + ": New track has been set up: " + this.airport.name + trackDisplay(track)
                        + " For a fly: " + this.airport.name + " -> " + this.targetAirport.name);
                for (Airport[] airports : track){
                    try {
                        fly(airports[1]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        this.track.clear();
    }

    private String trackDisplay(LinkedList<Airport[]> track){
        StringBuilder trackString = new StringBuilder();
        for (Airport[] airports : track){
            trackString.append(" -> ").append(airports[1].name);
        }
        return trackString.toString();
    }

    void printfPossibleAirports(){
        System.out.println("Possible airports for " + this.name);
        for(Airport airport : world.airportsArrayList){
            StringBuilder possibleAirports = new StringBuilder();
            for(Airport airport1 : airport.getNearAirports(fuel)){
                possibleAirports.append(airport1.name).append(", ");
            }
            System.out.println(airport.name + " : " + possibleAirports + "\n");
        }
    }

    LinkedList<Airport[]> sortAllPossibleTracks(LinkedList<LinkedList<Airport[]>> allPossibleTracks){

        Comparator<LinkedList<Airport[]>> compareByDistance = new Comparator<LinkedList<Airport[]>>() {
            @Override
            public int compare(LinkedList<Airport[]> track1, LinkedList<Airport[]> track2) {
                double distance1 = 0;
                for(Airport[] node : track1){
                    distance1 += world.checkDistance(node[0], node[1]);
                }
                double distance2 = 0;
                for(Airport[] node : track2){
                    distance2 += world.checkDistance(node[0], node[1]);
                }
                return Double.compare(distance1, distance2);
            }
        };
        allPossibleTracks.sort(compareByDistance);

        /*System.out.println("\nallPossibleTracks for " + this.airport.name + " -> " + this.targetAirport.name + ":");
        for(ArrayList<Airport[]> track : allPossibleTracks){
            System.out.println(printfNodeMap(track));
        }
        System.out.println();*/

        return allPossibleTracks.get(0);
    }

    LinkedList<LinkedList<Airport[]>> startRecursion(LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        LinkedList<Airport[]> track = new LinkedList<>();
        ArrayList<Airport[]> thisAirportNodes = new ArrayList<>(this.airport.getNodes(fuel));
        for(Airport[] node : thisAirportNodes){
            if(node[1].equals(targetAirport)){
                track.add(node);
                allPossibleTracks.add(track);
                break;
            } else {
                track.add(node);
                allPossibleTracks = recursion(track, allPossibleTracks);
            }
            track.clear();
        }
        return allPossibleTracks;
    }

    LinkedList<LinkedList<Airport[]>> recursion(LinkedList<Airport[]> track, LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        boolean nodeFound = false;
        LinkedList<Airport[]> nodesToSearch = new LinkedList<>();//backup
        if(track.size()>0){
            track.get(track.size()-1)[1].getNodes(fuel);
            ArrayList<Airport[]> node1GetNodes = new ArrayList<>(track.get(track.size()-1)[1].getNodes(fuel));
            for(Airport[] nodeNeighbor : node1GetNodes){
                if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && trackNotContainNode(nodeNeighbor, track)){
                    if(nodeNeighbor[1].equals(targetAirport)){
                        track.add(nodeNeighbor);
                        if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                            //System.out.println("Adding to allPossibleTracks = " + printfNodeMap(track));
                            allPossibleTracks.add(new LinkedList<>(track));
                            nodeFound = true;
                        }
                        track.remove(nodeNeighbor);
                        break;
                    }
                    else if(!nodeFound) {
                        nodesToSearch.add(nodeNeighbor);
                    }
                }
            }
            for(Airport[] nodeNeighbor : nodesToSearch){
                if(track.size()>0){
                    if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && trackNotContainNode(nodeNeighbor, track)){
                        if(nodeNeighbor[1].equals(this.targetAirport)){
                            track.add(nodeNeighbor);
                            if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                                //System.out.println("Adding to allPossibleTracks = " + printfNodleMap(track));
                                allPossibleTracks.add(new LinkedList<>(track));
                                nodeFound = true;
                            }
                            track.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeFound && !nodeNeighbor[1].equals(this.airport) && !nodeNeighbor[1].equals(track.get(track.size()-1)[1]) && trackNotContainNode(nodeNeighbor, track)){
                            track.add(nodeNeighbor);
                            allPossibleTracks = recursion(track, allPossibleTracks);
                            track.remove(nodeNeighbor);
                        }
                    }
                }
            }
            //nodesToSearch.clear();
            //track.clear();
        }
        return allPossibleTracks;
    }

    boolean trackNotContainNode(Airport[] node, LinkedList<Airport[]> track){
        for(Airport[] nodeInTrack : track){
            if(node[0]==nodeInTrack[0] || node[1]==nodeInTrack[1]){
                return false;
            }
        }
        return true;
    }

    boolean allPossibleTracksNotContainTrack(LinkedList<Airport[]> track, LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        if(!track.get(0)[0].equals(this.airport) && track.get(track.size()-1)[1].equals(this.targetAirport)){
            return true;
        }
        int count = 0;
        for(LinkedList<Airport[]> trackFromAllPossibleTracks : allPossibleTracks){
            if(track.size()==trackFromAllPossibleTracks.size()){
                for(Airport[] nodeFromAllPossibleTracks : trackFromAllPossibleTracks){
                    for(Airport[] nodeFromPossibleTrack : track){
                        if(nodeFromAllPossibleTracks[0]==nodeFromPossibleTrack[0] && nodeFromAllPossibleTracks[1]==nodeFromPossibleTrack[1]){
                            count++;
                        }
                    }
                    if(count==track.size()){
                        return false;
                    }
                    count = 0;
                }
            }
        }
        return true;
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