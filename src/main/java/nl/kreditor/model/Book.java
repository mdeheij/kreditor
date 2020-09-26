package nl.kreditor.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.kreditor.form.BookForm;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@EntityListeners(AuditingEntityListener.class)

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String headerImageUrl;

    @LastModifiedBy
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("created DESC")
    private Set<Operation> operations;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Category> categories;

    private Currency mainCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public Book() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    public Book(int id) {
        super();
        this.id = id;
    }

    public Book(BookForm bookForm, User owner) {
        super();
        this.name = bookForm.getName();
        this.owner = owner;
        this.mainCurrency = Currency.getInstance(bookForm.getCurrencyCode());
    }
}