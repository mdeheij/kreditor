package nl.kreditor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kreditor.component.solver.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Contact implements Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Book book;

    /**
     * This means this contact belonging to a book is actually a user that can log in to Kreditor
     */
    @Column(nullable = false)
    private boolean isLinked = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner;

    @Transient
    @Getter
    @Setter //todo: onnodig?
    @EqualsAndHashCode.Exclude
    private int memberId;

    public int getId() {
        return id.intValue();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isLinked=" + isLinked +
                ", memberId=" + memberId +
                '}';
    }
}
