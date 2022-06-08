package pathfinding.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import util.Airport;

import java.util.Objects;

@AllArgsConstructor
public class Node {

    @NonNull @Getter private final Airport initialAirport;
    @NonNull @Getter private final Airport finalAirport;

    public double getDistance() {
        return Math.sqrt(Math.pow((initialAirport.getX() - finalAirport.getX()), 2) +
                Math.pow((initialAirport.getY() - finalAirport.getY()), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return initialAirport.equals(node.getInitialAirport()) && finalAirport.equals(node.getFinalAirport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialAirport, finalAirport);
    }
}