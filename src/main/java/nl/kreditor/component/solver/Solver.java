package nl.kreditor.component.solver;


import lombok.Getter;

import java.util.*;

import static java.lang.Math.min;

/**
 * Implementation of Dinic's network flow algorithm. The algorithm works by first constructing a
 * level graph using a BFS and then finding augmenting paths on the level graph using multiple DFSs.
 *
 * Time Complexity: O(EV²)
 *
 * @link https://github.com/williamfiset/Algorithms
 */
public class Solver extends NetworkFlowSolverBase {
    @Getter
    private final List<Member> memberList;
    private final int[] level;

    /**
     * Creates an instance of a flow network solver. Use the addEdge method to add edges to the graph.
     */
    public Solver(List<Member> memberList) {
        super(memberList.size(), memberList.stream().map(Member::getName).toArray(String[]::new));
        level = new int[memberList.size()];
        this.memberList = memberList;
    }

    @Override
    public void solve() {
        // next[i] indicates the next unused edge index in the adjacency list for node i. This is part
        // of the Shimon Even and Alon Itai optimization of pruning deads ends as part of the DFS phase.
        int[] next = new int[n];

        while (bfs()) {
            Arrays.fill(next, 0);
            // Find max flow by adding all augmenting path flows.
            for (long f = dfs(s, next, INF); f != 0; f = dfs(s, next, INF)) {
                maxFlow += f;
            }
        }

        for (int i = 0; i < n; i++) if (level[i] != -1) minCut[i] = true;
    }

    // Do a BFS from source to sink and compute the depth/level of each node
    // which is the minimum number of edges from that node to the source.
    private boolean bfs() {
        Arrays.fill(level, -1);
        level[s.getMemberId()] = 0;
        Deque<Integer> q = new ArrayDeque<>(n);
        q.offer(s.getMemberId());
        while (!q.isEmpty()) {
            int node = q.poll();
            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && level[edge.to.getMemberId()] == -1) {
                    level[edge.to.getMemberId()] = level[node] + 1;
                    q.offer(edge.to.getMemberId());
                }
            }
        }
        return level[t.getMemberId()] != -1;
    }

    private long dfs(Member member, int[] next, long flow) {
        int at = member.getMemberId();
        if (at == t.getMemberId()) return flow;
        final int numEdges = graph[at].size();

        for (; next[at] < numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            long cap = edge.remainingCapacity();
            if (cap > 0 && level[edge.to.getMemberId()] == level[at] + 1) {

                long bottleNeck = dfs(edge.to, next, min(flow, cap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }

    /**
     * Settle all remaining redundant transactions by subtracting duplicates and remove their edges.
     */
    public List<Edge> settle() {
        List<Edge> settled = new ArrayList<>(edges);
        for (Edge x : edges) {
            for (Edge y : edges) {
                if (! x.equals(y) && x.isOpposite(y)) {
                    long calculated = x.calculate(y);

                    if (calculated < 0) {
                        settled.removeAll(Arrays.asList(x, y));
                    } else if (calculated != 0) {
                        settled.add(new Edge(x.from, y.from, calculated));
                    }
                }
            }
        }

        return settled;
    }
}
