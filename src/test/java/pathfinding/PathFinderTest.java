package pathfinding;

import org.junit.jupiter.api.Test;

import util.Airport;
import util.World;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    /**
     * @return World with all airports in Poland
     */
    private World poland(){
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

    /**
     * Finds minimal fuel which will allow for flying to every airport in provided world.
     * @param world util.World for which minimal required fuel will be calculated.
     * @return Minimal fuel required for travel to every airport in provided world.
     */
    private double calculateMinRequiredFuel(World world){
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


    //Finding path with one fuel tests


    @Test
    void shouldNotFindPathWithOneFuelDFS(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 1.0, poland);
        //Then//
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void shouldNotFindPathWithOneFuelBFS(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 1.0, poland);
        //Then//
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void shouldNotFindPathWithOneFuelDIJKSTRA(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 1.0, poland);
        //Then//
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }


    //Finding path with no fuel tests


    @Test
    void shouldNotFindPathWithNoFuelDFS(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 0.0, poland);
        //Then//
        assertNull(pathFinder.findPath("DFS"));
    }

    @Test
    void shouldNotFindPathWithNoFuelBFS(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 0.0, poland);
        //Then//
        assertNull(pathFinder.findPath("BFS"));
    }

    @Test
    void shouldNotFindPathWithNoFuelDIJKSTRA(){
        //Given//
        World poland = poland();
        //When//
        PathFinder pathFinder = new PathFinder(
                poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 0.0, poland);
        //Then//
        assertNull(pathFinder.findPath("DIJKSTRA"));
    }


    //Finding path with infinite fuel tests


    @Test
    void shouldFindPathWithInfiniteFuelDFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("DFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }

    @Test
    void shouldFindPathWithInfiniteFuelBFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("BFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }

    @Test
    void shouldFindPathWithInfiniteFuelDIJKSTRA(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("DIJKSTRA");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }


    //Finding path with minimal required fuel tests


    @Test
    void shouldFindPathWithMinRequiredFuelDFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        double minFuel = calculateMinRequiredFuel(world);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("DFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }

    @Test
    void shouldFindPathWithMinRequiredFuelBFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        double minFuel = calculateMinRequiredFuel(world);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("BFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }

    @Test
    void shouldFindPathWithMinRequiredFuelDIJKSTRA(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        double minFuel = calculateMinRequiredFuel(world);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
        LinkedList<Airport[]> foundPath = pathFinder.findPath("DIJKSTRA");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst()[0], initialAirport),
                () ->  assertEquals(foundPath.getLast()[1], finalAirport)
        );
    }

    @Test
    void shouldFindPathToEveryAirportWithMinRequiredFuelDFS(){
        //Given//
        World world = poland();
        double minFuel = calculateMinRequiredFuel(world);
        PathFinder pathFinder;
        LinkedList<Airport[]> foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
                    foundPath = pathFinder.findPath("DFS");
                    if(!foundPath.getFirst()[0].equals(initialAirport) || !foundPath.getLast()[1].equals(finalAirport)){
                        notFound = true;
                        break;
                    }
                }
            }
        }
        //Then//
        assertFalse(notFound);
    }

    @Test
    void shouldFindPathToEveryAirportWithMinRequiredFuelBFS(){
        //Given//
        World world = poland();
        double minFuel = calculateMinRequiredFuel(world);
        PathFinder pathFinder;
        LinkedList<Airport[]> foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
                    foundPath = pathFinder.findPath("BFS");
                    if(!foundPath.getFirst()[0].equals(initialAirport) || !foundPath.getLast()[1].equals(finalAirport)){
                        notFound = true;
                        break;
                    }
                }
            }
        }
        //Then//
        assertFalse(notFound);
    }

    @Test
    void shouldFindPathToEveryAirportWithMinRequiredFuelDIJKSTRA(){
        //Given//
        World world = poland();
        double minFuel = calculateMinRequiredFuel(world);
        PathFinder pathFinder;
        LinkedList<Airport[]> foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, minFuel, world);
                    foundPath = pathFinder.findPath("DIJKSTRA");
                    if(!foundPath.getFirst()[0].equals(initialAirport) || !foundPath.getLast()[1].equals(finalAirport)){
                        notFound = true;
                        break;
                    }
                }
            }
        }
        //Then//
        assertFalse(notFound);
    }
}