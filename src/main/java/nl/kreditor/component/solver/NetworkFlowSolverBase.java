package nl.kreditor.component.solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Mostly inspired on:
 * @link https://github.com/williamfiset/Algorithms
 */
abstract class NetworkFlowSolverBase {
    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
    protected static final long INF = Long.MAX_VALUE / 2;

    // Inputs: n = number of nodes, s = source, t = sink
    protected final int n;
    protected Member s, t;

    protected long maxFlow;

    protected final boolean[] minCut;
    protected List<Edge>[] graph;
    protected String[] vertexLabels;
    protected final List<Edge> edges;

    // Indicates whether the network flow algorithm has ran. We should not need to
    // run the solver multiple times, because it always yields the same result.
    protected boolean solved;

    /**
     * Creates an instance of a flow network solver. Use the {@link #addEdge} method to add edges to
     * the graph.
     *
     * @param n - The number of nodes in the graph including source and sink nodes.
     */
    public NetworkFlowSolverBase(int n, String[] vertexLabels) {
        this.n = n;
        initializeGraph();
        assignLabelsToVertices(vertexLabels);
        minCut = new boolean[n];
        edges = new ArrayList<>();
    }

    // Construct an empty graph with n nodes including the source and sink nodes.
    private void initializeGraph() {
        //noinspection unchecked
        graph = new List[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
    }

    // Add labels to vertices in the graph.
    private void assignLabelsToVertices(String[] vertexLabels) {
        if (vertexLabels.length != n)
            throw new IllegalArgumentException(String.format("You must pass %s number of labels", n));
        this.vertexLabels = vertexLabels;
    }

    /**
     * Adds a list of directed edges (and residual edges) to the flow graph.
     *
     * @param edges - A list of all edges to be added to the flow graph.
     */
    public void addEdges(List<Edge> edges) {
        if (edges == null) throw new IllegalArgumentException("Edges cannot be null");
        for (Edge edge : edges) {
            addEdge(edge.from, edge.to, edge.capacity);
        }
    }

    /**
     * Adds a directed edge (and residual edge) to the flow graph.
     *
     * @param from     - The index of the node the directed edge starts at.
     * @param to       - The index of the node the directed edge ends at.
     * @param capacity - The capacity of the edge.
     */
    public void addEdge(Member from, Member to, long capacity) {
        if (from.equals(to) || capacity < 0) {
            return;
        }

        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);
        e1.setResidual(e2);
        e2.setResidual(e1);
        graph[from.getMemberId()].add(e1);
        graph[to.getMemberId()].add(e2);
        edges.add(e1);
    }

    /**
     * Cost variant of addEdge(int, int, int) for min-cost max-flow
     */
    public void addEdge(Member from, Member to, long capacity, long cost) {
        Edge e1 = new Edge(from, to, capacity, cost);
        Edge e2 = new Edge(to, from, 0, -cost);
        e1.setResidual(e2);
        e2.setResidual(e1);
        graph[from.getMemberId()].add(e1);
        graph[to.getMemberId()].add(e2);
        edges.add(e1);
    }

    /**
     * Returns the graph after the solver has been executed. This allow you to inspect the {@link
     * Edge#flow} compared to the {@link Edge#capacity} in each edge. This is useful if you want to
     * figure out which edges were used during the max flow.
     */
    public List<Edge>[] getGraph() {
        execute();
        return graph;
    }

    /**
     * Returns all edges in this flow network
     */
    public List<Edge> getEdges() {
        return edges;
    }

    // Returns the maximum flow from the source to the sink.
    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    /**
     * Used to set the source for this flow network
     */
    public void setSource(Member s) {
        this.s = s;
    }

    /**
     * Used to set the sink for this flow network
     */
    public void setSink(Member t) {
        this.t = t;
    }

    /**
     * Get source for this flow network
     */
    public Member getSource() {
        return s;
    }

    /**
     * Get sink for this flow network
     */
    public Member getSink() {
        return t;
    }

    /**
     * Set 'solved' flag to false to force recomputation of subsequent flows.
     */
    public void recompute() {
        solved = false;
    }

    /**
     * Print all edges.
     */
    public void printEdges() {
        for (Edge edge : edges) {
            System.out.printf("%s ----%s----> %s%n", edge.from.getName(), edge.capacity, edge.to.getName());
        }
    }


    // Wrapper method that ensures we only call solve() once
    private void execute() {
        if (solved) return;
        solved = true;
        solve();
    }

    // Method to implement which solves the network flow problem.
    public abstract void solve();
}
