import java.util.*;

public class PathFinder {
    World world;
    Double fuel;
    Airport initialAirport;
    Airport finalAirport;

    PathFinder(Airport initialAirport, Airport finalAirport, Double fuel, World world){
        this.world = world;
        this.fuel = fuel;
        this.initialAirport = initialAirport;
        this.finalAirport = finalAirport;
    }

    public LinkedList<Airport[]> findPath(String algorithmName){
        LinkedList<Airport[]> track = new LinkedList<>();
        switch (algorithmName.toUpperCase(Locale.ROOT)) {
            case "BFS":
                track = startBFS();
                break;
            case "DFS":
                LinkedList<LinkedList<Airport[]>> allPossibleTracks = new LinkedList<>();
                startRecursion(allPossibleTracks);
                if(allPossibleTracks.isEmpty()){
                    track = null;
                    break;
                }
                track = sortAllPossibleTracks(allPossibleTracks);
                break;
            case "DIJKSTRA":
                track = startDijkstra();
                break;
        }
        return track;
    }

    private boolean trackNotContainNode(Airport[] node, LinkedList<Airport[]> track){
        for(Airport[] nodeInTrack : track){
            if(node[0]==nodeInTrack[0] || node[1]==nodeInTrack[1]){
                return false;
            }
        }
        return true;
    }

