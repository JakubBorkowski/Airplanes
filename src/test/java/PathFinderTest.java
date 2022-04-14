import org.junit.jupiter.api.Test;

import util.Airport;
import util.World;
import pathfinding.PathFinder;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    private World world(){
        World world = new World();
        world.addAirport(101, 319, "Zielona Góra");
        world.addAirport(81, 238, "Gorzów Wielokopolski");
        world.addAirport(44, 163, "Szczecin");
        world.addAirport(301, 71, "Gdańsk");
        world.addAirport(413, 138, "Olsztyn");
        world.addAirport(578, 203, "Białystok");
        world.addAirport(443, 286, "Warszawa");
        world.addAirport(253, 196, "Bydgoszcz");
        world.addAirport(299, 209, "Toruń");
        world.addAirport(189, 272, "Poznań");
        world.addAirport(348, 346, "Łódź");
        world.addAirport(200, 399, "Wrocław");
        world.addAirport(252, 455, "Opole");
        world.addAirport(319, 498, "Katowice");
        world.addAirport(376, 501, "Kraków");
        world.addAirport(516, 517, "Rzeszów");
        world.addAirport(422, 385, "Kielce");
        world.addAirport(545, 393, "Lublin");
        return world;
    }

    //One fuel tests

    @Test
    void oneFuelDFS(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void oneFuelBFS(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void oneFuelDIJKSTRA(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 1.0, world);
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }

    //No fuel tests

    @Test
    void noFuelDFS(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 0.0, world);
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void noFuelBFS(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1), 0.0, world);
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void noFuelDIJKSTRA(){
        World world = world();
        PathFinder pathFinder = new PathFinder(
                world.getAirportsArrayList().get(0), world.getAirportsArrayList().get(1),0.0, world);
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }

    //Infinite fuel tests

    @Test
    void infiniteFuelDFS(){
        World world = world();
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
        World world = world();
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
        World world = world();
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
     * @param world util.World for which minimal required fuel will be calculated.
     * @return Minimal fuel required for travel to every airport in provided world.
     */
    private double calculateMinFuel(World world){
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
        World world = world();
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
        World world = world();
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
        World world = world();
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