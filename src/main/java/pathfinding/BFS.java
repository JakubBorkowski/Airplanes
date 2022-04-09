package pathfinding;

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
    public LinkedList<Airport[]> startBFS(){
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
                        !(node3[1] == path.getLast()[0]) && isPathNotContainingNode(node3, path)){
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

}
