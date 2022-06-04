package pathfinding;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import util.Airport;
import util.World;


import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    private final double polandMinFuel = 160.0;

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
     * @param world World for which minimal required fuel will be calculated.
     * @return Minimal fuel required for travel to every airport in provided world.
     */
    private double calculateMinRequiredFuel(@NotNull World world){
        PathFinder pathFinder;
        Double fuel = 0.0;
        for(Airport airport1 : world.getAirportsArrayList()){
            for(Airport airport2 : world.getAirportsArrayList()){
                if(!(airport1.getName().equals(airport2.getName()))){
                    pathFinder = new PathFinder(airport1, airport2, fuel, world);
                    while (pathFinder.findPath("DIJKSTRA") == null){
                        pathFinder = new PathFinder(airport1, airport2, fuel++, world);
                    }
                }
            }
        }
        System.out.println(fuel);
        return fuel;
    }


    //Finding path with one fuel tests


    @Test
    @DisplayName("Shouldn't find path with '1' fuel - DFS")
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
    @DisplayName("Shouldn't find path with '1' fuel - BFS")
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
    @DisplayName("Shouldn't find path with '1' fuel - Dijkstra")
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
    @DisplayName("Shouldn't find path with no fuel - DFS")
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
    @DisplayName("Shouldn't find path with no fuel - BFS")
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
    @DisplayName("Shouldn't find path with no fuel - Dijkstra")
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
    @DisplayName("Should find path with infinite fuel - DFS")
    void shouldFindPathWithInfiniteFuelDFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        Path foundPath = pathFinder.findPath("DFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }

    @Test
    @DisplayName("Should find path with infinite fuel - BFS")
    void shouldFindPathWithInfiniteFuelBFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        Path foundPath = pathFinder.findPath("BFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }

    @Test
    @DisplayName("Should find path with infinite fuel - Dijkstra")
    void shouldFindPathWithInfiniteFuelDIJKSTRA(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        Path foundPath = pathFinder.findPath("DIJKSTRA");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }


    //Finding path with minimal required fuel tests


    @Test
    @DisplayName("Should find path with minimum required fuel - DFS")
    void shouldFindPathWithMinRequiredFuelDFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
        Path foundPath = pathFinder.findPath("DFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }

    @Test
    @DisplayName("Should find path with minimum required fuel - BFS")
    void shouldFindPathWithMinRequiredFuelBFS(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
        Path foundPath = pathFinder.findPath("BFS");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }

    @Test
    @DisplayName("Should find path with minimum required fuel - Dijkstra")
    void shouldFindPathWithMinRequiredFuelDIJKSTRA(){
        //Given//
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        PathFinder pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
        Path foundPath = pathFinder.findPath("DIJKSTRA");
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(foundPath.getFirst().getInitialAirport(), initialAirport),
                () ->  assertEquals(foundPath.getLast().getFinalAirport(), finalAirport)
        );
    }

    @Test
    @DisplayName("Should find path to every airport with minimum required fuel - DFS")
    void shouldFindPathToEveryAirportWithMinRequiredFuelDFS(){
        //Given//
        World world = poland();
        PathFinder pathFinder;
        Path foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
                    foundPath = pathFinder.findPath("DFS");
                    if (foundPath == null || (!foundPath.getFirst().getInitialAirport().equals(initialAirport) ||
                            !foundPath.getLast().getFinalAirport().equals(finalAirport))) {
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
    @DisplayName("Should find path to every airport with minimum required fuel - BFS")
    void shouldFindPathToEveryAirportWithMinRequiredFuelBFS(){
        //Given//
        World world = poland();
        PathFinder pathFinder;
        Path foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
                    foundPath = pathFinder.findPath("BFS");
                    if(foundPath == null || !foundPath.getFirst().getInitialAirport().equals(initialAirport) ||
                            !foundPath.getLast().getFinalAirport().equals(finalAirport)){
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
    @DisplayName("Should find path to every airport with minimum required fuel - Dijkstra")
    void shouldFindPathToEveryAirportWithMinRequiredFuelDIJKSTRA(){
        //Given//
        World world = poland();
        PathFinder pathFinder;
        Path foundPath;
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    pathFinder = new PathFinder(initialAirport, finalAirport, polandMinFuel, world);
                    foundPath = pathFinder.findPath("DIJKSTRA");
                    if(foundPath == null || !foundPath.getFirst().getInitialAirport().equals(initialAirport) ||
                            !foundPath.getLast().getFinalAirport().equals(finalAirport)){
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