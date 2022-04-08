import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;

public class PathFinder {
    @NonNull private final World world;
    @NonNull private final Double fuel;
    @NonNull private final Airport initialAirport;
    @NonNull private final Airport finalAirport;

    /**
     * Creates a PathFinder. The PathFinder allows you to find the shortest path between two airports
     * with one of the chosen algorithms.
     * @param initialAirport Airport from which path will be searched.
     * @param finalAirport Airport to which path will be searched.
     * @param fuel Fuel of the airplane for which the path will be searched for.
     * @param world World object with information about all airports on the map.
     */
    PathFinder(Airport initialAirport, Airport finalAirport, Double fuel, World world){
        this.world = world;
        this.fuel = fuel;
        this.initialAirport = initialAirport;
        this.finalAirport = finalAirport;
    }

    /**
     * Starts the process of finding a path from initialAirport to finalAirport with one of the chosen algorithms.
     * @param algorithmName Name of algorithm which should be used. Available names: "BFS", "DFS", "DIJKSTRA".
     * @return Path from initialAirport to finalAirport, if there is one.
     */
    public LinkedList<Airport[]> findPath(String algorithmName){
        LinkedList<Airport[]> path = new LinkedList<>();
        switch (algorithmName.toUpperCase(Locale.ROOT)) {
            case "BFS":
                path = startBFS();
                break;
            case "DFS":
                path = startDFS();
                break;
            case "DIJKSTRA":
                path = startDijkstra();
                break;
        }
        return path;
    }

