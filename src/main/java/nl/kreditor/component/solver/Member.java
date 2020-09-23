package nl.kreditor.component.solver;

/**
 * Interface to allow different types of nodes in index matrix using a member ID.
 */
public interface Member {
    int getId();
    int getMemberId();
    void setMemberId(int id);
    String getName();
}
