package wf.spring.justmessenger.service.person;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.EsPerson;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;

public interface PersonSearchService {
    void save(Person person);

    void updateUsername(ObjectId personId, String username);

    void delete(ObjectId personId);

    List<EsPerson> searchByUsername(String username, int limit);
}
