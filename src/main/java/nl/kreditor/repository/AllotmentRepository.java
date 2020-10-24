package nl.kreditor.repository;

import nl.kreditor.model.Allotment;
import nl.kreditor.model.Contact;
import nl.kreditor.model.Operation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllotmentRepository extends CrudRepository<Allotment, Integer> {
    List<Contact> findAllByOperation(Operation operation);

    Allotment findById(int id);
}