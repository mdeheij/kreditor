package nl.kreditor.component.solver;

import lombok.Data;

/**
 * Edge with a given cost and capacity to be used in graph between different members.
 */
@Data
public class Edge {
    public Member from, to;

    private Edge residual;
    public long flow, cost;
    public final long capacity, originalCost;

    public Edge(Member from, Member to, long capacity) {
        this(from, to, capacity, 0 /* unused */);
    }

    public Edge(Member from, Member to, long capacity, long cost) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.originalCost = this.cost = cost;
    }

    public long remainingCapacity() {
        return capacity - flow;
    }

    public void augment(long bottleNeck) {
        flow += bottleNeck;
        residual.flow -= bottleNeck;
    }

    public boolean isOpposite(Edge other) {
        return this.from.equals(other.to) || this.to.equals(other.from);
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long calculate(Edge other) {
        return (this.capacity - other.capacity);
    }
}