    /**
     * Checks if the specified path does not contain the specified node.
     * @param node Array of 2 airports, from which you can fly to each other [from Airport1 -> to Airport2]
     * @param path LinkedList of arrays containing nodes. Every node should contain exactly 2 airports.
     *              The second airport in the array should be the same as the first one in the next node.
     *              [Airport1, Airport2], [Airport2, Airport3]...
     * @return true if specified path not contain specified node.
     */
    private boolean pathNotContainNode(Airport[] node, LinkedList<Airport[]> path){
        for(Airport[] nodeInPath : path){
            if(node[0]==nodeInPath[0] || node[1]==nodeInPath[1]){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified path does not contain the specified node.
     * @param path LinkedList of arrays containing nodes. Every node should contain exactly 2 airports.
     *              The second airport in the array should be the same as the first one in the next node.
     *              [Airport1, Airport2], [Airport2, Airport3]...
     * @param allPossiblePaths LinkedList of multiple paths.
     * @return true if allPossiblePaths not contains path.
     */
    private boolean allPossiblePathsNotContainPath(LinkedList<Airport[]> path,
                                                     LinkedList<LinkedList<Airport[]>> allPossiblePaths){
        if(!path.get(0)[0].equals(initialAirport) && path.get(path.size()-1)[1].equals(finalAirport)){
            return true;
        }
        int count = 0;
        for(LinkedList<Airport[]> pathFromAllPossiblePaths : allPossiblePaths){
            if(path.size()==pathFromAllPossiblePaths.size()){
                for(Airport[] nodeFromAllPossiblePaths : pathFromAllPossiblePaths){
                    for(Airport[] nodeFromPossiblePath : path){
                        if(nodeFromAllPossiblePaths[0]==nodeFromPossiblePath[0]
                                && nodeFromAllPossiblePaths[1]==nodeFromPossiblePath[1]){
                            count++;
                        }
                    }
                    if(count==path.size()){
                        return false;
                    }
                    count = 0;
                }
            }
        }
        return true;
    }

    /**
     * Allows for comparing paths by overall distance that must be travel.
     */
    private final Comparator<LinkedList<Airport[]>> compareByDistancePath = new Comparator<LinkedList<Airport[]>>() {
        @Override
        public int compare(LinkedList<Airport[]> path1, LinkedList<Airport[]> path2) {
            double distance1 = 0;
            for(Airport[] node : path1){
                distance1 += world.checkDistance(node[0], node[1]);
            }
            double distance2 = 0;
            for(Airport[] node : path2){
                distance2 += world.checkDistance(node[0], node[1]);
            }
            return Double.compare(distance1, distance2);
        }
    };

    //
    //Depth-first search
    //

    /**
     * Starts Depth-first search algorithm.
     * @return path to finalAirport.
     */
    private LinkedList<Airport[]> startDFS(){
        LinkedList<Airport[]> path = new LinkedList<>();
        ///If it is possible to travel directly to destination - return appropriate path//
        if(initialAirport.getNearAirports(fuel).contains(finalAirport)){
            path.add(new Airport[]{initialAirport, finalAirport});
            return path;
        }
        LinkedList<LinkedList<Airport[]>> allPossiblePaths = new LinkedList<>();
        ArrayList<Airport[]> thisAirportNodes = new ArrayList<>(initialAirport.getNodes(fuel));
        for(Airport[] node : thisAirportNodes){
            if(node[1].equals(finalAirport)){
                path.add(node);
                allPossiblePaths.add(path);
                break;
            } else {
                path.add(node);
                allPossiblePaths = recursion(path, allPossiblePaths);
            }
            path.clear();
        }
        if(allPossiblePaths.isEmpty()){
            return null;
        }
        else {
            allPossiblePaths.sort(compareByDistancePath);
            path = allPossiblePaths.getFirst();
            return path;
        }
    }

    /**
     * Continue Depth-first search with specified unfinished path.
     * @param path currently processed path.
     * @param allPossiblePaths all found path leading to finalAirport.
     * @return updated allPossiblePaths.
     */
    private LinkedList<LinkedList<Airport[]>> recursion(LinkedList<Airport[]> path,
                                                        LinkedList<LinkedList<Airport[]>> allPossiblePaths){
        LinkedList<Airport[]> nodesToSearch = new LinkedList<>();//backup
        if(path.size()>0){
            path.get(path.size()-1)[1].getNodes(fuel);
            ArrayList<Airport[]> node1GetNodes = new ArrayList<>(path.get(path.size()-1)[1].getNodes(fuel));
            for(Airport[] nodeNeighbor : node1GetNodes){
                if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) &&
                        !(nodeNeighbor[1] == path.get(path.size()-1)[0]) && pathNotContainNode(nodeNeighbor, path)){
                    if(nodeNeighbor[1].equals(finalAirport)){
                        path.add(nodeNeighbor);
                        if(allPossiblePathsNotContainPath(path, allPossiblePaths)){
                            allPossiblePaths.add(new LinkedList<>(path));
                        }
                        path.remove(nodeNeighbor);
                        break;
                    }
                    else {
                        nodesToSearch.add(nodeNeighbor);
                    }
                }
            }
            for(Airport[] nodeNeighbor : nodesToSearch){
                if(path.size()>0){
                    if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) &&
                            !(nodeNeighbor[1] == path.get(path.size()-1)[0]) &&
                            pathNotContainNode(nodeNeighbor, path)){
                        if(nodeNeighbor[1].equals(this.finalAirport)){
                            path.add(nodeNeighbor);
                            if(allPossiblePathsNotContainPath(path, allPossiblePaths)){
                                allPossiblePaths.add(new LinkedList<>(path));
                            }
                            path.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeNeighbor[1].equals(initialAirport) &&
                                !nodeNeighbor[1].equals(path.get(path.size()-1)[1]) &&
                                pathNotContainNode(nodeNeighbor, path)){
                            path.add(nodeNeighbor);
                            allPossiblePaths = recursion(path, allPossiblePaths);
                            path.remove(nodeNeighbor);
                        }
                    }
                }
            }
        }
        return allPossiblePaths;
    }

    //
    //Breadth-first search
    //

    /**
     * Starts Breadth-first search.
     * @return path to finalAirport.
     */
    private LinkedList<Airport[]> startBFS(){
        LinkedList<LinkedList<Airport[]>> queue = new LinkedList<>();
        LinkedList<Airport[]> path = new LinkedList<>();
        for(Airport[] node : initialAirport.getSortedNodes(fuel)){
            path.add(node);
            if(node[1].equals(finalAirport)){
                return path;
            }
            queue.add(new LinkedList<>(path));
            path.clear();
        }
        for(int i = 0; i < queue.size(); i++) {
            path = queue.get(i);
            ArrayList<Airport[]> path3Nodes = path.getLast()[1].getSortedNodes(fuel);
            for(Airport[] node3 : path3Nodes){
                if(!(node3[0] == initialAirport) && !(node3[1] == initialAirport) &&
                        !(node3[1] == path.getLast()[0]) && pathNotContainNode(node3, path)){
                    path.add(node3);
                    if(path.getLast()[1].equals(finalAirport)){
                        return path;
                    }
                    else {
                        queue.add(new LinkedList<>(path));
                    }
                    path.remove(node3);
                }
            }
        }
        return null;
    }

    //
    // Dijkstra's algorithm
    //

    /**
     * Starts Dijkstra's shortest path algorithm.
     * @return path to finalAirport.
     */
    private LinkedList<Airport[]> startDijkstra(){
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
        while (!airport.equals(initialAirport)){
            int airportIndex = dijkstraTableArrayList.indexOf(
                    findAirportInDijkstraTable(airport, dijkstraTableArrayList)
            );
            airport = dijkstraTableArrayList.get(airportIndex).getPreviousAirport();
            path.add(airport);
        }
        Collections.reverse(path);
        //Converting airports to nodes of airports in path//
        LinkedList<Airport[]> nodePath = new LinkedList<>();
        for(Airport airportFromPath : path){
            if(path.size() > path.indexOf(airportFromPath)+1){
                nodePath.add(new Airport[]{airportFromPath, path.get(path.indexOf(airportFromPath)+1)});
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
    @NonNull @Getter private final Airport airport;
    /**
     * Shortest known overall distance from initialAirport to Airport.
     * If not yet calculated, it should have a value of infinity.
     */
    @NonNull @Getter @Setter private Double distance;
    /**
     * Airport from which airplane can arrive to airport specified in
     * the same DijkstraTable row with the shortest known overall distance.
     */
    @Nullable @Getter @Setter private Airport previousAirport;
}