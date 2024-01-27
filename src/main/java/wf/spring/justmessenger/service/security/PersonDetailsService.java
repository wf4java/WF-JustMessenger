package wf.spring.justmessenger.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wf.spring.justmessenger.entity.person.Person;

public interface PersonDetailsService extends UserDetailsService {
    @Override
    Person loadUserByUsername(String username) throws UsernameNotFoundException;

    Person loadUserById(String id) throws UsernameNotFoundException;
}
