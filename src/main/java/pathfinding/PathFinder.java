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
    public Path findPath(String algorithmName){
        Path path = new Path();
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
     * Allows for comparing paths by overall distance that must be travel.
     */
    final Comparator<Path> comparePathByDistance = new Comparator<Path>() {
        @Override
        public int compare(Path path1, Path path2) {
            double distance1 = 0;
            for(Node node : path1){
                distance1 += world.checkDistance(node.getInitialAirport(), node.getFinalAirport());
            }
            double distance2 = 0;
            for(Node node : path2){
                distance2 += world.checkDistance(node.getInitialAirport(), node.getFinalAirport());
            }
            return Double.compare(distance1, distance2);
        }
    };

}