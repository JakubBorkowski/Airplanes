package pathfinding;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import util.Airport;
import util.World;

import java.util.*;

/**
 * PathFinder allows you to find the shortest path
 * between two airports with one of the chosen algorithms.
 */
@AllArgsConstructor
public class PathFinder {
    @NonNull final Airport initialAirport; //Airport from which path will be searched.
    @NonNull final Airport finalAirport; //Airport to which path will be searched.
    @NonNull final Double fuel; //Fuel of the airplane for which the path will be searched for.
    @NonNull final World world; //World object with information about all airports on the map.

    /**
     * Starts the process of finding a path from initialAirport to finalAirport with one of the chosen algorithms.
     * @param algorithmName Name of algorithm which should be used. Available names: "BFS", "DFS", "DIJKSTRA".
     * @return Path from initialAirport to finalAirport, if there is one.
     */
    public LinkedList<Airport[]> findPath(String algorithmName){
        LinkedList<Airport[]> path = new LinkedList<>();
        switch (algorithmName.toUpperCase(Locale.ROOT)) {
            case "BFS":
                BFS bfs = new BFS(initialAirport, finalAirport, fuel, world);
                path = bfs.startBFS();
                break;
            case "DFS":
                DFS dfs = new DFS(initialAirport, finalAirport, fuel, world);
                path = dfs.startDFS();
                break;
            case "DIJKSTRA":
                Dijkstra dijkstra = new Dijkstra(initialAirport, finalAirport, fuel, world);
                path = dijkstra.startDijkstra();
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
    boolean isPathNotContainingNode(Airport[] node, LinkedList<Airport[]> path){
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
    boolean isAllPossiblePathsNotContainingPath(LinkedList<Airport[]> path,
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
    final Comparator<LinkedList<Airport[]>> compareByDistancePath = new Comparator<LinkedList<Airport[]>>() {
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

}