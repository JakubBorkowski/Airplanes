package pathfinding;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pathfinding.model.Path;
import util.Airport;
import util.World;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

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

    @Test
    @DisplayName("Shouldn't find path with '1' fuel - BFS")
    void shouldNotFindPathWithOneFuelBFS(){
        //Given//
        BFS bfs;
        Path path;
        World poland = poland();
        //When//
        bfs = new BFS(poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 1.0, poland);
        path = bfs.startBFS();
        //Then//
        assertNull(path);
    }

    @Test
    @DisplayName("Shouldn't find path with no fuel - BFS")
    void shouldNotFindPathWithNoFuelBFS(){
        //Given//
        BFS bfs;
        Path path;
        World poland = poland();
        //When//
        bfs = new BFS(poland.getAirportsArrayList().get(0), poland.getAirportsArrayList().get(1), 0.0, poland);
        path = bfs.startBFS();
        //Then//
        assertNull(path);
    }

    @Test
    @DisplayName("Should find path with infinite fuel - BFS")
    void shouldFindPathWithInfiniteFuelBFS(){
        //Given//
        BFS bfs;
        Path path;
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        bfs = new BFS(initialAirport, finalAirport, Double.POSITIVE_INFINITY, world);
        path = bfs.startBFS();
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(initialAirport, path.getFirst().getInitialAirport()),
                () ->  assertEquals(finalAirport, path.getLast().getFinalAirport())
        );
    }

    @Test
    @DisplayName("Should find path with minimum required fuel - BFS")
    void shouldFindPathWithMinRequiredFuelBFS(){
        //Given//
        BFS bfs;
        Path path;
        World world = poland();
        Airport initialAirport = world.getAirportsArrayList().get(0);
        Airport finalAirport = world.getAirportsArrayList().get(1);
        //When//
        bfs = new BFS(initialAirport, finalAirport, polandMinFuel, world);
        path = bfs.startBFS();
        //Then//
        assertAll("Should return path from initialAirport to finalAirport",
                () ->  assertEquals(initialAirport, path.getFirst().getInitialAirport()),
                () ->  assertEquals(finalAirport, path.getLast().getFinalAirport())
        );
    }

    @Test
    @DisplayName("Should find path to every airport with minimum required fuel - BFS")
    void shouldFindPathToEveryAirportWithMinRequiredFuelBFS(){
        //Given//
        BFS bfs;
        Path path;
        World world = poland();
        //When//
        boolean notFound = false;
        for(Airport initialAirport : world.getAirportsArrayList()) {
            for (Airport finalAirport : world.getAirportsArrayList()) {
                if(!initialAirport.equals(finalAirport)){
                    bfs = new BFS(initialAirport, finalAirport, polandMinFuel, world);
                    path = bfs.startBFS();
                    if(path == null || !path.getFirst().getInitialAirport().equals(initialAirport) ||
                            !path.getLast().getFinalAirport().equals(finalAirport)){
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