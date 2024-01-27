package wf.spring.justmessenger.repository.person;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.person.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, ObjectId> {

    public Optional<Person> findByUsername(String username);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

}
