package wf.spring.justmessenger.service.person;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.person.EsPerson;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.repository.person.EsPersonRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PersonSearchService {

    private final EsPersonRepository esPersonRepository;




    public void save(Person person) {
        EsPerson esPerson = new EsPerson();

        esPerson.setId(person.getId());
        esPerson.setUsername(person.getUsername());

        esPersonRepository.save(esPerson);
    }



    public void updateUsername(ObjectId personId, String username) {
        EsPerson esPerson = esPersonRepository.findById(personId).orElseThrow(() -> new NoSuchElementException("Person not found with id: " + personId));

        esPerson.setUsername(username);

        esPersonRepository.save(esPerson);
    }


    public void delete(ObjectId personId) {
        esPersonRepository.deleteById(personId);
    }


    public List<EsPerson> searchByUsername(String username, int limit) {
        return esPersonRepository.findAllByUsername(username, PageRequest.of(0, limit));
    }


}
