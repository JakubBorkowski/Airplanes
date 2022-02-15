import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class Airplane implements Runnable {
    private final AirplanesGUI airplanesGUI;
    private double x;
    private double y;
    final String name;
    final double fuel;
    double currentFuel;
    Airport targetAirport;

    JLabel airplaneJLabel;
    JLabel maxFuelJLabel;
    JLabel currentFuelJLabel;

    private final World world;
    private Airport airport;

    ArrayList<Airport[]> nodes = new ArrayList<>();

    //LinkedList<LinkedList<Airport[]>> allPossibleTracks = new LinkedList<>();
    LinkedList<Airport[]> track = new LinkedList<>();

    private final Random rand = new Random();

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
        /*if(checkTarget()){//można dojechać od razu
            System.out.println(name + ": New track has been set up: "+ this.airport.name + " -> " + targetAirport.name);
            track.add(new Airport[]{this.airport, targetAirport});
            try {
                fly(targetAirport);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {*/
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
        //}
        this.track.clear();
    }

    private String trackDisplay(LinkedList<Airport[]> track){
        StringBuilder trackString = new StringBuilder();
        for (Airport[] airports : track){
            trackString.append(" -> ").append(airports[1].name);
        }
        return trackString.toString();
    }

    public Airport getAirport(){
        for(Airport airport : world.airportsArrayList){
            if (airport.x == x && airport.y == y){
                return airport;
            }
        }
        return null;
    }

    void printfPossibleAirports(){
        for(Airport airport : world.airportsArrayList){
            StringBuilder possibleAirports = new StringBuilder();
            for(Airport airport1 : airport.getNearAirports(fuel)){
                possibleAirports.append(airport1.name).append(", ");
            }
            System.out.println(airport.name + " : " + possibleAirports + "\n");
        }
    }

    void createNodeArray(){
        for(Airport airport : world.airportsArrayList){
            if(world.airportsArrayList.indexOf(airport) < world.airportsArrayList.size()-1){
                if(!(world.airportsArrayList.indexOf(airport)==0)){
                    if(airport.equals(world.airportsArrayList.get(world.airportsArrayList.size()-1)))
                        airport.getNearAirports(fuel);
                    Airport[] node = {airport, world.airportsArrayList.get(world.airportsArrayList.indexOf(airport)+1)};
                    nodes.add(node);
                } else {
                    airport.getNearAirports(fuel);
                    Airport[] node = {airport, world.airportsArrayList.get(world.airportsArrayList.indexOf(airport)+1)};
                    nodes.add(node);
                }
            }
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
        for(ArrayList<Airport[]> nodlesMap : allPossibleTracks){
            System.out.println(printfNodeMap(nodlesMap));
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
                if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && !isNodeMapContainingNode(nodeNeighbor, track)){
                    if(nodeNeighbor[1].equals(targetAirport)){
                        track.add(nodeNeighbor);
                        if(!isFinishedNodeMapContainingNodeMap(track, allPossibleTracks)){
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
                    if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && !isNodeMapContainingNode(nodeNeighbor, track)){
                        //track.add(nodeNeighbor);
                        if(nodeNeighbor[1].equals(this.targetAirport)){
                            track.add(nodeNeighbor);
                            if(!isFinishedNodeMapContainingNodeMap(track, allPossibleTracks)){
                                //System.out.println("Adding to finishnodlesMap = " + printfNodleMap(track));
                                allPossibleTracks.add(new LinkedList<>(track));
                                nodeFound = true;
                            }
                            track.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeFound && !nodeNeighbor[1].equals(this.airport) && !nodeNeighbor[1].equals(track.get(track.size()-1)[1]) && !isNodeMapContainingNode(nodeNeighbor, track)){
                            //if(track.size()>0 && !track.get(track.size()-1)[1].equals(nodeNeighbor[0])){
                            track.add(nodeNeighbor);
                            allPossibleTracks = recursion(track, allPossibleTracks);
                            track.remove(nodeNeighbor);
                            //}
                        }
                    }
                }
            }
            //nodesToSearch.clear();
            //track.clear();
        }
        return allPossibleTracks;
    }

    boolean isNodeMapContainingNode(Airport[] node, LinkedList<Airport[]> possibleTrack){
        for(Airport[] nodeInTrack : possibleTrack){
            if(node[0]==nodeInTrack[0] || node[1]==nodeInTrack[1]){
                return true;//nodle sa takie same
            }
        }
        return false;
    }

    boolean isFinishedNodeMapContainingNodeMap(LinkedList<Airport[]> possibleTrack, LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        if(!possibleTrack.get(0)[0].equals(this.airport) && possibleTrack.get(possibleTrack.size()-1)[1].equals(this.targetAirport)){
            return false;
        }
        int count = 0;
        for(LinkedList<Airport[]> trackFromAllPossibleTracks : allPossibleTracks){
            if(possibleTrack.size()==trackFromAllPossibleTracks.size()){
                for(Airport[] nodeFromAllPossibleTracks : trackFromAllPossibleTracks){
                    for(Airport[] nodeFromPossibleTrack : possibleTrack){
                        if(nodeFromAllPossibleTracks[0]==nodeFromPossibleTrack[0] && nodeFromAllPossibleTracks[1]==nodeFromPossibleTrack[1]){
                            count++;
                        }
                    }
                    if(count==possibleTrack.size()){
                        return true;//Są takie same
                    }
                    count = 0;
                }
            }
        }
        return false;//nie są takie same
    }

    private boolean checkTarget(){
        return fuel >= Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
    }

    private boolean checkDistanceToAirport(Airport targetAirport){
        return fuel >= Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
    }

    double checkDistance(Airport initialAirport, Airport finalAirport){
        return Math.sqrt(Math.pow((initialAirport.x - finalAirport.x), 2) + Math.pow((initialAirport.y - finalAirport.y), 2));
    }

    String printfNodeMap(ArrayList<Airport[]> nodeMap){
        StringBuilder nodeMapStr = new StringBuilder();
        for(Airport[] node : nodeMap){
            nodeMapStr.append("{").append(node[0].name).append(", ").append(node[1].name).append("}");
        }
        return nodeMapStr.toString();
    }

    private void fly(Airport targetAirport) throws InterruptedException {
        this.airport = null;
        this.airplaneJLabel.setVisible(true);
        currentFuelJLabel.setVisible(true);
        maxFuelJLabel.setVisible(true);
        final double xx = targetAirport.x - x;
        final double yy = targetAirport.y - y;
        double distance = Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
        double xmultiple = ((abs(xx) / abs(Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2)))));
        double ymultiple = ((abs(yy) / abs(Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2)))));
        currentFuel = fuel;

        double angle = Math.toDegrees(Math.atan2((y - targetAirport.y), (x - targetAirport.x))) - 90;
        if(angle < 0){
            angle += 360;
        }

        double xBackup = x;
        double yBackup = y;
        while(true) {
            xBackup = x;
            yBackup = y;
            if ((x < targetAirport.x)){
                if(targetAirport.x < (x + abs(xmultiple))){
                    x = targetAirport.x;
                }
                else {
                    x = (x + abs(xmultiple));
                }
            }
            else if ((x > targetAirport.x)){
                if(targetAirport.x > (x - abs(xmultiple))){
                    x = targetAirport.x;
                }
                else {
                    x = (x - abs(xmultiple));
                }
            }
            if (y < targetAirport.y){
                if(targetAirport.y < (y + abs(ymultiple))){
                    y = targetAirport.y;
                }
                else {
                    y = (y + abs(ymultiple));
                }
            }
            else if (y > targetAirport.y){
                if(targetAirport.y > (y - abs(ymultiple))){
                    y = targetAirport.y;
                }
                else {
                    y = (y - abs(ymultiple));
                }
            }
            /*if(currentfuel<0){
                System.out.println(currentfuel);
            }*/
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

            //airplanesGUI.updateAirplane(airplaneJLabel, (int) x, (int) y, angle);
            //airplanesGUI.updateFuelStatusAirplane(this);

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
            //this.allPossibleTracks.clear();
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
        while (true){
            if(this.targetAirport==null){
                do{
                    targetAirport = world.airportsArrayList.get(rand.nextInt(world.airportsArrayList.size()));
                }
                while (targetAirport.x == this.x && targetAirport.y == this.y);
                System.out.println(name + ": New target: " + targetAirport.name);
                System.out.println(name + ": Searching flight: " + this.airport.name + " -> "  + targetAirport.name);
                this.setTarget(targetAirport);
            }
        }
    }
}