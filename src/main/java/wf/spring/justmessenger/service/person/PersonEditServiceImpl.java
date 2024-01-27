package wf.spring.justmessenger.service.person;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.person.edit.PersonPasswordChangeRqDTO;
import wf.spring.justmessenger.dto.person.edit.PersonUsernameChangeRqDTO;
import wf.spring.justmessenger.dto.person.edit.PersonUsernameUpdateRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.model.exception.ConflictException;

@Service
@RequiredArgsConstructor
public class PersonEditServiceImpl implements PersonEditService {

    private final MongoTemplate mongoTemplate;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final PersonSearchService personSearchService;
    private final MessengerMessagingTemplate messengerMessagingTemplate;


    @Override
    public void changeUsername(PersonUsernameChangeRqDTO personUsernameChangeRqDTO, Person principal) {
        if(personService.existsByUsername(personUsernameChangeRqDTO.getUsername()))
            throw new ConflictException("Username already taken!");

        Query query = new Query(Criteria.where("_id").is(principal.getId()));
        Update update = new Update().set("username", personUsernameChangeRqDTO.getUsername());

        mongoTemplate.updateFirst(query, update, Person.class);
        personSearchService.updateUsername(principal.getId(), personUsernameChangeRqDTO.getUsername());

        messengerMessagingTemplate.convertAndSendToTopicOfUserUsername(principal.getId().toHexString(),
                new PersonUsernameUpdateRsDTO(principal.getId(), personUsernameChangeRqDTO.getUsername()));
    }


    @Override
    public void changePassword(PersonPasswordChangeRqDTO personPasswordChangeRqDTO, Person principal) {
        Query query = new Query(Criteria.where("_id").is(principal.getId()));
        Update update = new Update().set("password", passwordEncoder.encode(personPasswordChangeRqDTO.getPassword()));
        mongoTemplate.updateFirst(query, update, Person.class);

        //Disconnect from user webSocket
    }

}
