package pathfinding;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import pathfinding.model.Node;
import pathfinding.model.Path;
import util.Airport;
import util.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Depth-first search algorithm
 */
@AllArgsConstructor
public class DFS {
    @NonNull private final Airport initialAirport;
    @NonNull private final Airport finalAirport;
    @NonNull private final Double fuel;
    @NonNull private final World world;

    /**
     * Starts Depth-first search algorithm.
     *
     * @return Path to finalAirport.
     */
    public Path startDFS() {
        Path path = new Path();
        //If it is possible to travel directly to destination - return appropriate path//
        if (initialAirport.getNearAirports(fuel).contains(finalAirport)) {
            path.add(new Node(initialAirport, finalAirport));
            return path;
        } else {
            LinkedList<Path> allPossiblePaths = new LinkedList<>();
            LinkedList<Node> initialAirportNodes = new LinkedList<>(initialAirport.getNodes(fuel));
            for (Node node : initialAirportNodes) {
                path.add(node);
                allPossiblePaths = recursion(path, allPossiblePaths);
                path.clear();
            }
            if (allPossiblePaths.isEmpty()) {
                return null;
            } else {
                allPossiblePaths.sort(comparePathByDistance);
                path = allPossiblePaths.getFirst();
                return path;
            }
        }
    }

    /**
     * Continue Depth-first search with specified unfinished path.
     *
     * @param path             Currently processed path.
     * @param allPossiblePaths All found path leading to finalAirport.
     * @return Updated allPossiblePaths.
     */
    private LinkedList<Path> recursion(Path path, LinkedList<Path> allPossiblePaths) {
        if (!path.isEmpty()) {
            LinkedList<Node> nodesToSearch = new LinkedList<>();//backup
            ArrayList<Node> possibleNextNodes = new ArrayList<>(path.getLast().getFinalAirport().getNodes(fuel));
            for (Node nodeNeighbor : possibleNextNodes) {
                if (!path.contains(nodeNeighbor) && !path.contains(nodeNeighbor.getFinalAirport())) {
                    if (nodeNeighbor.getFinalAirport().equals(finalAirport)) {
                        path.add(nodeNeighbor);
                        if (!allPossiblePaths.contains(path)) {
                            allPossiblePaths.add(new Path(path));
                        }
                        path.remove(nodeNeighbor);
                    } else {
                        nodesToSearch.add(nodeNeighbor);
                    }
                }
            }
            for (Node nodeNeighbor : nodesToSearch) {
                if (!path.isEmpty()) {
                    if (!path.contains(nodeNeighbor) && !path.contains(nodeNeighbor.getFinalAirport())) {
                        if (nodeNeighbor.getFinalAirport().equals(finalAirport)) {
                            path.add(nodeNeighbor);
                            if (!allPossiblePaths.contains(path)) {
                                allPossiblePaths.add(new Path(path));
                            }
                            path.remove(nodeNeighbor);
                            break;
                        } else {
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

    /**
     * Allows for comparing paths by overall distance that must be travel.
     */
    private final Comparator<Path> comparePathByDistance = new Comparator<Path>() {
        @Override
        public int compare(Path path1, Path path2) {
            double distance1 = 0;
            for (Node node : path1) {
                distance1 += world.checkDistance(node.getInitialAirport(), node.getFinalAirport());
            }
            double distance2 = 0;
            for (Node node : path2) {
                distance2 += world.checkDistance(node.getInitialAirport(), node.getFinalAirport());
            }
            return Double.compare(distance1, distance2);
        }
    };

}
