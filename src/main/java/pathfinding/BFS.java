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
     * @return path to finalAirport.
     */
    public Path startBFS(){
        LinkedList<Path> queue = new LinkedList<>();
        Path path = new Path();
        for(Node node : initialAirport.getSortedNodes(fuel)){
            path.add(node);
            if(node.getFinalAirport().equals(finalAirport)){
                return path;
            } else {
                queue.add(new Path(path));
                path.clear();
            }
        }
        for(int i = 0; i < queue.size(); i++) {
            path = queue.get(i);
            ArrayList<Node> path3Nodes = path.getLast().getFinalAirport().getSortedNodes(fuel);
            for(Node node3 : path3Nodes){
                if(!(node3.getInitialAirport() == initialAirport) && !(node3.getFinalAirport() == initialAirport) &&
                        !(node3.getFinalAirport() == path.getLast().getInitialAirport()) && !path.contains(node3)){
                    path.add(node3);
                    if(path.getLast().getFinalAirport().equals(finalAirport)){
                        return path;
                    }
                    else {
                        queue.add(new Path(path));
                    }
                    path.remove(node3);
                }
            }
        }
        return null;
    }

}
