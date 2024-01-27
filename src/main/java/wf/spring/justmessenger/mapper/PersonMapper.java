package wf.spring.justmessenger.mapper;

import org.springframework.stereotype.Component;
import wf.spring.justmessenger.dto.auth.RegisterRqDTO;
import wf.spring.justmessenger.dto.person.PersonRsDTO;
import wf.spring.justmessenger.entity.person.Person;

@Component
public class PersonMapper {

    public Person toPerson(RegisterRqDTO registerRqDTO) {
        Person person = new Person();

        person.setUsername(registerRqDTO.getUsername());
        person.setEmail(registerRqDTO.getEmail());
        person.setPassword(registerRqDTO.getPassword());
        person.setActive(true);

        return person;
    }

    public PersonRsDTO toPersonRsDTO(Person person) {
        PersonRsDTO personRsDTO = new PersonRsDTO();

        personRsDTO.setId(person.getId());
        personRsDTO.setUsername(person.getUsername());
        personRsDTO.setGender(person.getGender());
        personRsDTO.setHasProfilePhoto(person.getProfilePhoto() != null);

        return personRsDTO;
    }

}
