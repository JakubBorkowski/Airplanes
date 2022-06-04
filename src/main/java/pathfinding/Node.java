package pathfinding;

import lombok.Getter;
import lombok.NonNull;
import util.Airport;
import util.World;

import java.util.Objects;

public class Node {

    @Getter
    private final Airport initialAirport;

    @Getter
    private final Airport finalAirport;

    public Node(@NonNull Airport initialAirport, @NonNull  Airport finalAirport){
        this.initialAirport = initialAirport;
        this.finalAirport = finalAirport;
    }

    public double getDistance() {
        World world = new World();
        return world.checkDistance(initialAirport, finalAirport);
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