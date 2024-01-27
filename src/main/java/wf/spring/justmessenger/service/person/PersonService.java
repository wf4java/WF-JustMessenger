package wf.spring.justmessenger.service.person;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.dto.person.PersonRsDTO;
import wf.spring.justmessenger.dto.person.PersonSearchRqDTO;
import wf.spring.justmessenger.entity.person.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<Person> findById(ObjectId id);

    Person getById(ObjectId id);

    boolean existsById(ObjectId id);

    void exitsByIdOrThrow(ObjectId id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Person save(Person person);

    PersonRsDTO getPersonById(ObjectId id, Person principal);

    List<PersonRsDTO> getPersonByIds(List<ObjectId> ids, Person principal);

    List<PersonRsDTO> search(PersonSearchRqDTO personSearchRqDTO, Person principal);

    List<PersonRsDTO> getPersonByIds(List<ObjectId> ids);

    PersonRsDTO toPersonRsDTO(Person person);

    List<PersonRsDTO> toPersonRsDTOList(List<Person> persons);
}
