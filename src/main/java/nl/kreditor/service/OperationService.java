package nl.kreditor.service;

import nl.kreditor.model.Book;
import nl.kreditor.model.Operation;
import nl.kreditor.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {
    @Autowired
    private OperationRepository repository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<Operation> findAll() {
        return (List<Operation>) repository.findAll();
    }
    public List<Operation> findByBook(Book book) {
        return repository.findByBookOrderByCreatedDesc(book);
    }

    public Operation findById(int id) {
        Optional<Operation> result = repository.findById(id);

        if (result.isPresent()) {
            return result.get();
        }

        throw new RuntimeException("Unable to find Operation");
    }

    public Long count() {
        return repository.count();
    }

    @Transactional
    public Operation persist(Operation Operation) { //TODO: is dit ok voor een EntityService?
        entityManager.persist(Operation);
        entityManager.flush();
        return Operation;
    }

    public void deleteById(int OperationsListId) {
        repository.deleteById(OperationsListId);
    }
}