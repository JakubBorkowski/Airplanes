package util;

import lombok.Getter;
import lombok.NonNull;
import pathfinding.model.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Airport {
    @Getter private final int x;
    @Getter private final int y;
    @NonNull @Getter private final String name;
    @NonNull private final World world;

    /**
     * Creates an airport.
     *
     * @param x     X-axis position of airport.
     * @param y     Y-axis position of airport.
     * @param name  Name of airport.
     * @param world World object with information about all airports on map.
     */
    public Airport(int x, int y, @NonNull String name, @NonNull World world) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.world = world;
        world.getAirplanesGUI().drawAirport(x, y, name);
    }

    /**
     * Finds airports in reach for airplane with specified fuel from this airport.
     *
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of airports in reach from this airport.
     */
    public ArrayList<Airport> getNearAirports(double fuel) {
        ArrayList<Airport> nearAirportsArrayList = new ArrayList<>();
        for (Airport airport : world.getAirportsArrayList()) {
            if (fuel >= world.checkDistance(this, airport)) {
                nearAirportsArrayList.add(airport);
            }
        }
        nearAirportsArrayList.remove(this);
        return nearAirportsArrayList;
    }

    /**
     * Finds nodes of airports in reach from this airport by airplane with specified fuel.
     *
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of nodes of airports in reach from this airport.
     */
    public ArrayList<Node> getNodes(double fuel) {
        ArrayList<Node> nearAirportsArrayList = new ArrayList<>();
        for (Airport airport : world.getAirportsArrayList()) {
            if (!airport.equals(this) && fuel >= world.checkDistance(this, airport)) {
                nearAirportsArrayList.add(new Node(this, airport));
            }
        }
        return nearAirportsArrayList;
    }

    /**
     * Finds nodes of airports in reach from this airport by airplane
     * with specified fuel and sort it from nearest to farthest.
     *
     * @param fuel Maximum fuel of airplane for which the flight is being searched for.
     * @return ArrayList of sorted nodes of airports in reach from this airport.
     */
    public ArrayList<Node> getSortedNodes(double fuel) {
        ArrayList<Node> nodesToSort = getNodes(fuel);
        Comparator<Node> compareByDistance = (node1, node2) -> {
            double distance1 = world.checkDistance(node1.getInitialAirport(), node1.getFinalAirport());
            double distance2 = world.checkDistance(node2.getInitialAirport(), node2.getFinalAirport());
            return Double.compare(distance1, distance2);
        };
        nodesToSort.sort(compareByDistance);
        return nodesToSort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return x == airport.x && y == airport.y && name.equals(airport.name) && world.equals(airport.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name, world);
    }

}