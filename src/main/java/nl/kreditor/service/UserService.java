package nl.kreditor.service;

import nl.kreditor.model.User;
import nl.kreditor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new RuntimeException("Unable to find user");
    }

    @Transactional
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}