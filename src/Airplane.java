import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class Airplane implements Runnable {
    AirplanesGUI airplanesGUI;
    double x;
    double y;
    String name;
    double fuel;
    double currentfuel;
    Airport targetAirport;

    JLabel airplaneJLabel;
    JLabel maxfuelJLabel;
    JLabel currentFuelJLabel;

    World world;
    Airport airport;

    ArrayList<Airport[]> nodes = new ArrayList<>();

    ArrayList<ArrayList<Airport[]>> finishNodesMap = new ArrayList<>();
    ArrayList<Airport[]> track = new ArrayList<>();

    private Random rand = new Random();

    public Airplane(Airport airport, String name, double fuel, AirplanesGUI airplanesGUI, World world){
        this.airplanesGUI = airplanesGUI;
        this.x = airport.x;
        this.y = airport.y;
        this.name = name;
        this.fuel = fuel;
        this.airplaneJLabel = airplanesGUI.drawAirplane((int) x, (int) y);
        this.world = world;
        this.targetAirport = null;
        this.airport = airport;

        this.currentFuelJLabel = airplanesGUI.drawCurrentFuelStatus(this);
        this.maxfuelJLabel = airplanesGUI.drawMaxFuelStatus(this);

        this.currentfuel = fuel;

        printfPossibleAirports();
    }

    public void setTarget(Airport targetAirport){
        if(checkTarget()){//można dojechać od razu
            System.out.println(name + ": New track has been set up: "+ this.airport.name + " -> " + targetAirport.name);
            track.add(new Airport[]{this.airport, targetAirport});
            try {
                fly(targetAirport);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            startRecursion();
            if(finishNodesMap.isEmpty()){
                System.out.println(name + ": Can't reach " + targetAirport.name);
                this.targetAirport = null;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                track = sortowanko();
                System.out.println("\n\n" + name + ": New track has been set up: " + this.airport.name + trackDispaly(track) +" For a fly: " + this.airport.name + " -> " + this.targetAirport.name + "\n");
                for (Airport[] airports : track){
                    try {
                        fly(airports[1]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.track.clear();
    }

    private String trackDispaly(ArrayList<Airport[]> track){
        String trackString = "";
        for (Airport[] airports : track){
            trackString += " -> " + airports[1].name;
        }
        return trackString;
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
            String possibleAirports = "";
            for(Airport airport1 : airport.getNearAirports(fuel)){
                possibleAirports += airport1.name + ", ";
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

    ArrayList<Airport[]> sortowanko(){
        System.out.println("\nFinishNodesMap for " + this.airport.name + " -> " + this.targetAirport.name + ":");
        for(ArrayList<Airport[]> nodlesMap : finishNodesMap){
            System.out.println(printfNodeMap(nodlesMap));
        }
        System.out.println();

        ArrayList<Double> distance = new ArrayList<>();
        double distance1 = 0;
        for(ArrayList<Airport[]> nodlesMap : finishNodesMap){
            for(Airport[] node : nodlesMap){
                distance1 += world.checkDistance(node[0], node[1]);
            }
            distance.add(distance1);
            distance1 = 0;
        }
        ArrayList<ArrayList<Airport[]>> equalMinDistanceIndex  = new ArrayList<>();
        int minDistanceIndex = 0;
        for(int i = 0; i < distance.size(); i++){
            if(distance.get(i).equals(distance.get(minDistanceIndex))){
                equalMinDistanceIndex.add(finishNodesMap.get(i));

            } else if(distance.get(minDistanceIndex)>distance.get(i)){
                minDistanceIndex = i;
                equalMinDistanceIndex.add(finishNodesMap.get(i));
            }
        }
        ArrayList<Airport[]> trackToReturn = new ArrayList<>(equalMinDistanceIndex.get(0));
        for(ArrayList<Airport[]> meh2 : equalMinDistanceIndex){
            if(trackToReturn.size()>meh2.size()){
                trackToReturn = meh2;
            }
        }
        return trackToReturn;
    }

    void startRecursion(){
        ArrayList<Airport[]> track = new ArrayList<>();
        ArrayList<Airport[]> thisAirpodNodes = new ArrayList<>(this.airport.getNodes(fuel));
        for(Airport[] node : thisAirpodNodes){
            if(node[1].equals(targetAirport)){
                track.add(node);
                finishNodesMap.add(track);
                break;
            } else {
                track.add(node);
                recursion(track);
            }
            track.clear();
        }
    }

    String printfNodeMap(ArrayList<Airport[]> nodeMap){
        String nodeMapStr = "";
        for(Airport[] node : nodeMap){
            nodeMapStr +=  "{" + node[0].name + ", " + node[1].name + "}";
        }
        return nodeMapStr;
    }

    void recursion(ArrayList<Airport[]> track){
        ArrayList<Airport[]> nodesToShearch = new ArrayList<>();//backup
        if(track.size()>0){
            track.get(track.size()-1)[1].getNodes(fuel);
            ArrayList<Airport[]> node1GetNodes = new ArrayList<>(track.get(track.size()-1)[1].getNodes(fuel));
            for(Airport[] nodeNeighbor : node1GetNodes){
                if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && !isNodeMapContaingNode(nodeNeighbor, track)){
                    if(nodeNeighbor[1].equals(targetAirport)){
                        track.add(nodeNeighbor);
                        if(!isFinishedNodeMapContainingNodeMap(track)){
                            //System.out.println("Adding to finishNodesMap = " + printfNodeMap(track));
                            finishNodesMap.add(new ArrayList<>(track));
                        }
                        track.remove(nodeNeighbor);
                        //break;
                    }
                    else {
                        nodesToShearch.add(nodeNeighbor);
                    }
                }
            }
            for(Airport[] nodeNeighbor : nodesToShearch){
                if(track.size()>0){
                    if(!(nodeNeighbor[0] == this.airport) && !(nodeNeighbor[1] == this.airport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && !isNodeMapContaingNode(nodeNeighbor, track)){
                        //track.add(nodeNeighbor);
                        if(nodeNeighbor[1].equals(this.targetAirport)){
                            track.add(nodeNeighbor);
                            if(!isFinishedNodeMapContainingNodeMap(track)){
                                //System.out.println("Adding to finishnodlesMap = " + printfNodleMap(track));
                                finishNodesMap.add(new ArrayList<>(track));
                            }
                            track.remove(nodeNeighbor);
                            //break;
                        }
                        else if(!nodeNeighbor[1].equals(this.airport) && !nodeNeighbor[1].equals(track.get(track.size()-1)[1]) && !isNodeMapContaingNode(nodeNeighbor, track)){
                            //if(track.size()>0 && !track.get(track.size()-1)[1].equals(nodeNeighbor[0])){
                            track.add(nodeNeighbor);
                            recursion(track);
                            //track.remove(track.size()-1);
                            track.remove(nodeNeighbor);
                            //}
                        }
                    }
                }
            }
            //nodesToShearch.clear();
            //track.clear();
        }

    }

    boolean isNodeMapContaingNode(Airport[] node, ArrayList<Airport[]> nodlesMap){
        for(Airport[] nodeInMap : nodlesMap){
            if(node[0]==nodeInMap[0] || node[1]==nodeInMap[1]){
                return true;//nodle sa takie same
            }
        }
        return false;
    }

    boolean isFinishedNodeMapContainingNodeMap(ArrayList<Airport[]> nodesMap){
        if(!nodesMap.get(0)[0].equals(this.airport) && nodesMap.get(nodesMap.size()-1)[1].equals(this.targetAirport)){
            return false;
        }
        int count = 0;
        for(ArrayList<Airport[]> mapFromFinishNodesMap : finishNodesMap){
            if(nodesMap.size()==mapFromFinishNodesMap.size()){
                for(Airport[] nodeFromFinishNodesMap : mapFromFinishNodesMap){
                    for(Airport[] nodeFromNodesMap : nodesMap){
                        if(nodeFromFinishNodesMap[0]==nodeFromNodesMap[0] && nodeFromFinishNodesMap[1]==nodeFromNodesMap[1]){
                            count++;
                        }
                    }
                    if(count==nodesMap.size()){
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

    private void fly(Airport targetAirport) throws InterruptedException {
        this.airport = null;
        this.airplaneJLabel.setVisible(true);
        currentFuelJLabel.setVisible(true);
        maxfuelJLabel.setVisible(true);
        final double xx = targetAirport.x - x;
        final double yy = targetAirport.y - y;
        double distance = Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2));
        double xmultiple = ((abs(xx) / abs(Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2)))));
        double ymultiple = ((abs(yy) / abs(Math.sqrt(Math.pow((x - targetAirport.x), 2) + Math.pow((y - targetAirport.y), 2)))));
        currentfuel = fuel;

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
            if (y < targetAirport.y){
                if(targetAirport.y < (y + abs(ymultiple))){
                    y = targetAirport.y;
                }
                else {
                    y = (y + abs(ymultiple));
                }
            }
            if ((x > targetAirport.x)){
                if(targetAirport.x > (x - abs(xmultiple))){
                    x = targetAirport.x;
                }
                else {
                    x = (x - abs(xmultiple));
                }
            }
            if (y > targetAirport.y){
                if(targetAirport.y > (y - abs(ymultiple))){
                    y = targetAirport.y;
                }
                else {
                    y = (y - abs(ymultiple));
                }
            }
            if(currentfuel<0){
                System.out.println(currentfuel);
            }
            currentfuel = currentfuel - Math.sqrt(Math.pow((xBackup - x), 2) + Math.pow((yBackup - y), 2));
            airplanesGUI.updateAirplane(airplaneJLabel, (int) x, (int) y, angle);
            airplanesGUI.updateFuelStatusAirplane(this);
            TimeUnit.MILLISECONDS.sleep(10);
            if(x==targetAirport.x && y==targetAirport.y){
                land(targetAirport);
                break;
            }
        }
    }

    private void land(Airport targetAirport) {
        airplaneJLabel.setVisible(false);
        currentFuelJLabel.setVisible(false);
        maxfuelJLabel.setVisible(false);
        if(targetAirport.equals(this.targetAirport)){
            this.airport = targetAirport;
            this.targetAirport = null;
            this.finishNodesMap.clear();
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
                System.out.println("\n" + name + ": Searching flight: " + this.airport.name + " -> "  + targetAirport.name);
                this.setTarget(targetAirport);
            }
        }
    }
}
