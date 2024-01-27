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
public class PersonSearchServiceImpl implements PersonSearchService {

    private final EsPersonRepository esPersonRepository;


//    @PostConstruct
//    public void load() {
//        esPersonRepository.deleteAll();
//        personRepository.findAll().forEach(this::save);
//        personRepository.findAll().forEach((p) -> System.out.println(p.getId()));
//        esPersonRepository.findAll().forEach((p) -> System.out.println(p.getId()));
//    }


    @Override
    public void save(Person person) {
        EsPerson esPerson = new EsPerson();

        esPerson.setId(person.getId());
        esPerson.setUsername(person.getUsername());

        esPersonRepository.save(esPerson);
    }


    @Override
    public void updateUsername(ObjectId personId, String username) {
        EsPerson esPerson = esPersonRepository.findById(personId).orElseThrow(() -> new NoSuchElementException("Person not found with id: " + personId));

        esPerson.setUsername(username);

        esPersonRepository.save(esPerson);
    }

    @Override
    public void delete(ObjectId personId) {
        esPersonRepository.deleteById(personId);
    }

    @Override
    public List<EsPerson> searchByUsername(String username, int limit) {
        return esPersonRepository.findAllByUsername(username, PageRequest.of(0, limit));
    }


}