    private boolean allPossibleTracksNotContainTrack(LinkedList<Airport[]> track, LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        if(!track.get(0)[0].equals(initialAirport) && track.get(track.size()-1)[1].equals(finalAirport)){
            return true;
        }
        int count = 0;
        for(LinkedList<Airport[]> trackFromAllPossibleTracks : allPossibleTracks){
            if(track.size()==trackFromAllPossibleTracks.size()){
                for(Airport[] nodeFromAllPossibleTracks : trackFromAllPossibleTracks){
                    for(Airport[] nodeFromPossibleTrack : track){
                        if(nodeFromAllPossibleTracks[0]==nodeFromPossibleTrack[0] && nodeFromAllPossibleTracks[1]==nodeFromPossibleTrack[1]){
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

    private LinkedList<Airport[]> sortAllPossibleTracks(LinkedList<LinkedList<Airport[]>> allPossibleTracks){

        Comparator<LinkedList<Airport[]>> compareByDistance = new Comparator<LinkedList<Airport[]>>() {
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
        allPossibleTracks.sort(compareByDistance);

        /*System.out.println("\nallPossibleTracks for " + initialAirport.name + " -> " + finalAirport.name + ":");
        for(ArrayList<Airport[]> track : allPossibleTracks){
            System.out.println(printfNodeMap(track));
        }
        System.out.println();*/

        return allPossibleTracks.get(0);
    }

    //Depth-first search//

    private LinkedList<LinkedList<Airport[]>> startRecursion(LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        LinkedList<Airport[]> track = new LinkedList<>();

        if(initialAirport.getNearAirports(fuel).contains(finalAirport)){
            track.add(new Airport[]{initialAirport, finalAirport});
            allPossibleTracks.add(track);
            return allPossibleTracks;
        }

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
        return allPossibleTracks;
    }

    private LinkedList<LinkedList<Airport[]>> recursion(LinkedList<Airport[]> track, LinkedList<LinkedList<Airport[]>> allPossibleTracks){
        boolean nodeFound = false;
        LinkedList<Airport[]> nodesToSearch = new LinkedList<>();//backup
        if(track.size()>0){
            track.get(track.size()-1)[1].getNodes(fuel);
            ArrayList<Airport[]> node1GetNodes = new ArrayList<>(track.get(track.size()-1)[1].getNodes(fuel));
            for(Airport[] nodeNeighbor : node1GetNodes){
                if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && trackNotContainNode(nodeNeighbor, track)){
                    if(nodeNeighbor[1].equals(finalAirport)){
                        track.add(nodeNeighbor);
                        if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                            //System.out.println("Adding to allPossibleTracks = " + printfNodeMap(track));
                            allPossibleTracks.add(new LinkedList<>(track));
                            nodeFound = true;
                        }
                        track.remove(nodeNeighbor);
                        break;
                    }
                    else if(!nodeFound) {
                        nodesToSearch.add(nodeNeighbor);
                    }
                }
            }
            for(Airport[] nodeNeighbor : nodesToSearch){
                if(track.size()>0){
                    if(!(nodeNeighbor[0] == initialAirport) && !(nodeNeighbor[1] == initialAirport) && !(nodeNeighbor[1] == track.get(track.size()-1)[0]) && trackNotContainNode(nodeNeighbor, track)){
                        if(nodeNeighbor[1].equals(this.finalAirport)){
                            track.add(nodeNeighbor);
                            if(allPossibleTracksNotContainTrack(track, allPossibleTracks)){
                                //System.out.println("Adding to allPossibleTracks = " + printfNodleMap(track));
                                allPossibleTracks.add(new LinkedList<>(track));
                                nodeFound = true;
                            }
                            track.remove(nodeNeighbor);
                            break;
                        }
                        else if(!nodeFound && !nodeNeighbor[1].equals(initialAirport) && !nodeNeighbor[1].equals(track.get(track.size()-1)[1]) && trackNotContainNode(nodeNeighbor, track)){
                            track.add(nodeNeighbor);
                            allPossibleTracks = recursion(track, allPossibleTracks);
                            track.remove(nodeNeighbor);
                        }
                    }
                }
            }
            //nodesToSearch.clear();
            //track.clear();
        }
        return allPossibleTracks;
    }

    //Breadth-first search//

    private LinkedList<Airport[]> startBFS(){
        LinkedList<LinkedList<Airport[]>> foundTracks = new LinkedList<>();
        LinkedList<LinkedList<Airport[]>> queue3 = new LinkedList<>();
        LinkedList<Airport[]> track = new LinkedList<>();
        for(Airport[] node : initialAirport.getSortedNodes(fuel)){
            track.add(node);
            if(node[1].equals(finalAirport)){
                return track;
            }
            queue3.add(new LinkedList<>(track));
            track.clear();
        }
        for(int i = 0; i < queue3.size(); i++) {
            LinkedList<Airport[]> track3 = queue3.get(i);
            ArrayList<Airport[]> track3Nodes = track3.getLast()[1].getSortedNodes(fuel);
            for(Airport[] node3 : track3Nodes){
                if(!(node3[0] == initialAirport) && !(node3[1] == initialAirport) && !(node3[1] == track3.getLast()[0]) && trackNotContainNode(node3, track3)){
                    track3.add(node3);
                    if(track3.getLast()[1].equals(finalAirport)){
                        foundTracks.add(new LinkedList<>(track3));
                    }
                    else {
                        queue3.add(new LinkedList<>(track3));
                    }
                    track3.remove(node3);
                }
            }
            if (!foundTracks.isEmpty()){
                return sortAllPossibleTracks(foundTracks);
            }
        }
        return null;
    }

    //Dijkstra's algorithm//

    private LinkedList<Airport[]> startDijkstra(){

        Comparator<DijkstraTable> compareByDistance = new Comparator<DijkstraTable>() {
            @Override
            public int compare(DijkstraTable dijkstraTable1 , DijkstraTable dijkstraTable2) {
                return Double.compare(dijkstraTable1.distance, dijkstraTable2.distance);
            }
        };

        ArrayList<DijkstraTable> dijkstraTableArrayList = new ArrayList<>();
        ArrayList<Airport> visitedAirports = new ArrayList<>();
        ArrayList<Airport> unvisitedAirports = new ArrayList<>(world.airportsArrayList);

        for(Airport airport : world.airportsArrayList){
            if(airport.equals(initialAirport)){
                dijkstraTableArrayList.add(new DijkstraTable(airport, (double) 0, null));
            }
            else {
                dijkstraTableArrayList.add(new DijkstraTable(airport, Double.POSITIVE_INFINITY, null));
            }
        }
        dijkstraTableArrayList.sort(compareByDistance);
        while (unvisitedAirports.size() > 0){
            dijkstraTableArrayList.sort(compareByDistance);
            for(DijkstraTable dijkstraTable : dijkstraTableArrayList){
                if(unvisitedAirports.contains(dijkstraTable.airport)){
                    for(Airport airport : dijkstraTable.airport.getNearAirports(fuel)){
                        if(unvisitedAirports.contains(airport)){
                            if(dijkstraTable.distance + world.checkDistance(dijkstraTable.airport, airport) < findDijkstra(airport, dijkstraTableArrayList).distance){
                                dijkstraTableArrayList.get(dijkstraTableArrayList.indexOf(findDijkstra(airport, dijkstraTableArrayList))).distance = dijkstraTable.distance + world.checkDistance(dijkstraTable.airport, airport);
                                dijkstraTableArrayList.get(dijkstraTableArrayList.indexOf(findDijkstra(airport, dijkstraTableArrayList))).previousAirport = dijkstraTable.airport;
                            }
                        }
                    }
                    visitedAirports.add(dijkstraTable.airport);
                    unvisitedAirports.remove(dijkstraTable.airport);
                    break;
                }
            }
        }
        if(dijkstraTableArrayList.get(dijkstraTableArrayList.indexOf(findDijkstra(finalAirport, dijkstraTableArrayList))).previousAirport == null){
            return null;
        }
        LinkedList<Airport> path = new LinkedList<>();
        path.add(finalAirport);
        Airport airport = dijkstraTableArrayList.get(dijkstraTableArrayList.indexOf(findDijkstra(finalAirport, dijkstraTableArrayList))).previousAirport;
        path.add(airport);
        while (!airport.equals(initialAirport)){
            airport = dijkstraTableArrayList.get(dijkstraTableArrayList.indexOf(findDijkstra(airport, dijkstraTableArrayList))).previousAirport;
            path.add(airport);
        }
        Collections.reverse(path);
        LinkedList<Airport[]> nodePath = new LinkedList<>();
        for(Airport airportFromPath : path){
            if(path.size() > path.indexOf(airportFromPath)+1){
                nodePath.add(new Airport[]{airportFromPath, path.get(path.indexOf(airportFromPath)+1)});
            }
        }
        return nodePath;
    }

    private DijkstraTable findDijkstra(Airport airport, ArrayList<DijkstraTable> dijkstraTableArrayList){
        for (DijkstraTable dijkstraTable : dijkstraTableArrayList){
            if (dijkstraTable.airport.equals(airport)){
                return(dijkstraTable);
            }
        }
        return(null);
    }
}

class DijkstraTable {
    Airport airport;
    Double distance;
    Airport previousAirport;

    DijkstraTable(Airport airport, Double distance, Airport previousAirport){
        this.airport = airport;
        this.distance = distance;
        this.previousAirport = previousAirport;
    }
}