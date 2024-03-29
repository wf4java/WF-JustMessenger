package wf.spring.justmessenger.service.person;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.person.PersonRsDTO;
import wf.spring.justmessenger.dto.person.PersonSearchRqDTO;
import wf.spring.justmessenger.entity.person.EsPerson;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.PersonMapper;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.repository.person.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {


    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final PersonSearchService personSearchService;
    private final PersonStatusService personStatusService;



    public Optional<Person> findById(ObjectId id) {
        return personRepository.findById(id);
    }


    public Person getById(ObjectId id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Person with this id was not found"));
    }


    public boolean existsById(ObjectId id) {
        return personRepository.existsById(id);
    }


    public void exitsByIdOrThrow(ObjectId id) {
        if(personRepository.existsById(id))
            throw new NotFoundException("Person with this id was not found");
    }


    public boolean existsByUsername(String username) {
        return personRepository.existsByUsername(username);
    }


    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }


    public Person save(Person person) {
        return personRepository.save(person);
    }


    public PersonRsDTO getPersonById(ObjectId id, Person principal) {
        //Check access

        return toPersonRsDTO(getById(id));
    }


    public List<PersonRsDTO> getPersonByIds(List<ObjectId> ids, Person principal) {
        //Check access

        return getPersonByIds(ids);
    }


    public List<PersonRsDTO> search(PersonSearchRqDTO personSearchRqDTO, Person principal) {
        //Check access

        List<EsPerson> esPersonLs = personSearchService.searchByUsername(personSearchRqDTO.getUsername(), personSearchRqDTO.getLimit());

        return getPersonByIds(esPersonLs.stream().map(EsPerson::getId).toList());
    }


    public List<PersonRsDTO> getPersonByIds(List<ObjectId> ids) {
        return toPersonRsDTOList(personRepository.findAllById(ids));
    }


    public PersonRsDTO toPersonRsDTO(Person person) {
        PersonRsDTO personRsDTO = personMapper.toPersonRsDTO(person);
        personRsDTO.setStatus(personStatusService.getStatus(person.getId()));

        return personRsDTO;
    }


    public List<PersonRsDTO> toPersonRsDTOList(List<Person> persons) {
        List<PersonRsDTO> personRsDTOS = new ArrayList<>(persons.size());

        persons.forEach((p) -> {
            PersonRsDTO personRsDTO = personMapper.toPersonRsDTO(p);
            personRsDTO.setStatus(personStatusService.getStatus(p.getId()));

            personRsDTOS.add(personRsDTO);
        });

        return personRsDTOS;
    }


}
