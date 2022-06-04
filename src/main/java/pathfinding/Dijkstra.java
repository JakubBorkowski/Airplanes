package pathfinding;

import org.jetbrains.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import util.Airport;
import util.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Dijkstra's shortest path algorithm
 */
@AllArgsConstructor
public class Dijkstra {
    @NonNull private final Airport initialAirport; //Airport from which path will be searched.
    @NonNull private final Airport finalAirport; //Airport to which path will be searched.
    @NonNull private final Double fuel; //Fuel of the airplane for which the path will be searched for.
    @NonNull private final World world; //World object with information about all airports on the map.

    /**
     * Starts Dijkstra's shortest path algorithm.
     * @return path to finalAirport.
     */
    public Path startDijkstra(){
        ArrayList<Airport> unvisitedAirports = new ArrayList<>(world.getAirportsArrayList());
        ArrayList<DijkstraTable> dijkstraTableArrayList = new ArrayList<>();
        for(Airport airport : world.getAirportsArrayList()){
            if(airport.equals(initialAirport)){
                dijkstraTableArrayList.add(new DijkstraTable(airport, (double) 0, null));
            }
            else {
                dijkstraTableArrayList.add(new DijkstraTable(airport, Double.POSITIVE_INFINITY, null));
            }
        }
        dijkstraTableArrayList.sort(compareByDistanceDijkstraTable);
        while (unvisitedAirports.size() > 0){
            dijkstraTableArrayList.sort(compareByDistanceDijkstraTable);
            for(DijkstraTable dijkstraTable : dijkstraTableArrayList){
                if(unvisitedAirports.contains(dijkstraTable.getAirport())){
                    for(Airport airport : dijkstraTable.getAirport().getNearAirports(fuel)){
                        if(unvisitedAirports.contains(airport)){
                            DijkstraTable dijkstraTable2 = findAirportInDijkstraTable(airport, dijkstraTableArrayList);
                            if(!(dijkstraTable2 == null)){
                                double distanceBetweenAirports = world.checkDistance(dijkstraTable.getAirport(),
                                        airport);
                                if(dijkstraTable.getDistance()+distanceBetweenAirports < dijkstraTable2.getDistance()){
                                    int dijkstraTable2Index = dijkstraTableArrayList.indexOf(dijkstraTable2);
                                    dijkstraTableArrayList.get(dijkstraTable2Index).setDistance(
                                            dijkstraTable.getDistance() + distanceBetweenAirports);
                                    dijkstraTableArrayList.get(dijkstraTable2Index).setPreviousAirport(
                                            dijkstraTable.getAirport());
                                }
                            }
                        }
                    }
                    unvisitedAirports.remove(dijkstraTable.getAirport());
                    break;
                }
            }
        }
        //Checking if path was found//
        int finalAirportIndex = dijkstraTableArrayList.indexOf(
                findAirportInDijkstraTable(finalAirport, dijkstraTableArrayList)
        );
        if(dijkstraTableArrayList.get(finalAirportIndex).getPreviousAirport() == null){
            return null;//Airplane can't reach finalAirport
        }
        //Creating path of airports from dijkstraTableArrayList//
        LinkedList<Airport> path = new LinkedList<>();
        path.add(finalAirport);
        Airport airport = dijkstraTableArrayList.get(finalAirportIndex).getPreviousAirport();
        path.add(airport);
        assert airport != null;
        while (!airport.equals(initialAirport)){
            int airportIndex = dijkstraTableArrayList.indexOf(
                    findAirportInDijkstraTable(airport, dijkstraTableArrayList)
            );
            airport = dijkstraTableArrayList.get(airportIndex).getPreviousAirport();
            path.add(airport);
            assert airport != null;
        }
        Collections.reverse(path);
        //Converting airports to nodes of airports in path//
        Path nodePath = new Path();
        for(Airport airportFromPath : path){
            if(path.size() > path.indexOf(airportFromPath)+1){
                nodePath.add(new Node(airportFromPath, path.get(path.indexOf(airportFromPath)+1)));
            }
        }
        return nodePath;
    }

    /**
     * Compares distance in DijkstraTables.
     */
    private final Comparator<DijkstraTable> compareByDistanceDijkstraTable =
            Comparator.comparingDouble(DijkstraTable::getDistance);

    /**
     * Finds DijkstraTable with specified airport in dijkstraTableArrayList.
     * @param airport that wanted DijkstraTable should contain.
     * @param dijkstraTableArrayList ArrayList with all DijkstraTable objects.
     * @return DijkstraTable with specified airport or null if DijkstraTable was not found.
     */
    private DijkstraTable findAirportInDijkstraTable(Airport airport, ArrayList<DijkstraTable> dijkstraTableArrayList){
        for (DijkstraTable dijkstraTable : dijkstraTableArrayList){
            if (dijkstraTable.getAirport().equals(airport)){
                return(dijkstraTable);
            }
        }
        return(null);
    }

}

/**
 * Row of table used to calculate the shortest path with Dijkstra's algorithm.
 */
@AllArgsConstructor
class DijkstraTable {
    /**
     * Airport for which the shortest distance is sought.
     */
    @NonNull @Getter
    private final Airport airport;
    /**
     * Shortest known overall distance from initialAirport to util.Airport.
     * If not yet calculated, it should have a value of infinity.
     */
    @NonNull @Getter @Setter
    private Double distance;
    /**
     * Airport from which airplane can arrive to airport specified in
     * the same DijkstraTable row with the shortest known overall distance.
     */
    @Nullable @Getter @Setter
    private Airport previousAirport;
}