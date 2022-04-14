package util;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

public class Airport {
    @NonNull @Getter private final int x;
    @NonNull @Getter private final int y;
    @NonNull @Getter private final String name;
    @NonNull private final World world;

    /**
     * Creates an airport.
     * @param x X-axis position of airport.
     * @param y Y-axis position of airport.
     * @param name Name of airport.
     * @param world util.World object with information about all airports on map.
     */
    Airport(int x, int y, String name, World world){
        this.x = x;
        this.y = y;
        this.name = name;
        this.world = world;
        world.getAirplanesGUI().drawAirport(x, y, name);
    }

    /**
     * Finds airports in reach for airplane with specified fuel from this airport.
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of airports in reach from this airport.
     */
    public ArrayList<Airport> getNearAirports(double fuel){
        ArrayList<Airport> nearAirportsArrayList = new ArrayList<>();
        for(Airport airport : world.getAirportsArrayList()){
            if(fuel >= world.checkDistance(this, airport)){
                nearAirportsArrayList.add(airport);
            }
        }
        nearAirportsArrayList.remove(this);
        return nearAirportsArrayList;
    }

    /**
     * Finds nodes of airports in reach from this airport by airplane with specified fuel.
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of nodes of airports in reach from this airport.
     */
    public ArrayList<Airport[]> getNodes(double fuel){
        ArrayList<Airport[]> nearAirportsArrayList = new ArrayList<>();
        for(Airport airport : world.getAirportsArrayList()){
            if(!airport.equals(this) && fuel >= world.checkDistance(this, airport)){
                Airport[] node = {this, airport};
                nearAirportsArrayList.add(node);
            }
        }
        return nearAirportsArrayList;
    }

    /**
     * Finds nodes of airports in reach from this airport by airplane
     * with specified fuel and sort it from nearest to farthest.
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of sorted nodes of airports in reach from this airport.
     */
    public ArrayList<Airport[]> getSortedNodes(double fuel){
        ArrayList<Airport[]> nodesToSort = getNodes(fuel);
        Comparator<Airport[]> compareByDistance = (node1, node2) -> {
            double distance1 = world.checkDistance(node1[0], node1[1]);
            double distance2 = world.checkDistance(node2[0], node2[1]);
            return Double.compare(distance1, distance2);
        };
        nodesToSort.sort(compareByDistance);
        return nodesToSort;
    }
}