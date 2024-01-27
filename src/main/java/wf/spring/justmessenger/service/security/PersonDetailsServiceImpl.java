package wf.spring.justmessenger.service.security;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.repository.person.PersonRepository;



import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsServiceImpl implements PersonDetailsService {

    private final PersonRepository personRepository;

    @Override
    public Person loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if(person.isEmpty()) throw new UsernameNotFoundException("Authenticate failed!");

        return person.get();
    }

    @Override
    public Person loadUserById(String id) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findById(new ObjectId(id));
        if(person.isEmpty()) throw new UsernameNotFoundException("Authenticate failed!");

        return person.get();
    }

}
