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
        LinkedList<Airport[]> track = new LinkedList<>();
        switch (algorithmName.toUpperCase(Locale.ROOT)) {
            case "BFS":
                track = startBFS();
                break;
            case "DFS":
                track = startDFS();
                break;
            case "DIJKSTRA":
                track = startDijkstra();
                break;
        }
        return track;
    }

    /**
     * Checks if the specified path does not contain the specified node.
     * @param node Array of 2 airports, from which you can fly to each other [from Airport1 -> to Airport2]
     * @param track LinkedList of arrays containing nodes. Every node should contain exactly 2 airports.
     *              The second airport in the array should be the same as the first one in the next node.
     *              [Airport1, Airport2], [Airport2, Airport3]...
     * @return true if specified track not contain specified node.
     */
    private boolean trackNotContainNode(Airport[] node, LinkedList<Airport[]> track){
        for(Airport[] nodeInTrack : track){
            if(node[0]==nodeInTrack[0] || node[1]==nodeInTrack[1]){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified path does not contain the specified node.
     * @param track LinkedList of arrays containing nodes. Every node should contain exactly 2 airports.
     *              The second airport in the array should be the same as the first one in the next node.
     *              [Airport1, Airport2], [Airport2, Airport3]...
     * @param allPossibleTracks LinkedList of multiple tracks.
     * @return true if allPossibleTracks not contains track.
     */
    private boolean allPossibleTracksNotContainTrack(LinkedList<Airport[]> track,
                                                     LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        if(!track.get(0)[0].equals(initialAirport) && track.get(track.size()-1)[1].equals(finalAirport)){
            return true;
        }
        int count = 0;
        for(LinkedList<Airport[]> trackFromAllPossibleTracks : allPossibleTracks){
            if(track.size()==trackFromAllPossibleTracks.size()){
                for(Airport[] nodeFromAllPossibleTracks : trackFromAllPossibleTracks){
                    for(Airport[] nodeFromPossibleTrack : track){
                        if(nodeFromAllPossibleTracks[0]==nodeFromPossibleTrack[0]
                                && nodeFromAllPossibleTracks[1]==nodeFromPossibleTrack[1]){
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

    /**
     * Allows for comparing tracks by overall distance that must be travel.
     */
    private final Comparator<LinkedList<Airport[]>> compareByDistanceTrack = new Comparator<LinkedList<Airport[]>>() {
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

    //
    //Depth-first search
    //

    /**
     * Starts Depth-first search algorithm.
     * @return track to finalAirport.
     */
    private LinkedList<Airport[]> startDFS(){
        LinkedList<Airport[]> track = new LinkedList<>();
        ///If it is possible to travel directly to destination - return appropriate track//
        if(initialAirport.getNearAirports(fuel).contains(finalAirport)){
            track.add(new Airport[]{initialAirport, finalAirport});
            return track;
        }
        LinkedList<LinkedList<Airport[]>> allPossibleTracks = new LinkedList<>();
        ArrayList<Airport[]> thisAirportNodes = new ArrayList<>(initialAirport.getNodes(fuel));
        for(Airport[] node : thisAirportNodes){
            if(node[1].equals(finalAirport)){
                track.add(node);
                allPossibleTracks.add(track);
                break;
            } else {
                track.add(node);
                allPossibleTracks = recursion(track, allPossibleTracks);
            }
            track.clear();
        }
        if(allPossibleTracks.isEmpty()){
            return null;
        }
        else {
            allPossibleTracks.sort(compareByDistanceTrack);
            track = allPossibleTracks.getFirst();
            return track;
        }
    }

    /**
     * Continue Depth-first search with specified unfinished track.
     * @param track currently processed track.
     * @param allPossibleTracks all found track leading to finalAirport.
     * @return updated allPossibleTracks.
     */
    private LinkedList<LinkedList<Airport[]>> recursion(LinkedList<Airport[]> track,
                                                        LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        LinkedList<Airport[]> nodesToSearch = new LinkedList<>();//backup
        if(track.size()>0){
            track.get(track.size()-1)[1].getNodes(fuel);
            ArrayList<Airport[]> node1GetNodes = new ArrayList<>(track.get(track.size()-1)[1].getNodes(fuel));
            for(Airport[] nodeNeighbor : node1GetNodes){
                if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) &&
                        !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && trackNotContainNode(nodeNeighbor, track)){
                    if(nodeNeighbor[1].equals(finalAirport)){
                        track.add(nodeNeighbor);
                        if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                            allPossibleTracks.add(new LinkedList<>(track));
                        }
                        track.remove(nodeNeighbor);
                        break;
                    }
                    else {
                        nodesToSearch.add(nodeNeighbor);
                    }
                }
            }
            for(Airport[] nodeNeighbor : nodesToSearch){
                if(track.size()>0){
                    if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) &&
                            !(nodeNeighbor[1] == track.get(track.size()-1)[0]) &&
                            trackNotContainNode(nodeNeighbor, track)){
                        if(nodeNeighbor[1].equals(this.finalAirport)){
                            track.add(nodeNeighbor);
                            if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                                allPossibleTracks.add(new LinkedList<>(track));
                            }
                            track.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeNeighbor[1].equals(initialAirport) &&
                                !nodeNeighbor[1].equals(track.get(track.size()-1)[1]) &&
                                trackNotContainNode(nodeNeighbor, track)){
                            track.add(nodeNeighbor);
                            allPossibleTracks = recursion(track, allPossibleTracks);
                            track.remove(nodeNeighbor);
                        }
                    }
                }
            }
        }
        return allPossibleTracks;
    }

    //
    //Breadth-first search
    //

    /**
     * Starts Breadth-first search.
     * @return track to finalAirport.
     */
    private LinkedList<Airport[]> startBFS(){
        LinkedList<LinkedList<Airport[]>> queue = new LinkedList<>();
        LinkedList<Airport[]> track = new LinkedList<>();
        for(Airport[] node : initialAirport.getSortedNodes(fuel)){
            track.add(node);
            if(node[1].equals(finalAirport)){
                return track;
            }
            queue.add(new LinkedList<>(track));
            track.clear();
        }
        for(int i = 0; i < queue.size(); i++) {
            track = queue.get(i);
            ArrayList<Airport[]> track3Nodes = track.getLast()[1].getSortedNodes(fuel);
            for(Airport[] node3 : track3Nodes){
                if(!(node3[0] == initialAirport) && !(node3[1] == initialAirport) &&
                        !(node3[1] == track.getLast()[0]) && trackNotContainNode(node3, track)){
                    track.add(node3);
                    if(track.getLast()[1].equals(finalAirport)){
                        return track;
                    }
                    else {
                        queue.add(new LinkedList<>(track));
                    }
                    track.remove(node3);
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
     * @return track to finalAirport.
     */
    private LinkedList<Airport[]> startDijkstra(){
        ArrayList<Airport> unvisitedAirports = new ArrayList<>(world.airportsArrayList);
        ArrayList<DijkstraTable> dijkstraTableArrayList = new ArrayList<>();
        for(Airport airport : world.airportsArrayList){
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