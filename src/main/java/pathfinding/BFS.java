package pathfinding;

import pathfinding.model.Node;
import pathfinding.model.Path;
import util.Airport;
import util.World;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-first search algorithm
 */
public class BFS extends PathFinder {

    public BFS(Airport initialAirport, Airport finalAirport, Double fuel, World world) {
        super(initialAirport, finalAirport, fuel, world);
    }

    /**
     * Starts Breadth-first search.
     *
     * @return Path to finalAirport.
     */
    public Path startBFS() {
        LinkedList<Path> queue = new LinkedList<>();
        Path path = new Path();
        for (Node initialNode : initialAirport.getSortedNodes(fuel)) {
            path.add(initialNode);
            if (initialNode.getFinalAirport().equals(finalAirport)) {
                return path;
            } else {
                queue.add(new Path(path));
                path.clear();
            }
        }
        for (int i = 0; i < queue.size(); i++) {
            path = queue.get(i);
            ArrayList<Node> nodesOfLastAirportInPath = path.getLast().getFinalAirport().getSortedNodes(fuel);
            for (Node node : nodesOfLastAirportInPath) {
                if (!(node.getInitialAirport() == initialAirport) && !(node.getFinalAirport() == initialAirport) &&
                        !(node.getFinalAirport() == path.getLast().getInitialAirport()) && !path.contains(node)) {
                    path.add(node);
                    if (path.getLast().getFinalAirport().equals(finalAirport)) {
                        return path;
                    } else {
                        queue.add(new Path(path));
                    }
                    path.remove(node);
                }
            }
        }
        return null;
    }

}
