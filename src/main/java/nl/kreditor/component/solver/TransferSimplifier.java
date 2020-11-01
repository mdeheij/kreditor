package nl.kreditor.component.solver;

import nl.kreditor.component.transaction.Transaction;
import nl.kreditor.component.currency.SystemCurrency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransferSimplifier {
    private final List<Transaction> transactions;
    private Solver solver;
    private Set<Long> visitedEdges;
    private static final long OFFSET = 1000000000L;

    // TODO: replace this to allow different currencies
    public static final SystemCurrency defaultCurrency = new SystemCurrency("EUR");

    public TransferSimplifier(List<Transaction> transactions) {
        this.transactions = transactions;
        AtomicInteger i = new AtomicInteger(0);

        List<Member> uniqueIndexedMembers = Stream.concat(
                transactions.stream().map(Transaction::getFrom).collect(Collectors.toList()).stream(),
                transactions.stream().map(Transaction::getTo).collect(Collectors.toList()).stream()
        ).distinct().peek(member -> member.setMemberId(i.getAndIncrement())).collect(Collectors.toList());

        this.solver = new Solver(uniqueIndexedMembers);
    }

    private final BigDecimal fromCents = new BigDecimal(100).setScale(64, RoundingMode.HALF_EVEN);

    /**
     * Flatten list of transactions and return a simplified list.
     */
    public List<Transaction> simplify() {
        for (Transaction t : transactions) {
            //Add edge with precision in the form of cents
            solver.addEdge(
                    t.getFrom(),
                    t.getTo(),
                    t.getAmount().setScale(2, RoundingMode.HALF_EVEN)
                            .multiply(new BigDecimal(100).setScale(5, RoundingMode.HALF_EVEN))
                            .longValue()
            );
        }

        return graphs().stream()
                .map(edge -> new Transaction(
                        edge.from,
                        edge.to,
                        defaultCurrency,
                        new BigDecimal(edge.capacity).divide(fromCents, 64, RoundingMode.HALF_EVEN)
                ))
                .collect(Collectors.toList());
    }

    public static boolean isValid(List<Transaction> transactions) {
        HashMap<Member, BigDecimal> saldi = new HashMap<>();
        boolean valid = true;

        BigDecimal zero = new BigDecimal("0.00");

        for (Transaction transaction : transactions) {
            Member from = transaction.getFrom();
            Member to = transaction.getTo();

            saldi.putIfAbsent(from, zero);
            saldi.putIfAbsent(to, zero);

            saldi.put(to, saldi.get(to).add(BigDecimal.valueOf(1)));
            saldi.put(from, saldi.get(from).subtract(transaction.getAmount()));
        }

        if (saldi.values().stream().allMatch(bigDecimal -> bigDecimal.doubleValue() == 0)) {
            valid = false;
        }

        return valid;
    }

    private List<Edge> graphs() {
        // Map to keep track of visited edges
        visitedEdges = new HashSet<>();
        Integer edgePos;

        while ((edgePos = getNonVisitedEdgeIndex(solver.getEdges())) != null) {
            // Force recompute of subsequent flows in the graph
            solver.recompute();
            // Set source and sink in the flow graph
            Edge firstEdge = solver.getEdges().get(edgePos);
            solver.setSource(firstEdge.from);
            solver.setSink(firstEdge.to);

            List<Edge> newEdges = new ArrayList<>();

            for (List<Edge> allEdges : solver.getGraph()) {
                for (Edge edge : allEdges) {
                    long remainingFlow = ((edge.flow < 0) ? edge.capacity : (edge.capacity - edge.flow));
                    // If there is capacity remaining in the graph, then add the remaining capacity as an edge
                    // so that it can be used for optimizing other debts within the graph
                    if (remainingFlow > 0) {
                        newEdges.add(new Edge(edge.from, edge.to, remainingFlow));
                    }
                }
            }

            // Get the maximum flow between the source and sink
            long maxFlow = solver.getMaxFlow();
            // Mark the edge from source to sink as visited
            Member source = solver.getSource();
            Member sink = solver.getSink();
            visitedEdges.add(getHashKeyForEdge(source, sink));
            // Create a new graph
            solver = new Solver(solver.getMemberList());
            // Add edges having remaining capacity
            solver.addEdges(newEdges);
            // Add an edge from source to sink in the new graph with obtained maximum flow as it's weight
            solver.addEdge(source, sink, maxFlow);
        }

        return solver.settle();
    }

    private Integer getNonVisitedEdgeIndex(List<Edge> edges) {
        Integer edgePos = null;
        int curEdge = 0;
        for (Edge edge : edges) {
            if (!visitedEdges.contains(getHashKeyForEdge(edge.from, edge.to))) {
                edgePos = curEdge;
            }
            curEdge++;
        }
        return edgePos;
    }

    /**
     * Get a unique hash key for a given edge between members
     */
    private Long getHashKeyForEdge(Member u, Member v) {
        return u.getMemberId() * OFFSET + v.getMemberId();
    }
}
