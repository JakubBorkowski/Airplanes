package pathfinding;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import pathfinding.model.Path;
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
     *
     * @param algorithmName Name of algorithm which should be used. Available names: "BFS", "DFS", "DIJKSTRA".
     * @return Path from initialAirport to finalAirport, if there is one.
     */
    public Path findPath(String algorithmName) {
        Path path = new Path();
        switch (algorithmName.toUpperCase(Locale.ROOT)) {
            case "BFS":
                BFS bfs = new BFS(initialAirport, finalAirport, fuel);
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

}