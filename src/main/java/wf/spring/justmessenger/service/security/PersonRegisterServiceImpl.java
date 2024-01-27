package wf.spring.justmessenger.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wf.spring.justmessenger.dto.auth.RegisterRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.PersonMapper;
import wf.spring.justmessenger.model.exception.ConflictException;
import wf.spring.justmessenger.service.person.PersonSearchService;
import wf.spring.justmessenger.service.person.PersonService;

@Service
@RequiredArgsConstructor
public class PersonRegisterServiceImpl implements PersonRegisterService {

    private final PersonMapper personMapper;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final PersonSearchService personSearchService;

    @Override
    @Transactional
    public void register(RegisterRqDTO registerRqDTO) {
        Person person = personMapper.toPerson(registerRqDTO);

        if(personService.existsByUsername(person.getUsername()))
            throw new ConflictException("Username already taken!");

        if(personService.existsByEmail(person.getEmail()))
            throw new ConflictException("Email already taken!");

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setSelectedStatus(Person.Status.ONLINE);

        person = personService.save(person);
        personSearchService.save(person);
    }

}
