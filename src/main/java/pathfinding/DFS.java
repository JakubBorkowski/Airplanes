package pathfinding;

import util.Airport;
import util.World;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Depth-first search algorithm
 */
public class DFS extends PathFinder {

    public DFS(Airport initialAirport, Airport finalAirport, Double fuel, World world) {
        super(initialAirport, finalAirport, fuel, world);
    }

    /**
     * Starts Depth-first search algorithm.
     * @return path to finalAirport.
     */
    public LinkedList<Airport[]> startDFS(){
        LinkedList<Airport[]> path = new LinkedList<>();
        //If it is possible to travel directly to destination - return appropriate path//
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
                        !(nodeNeighbor[1] == path.get(path.size()-1)[0]) && isPathNotContainingNode(nodeNeighbor,path)){
                    if(nodeNeighbor[1].equals(finalAirport)){
                        path.add(nodeNeighbor);
                        if(isAllPossiblePathsNotContainingPath(path, allPossiblePaths)){
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
                            isPathNotContainingNode(nodeNeighbor, path)){
                        if(nodeNeighbor[1].equals(this.finalAirport)){
                            path.add(nodeNeighbor);
                            if(isAllPossiblePathsNotContainingPath(path, allPossiblePaths)){
                                allPossiblePaths.add(new LinkedList<>(path));
                            }
                            path.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeNeighbor[1].equals(initialAirport) &&
                                !nodeNeighbor[1].equals(path.get(path.size()-1)[1]) &&
                                isPathNotContainingNode(nodeNeighbor, path)){
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

}
