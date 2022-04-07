import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    //One fuel tests

    @Test
    void oneFuelDFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 1, 1, "DFS");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void oneFuelBFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 1, 1, "BFS");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void oneFuelDIJKSTRA(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 1, 1, "DIJKSTRA");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }

    //No fuel tests

    @Test
    void noFuelDFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 0, 0, "DFS");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 0.0, world);
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void noFuelBFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 0, 0, "BFS");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 0.0, world);
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void noFuelDIJKSTRA(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0, 0, 0, "DIJKSTRA");
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1),0.0, world);
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }

    //Infinite fuel tests

    @Test
    void infiniteFuelDFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "DFS");
        PathFinder pathFinder = new PathFinder(world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1),
                Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundTrack = new LinkedList<>(pathFinder.findPath("DFS"));
        LinkedList<Airport[]> track = new LinkedList<>();
        track.add(new Airport[]{world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1)});
        boolean equal = false;
        if(track.size()==foundTrack.size()){
            equal = true;
            for(int i=0;i<track.size();i++){
                if (!(track.get(i)[0].getName().equals(foundTrack.get(i)[0].getName())) &&
                        !(track.get(i)[1].getName().equals(foundTrack.get(i)[1].getName()))) {
                    equal = false;
                    break;
                }
            }
        }
        assertTrue(equal);
    }

    @Test
    void infiniteFuelBFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "BFS");
        PathFinder pathFinder = new PathFinder(world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1),
                Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundTrack = new LinkedList<>(pathFinder.findPath("BFS"));
        LinkedList<Airport[]> track = new LinkedList<>();
        track.add(new Airport[]{world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1)});
        boolean equal = false;
        if(track.size()==foundTrack.size()){
            equal = true;
            for(int i=0;i<track.size();i++){
                if (!(track.get(i)[0].getName().equals(foundTrack.get(i)[0].getName())) &&
                        !(track.get(i)[1].getName().equals(foundTrack.get(i)[1].getName()))) {
                    equal = false;
                    break;
                }
            }
        }
        assertTrue(equal);
    }

    @Test
    void infiniteFuelDIJKSTRA(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "DIJKSTRA");
        PathFinder pathFinder = new PathFinder(world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1),
                Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundTrack = new LinkedList<>(pathFinder.findPath("DIJKSTRA"));
        LinkedList<Airport[]> track = new LinkedList<>();
        track.add(new Airport[]{world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1)});
        boolean equal = false;
        if(track.size()==foundTrack.size()){
            equal = true;
            for(int i=0;i<track.size();i++){
                if (!(track.get(i)[0].getName().equals(foundTrack.get(i)[0].getName())) &&
                        !(track.get(i)[1].getName().equals(foundTrack.get(i)[1].getName()))) {
                    equal = false;
                    break;
                }
            }
        }
        assertTrue(equal);
    }

    //Minimal required fuel tests

    /**
     * Finds minimal fuel which will allow for flying to every airport in provided world.
     * @param world World for which minimal required fuel will be calculated.
     * @return Minimal fuel required for travel to every airport in provided world.
     */
    double calculateMinFuel(World world){
        LinkedList<Double> distanceBetweenAirports = new LinkedList<>();
        for(Airport airport1 : world.getAirportsArrayList()){
            for(Airport airport2 : world.getAirportsArrayList()){
                if(!(airport1.getName().equals(airport2.getName()))){
                    distanceBetweenAirports.add(world.checkDistance(airport1, airport2));
                }
            }
        }
        Collections.sort(distanceBetweenAirports);
        double minFuel;
        int i=0;
        int numberOfAirplanesWithNeighbor;
        do {
            numberOfAirplanesWithNeighbor = 0;
            minFuel = distanceBetweenAirports.get(i);
            for (Airport airport : world.getAirportsArrayList()) {
                if (airport.getNearAirports(minFuel).isEmpty()) {
                    i++;
                    break;
                } else {
                    numberOfAirplanesWithNeighbor++;
                }
            }
        } while (numberOfAirplanesWithNeighbor != world.getAirportsArrayList().size());
        return minFuel;
    }

    @Test
    void minRequiredFuelDFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "DFS");
        //Finding minimal fuel which will allow for flying to every airport//
        double minFuel = calculateMinFuel(world);
        //Checks if path was found to every airport//
        boolean notFound = false;
        for(Airport airport1 : world.getAirportsArrayList()){
            for(Airport airport2 : world.getAirportsArrayList()){
                if(!airport1.getName().equals(airport2.getName())){
                    PathFinder pathFinder = new PathFinder(airport1, airport2, minFuel, world);
                    if(pathFinder.findPath("DFS")==null){
                        notFound=true;
                        break;
                    }
                }
            }
        }
        assertFalse(notFound);
    }

    @Test
    void minRequiredFuelBFS(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "BFS");
        //Finding minimal fuel which will allow for flying to every airport//
        double minFuel = calculateMinFuel(world);
        //Checks if path was found to every airport//
        boolean notFound = false;
        for(Airport airport1 : world.getAirportsArrayList()){
            for(Airport airport2 : world.getAirportsArrayList()){
                if(!airport1.getName().equals(airport2.getName())){
                    PathFinder pathFinder = new PathFinder(airport1, airport2, minFuel, world);
                    if(pathFinder.findPath("BFS")==null){
                        notFound=true;
                        break;
                    }
                }
            }
        }
        assertFalse(notFound);
    }

    @Test
    void minRequiredFuelDIJKSTRA(){
        AirplanesGUI airplanesGUI = new AirplanesGUI();
        World world = new World(airplanesGUI, 0,
                (int) Double.POSITIVE_INFINITY, (int)  Double.POSITIVE_INFINITY, "DIJKSTRA");
        //Finding minimal fuel which will allow for flying to every airport//
        double minFuel = calculateMinFuel(world);
        //Checks if path was found to every airport//
        boolean notFound = false;
        for(Airport airport1 : world.getAirportsArrayList()){
            for(Airport airport2 : world.getAirportsArrayList()){
                if(!airport1.getName().equals(airport2.getName())){
                    PathFinder pathFinder = new PathFinder(airport1, airport2, minFuel, world);
                    if(pathFinder.findPath("DIJKSTRA")==null){
                        notFound=true;
                        break;
                    }
                }
            }
        }
        assertFalse(notFound);
    }
}