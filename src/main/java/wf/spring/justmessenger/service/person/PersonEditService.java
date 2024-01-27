package wf.spring.justmessenger.service.person;

import wf.spring.justmessenger.dto.person.edit.PersonPasswordChangeRqDTO;
import wf.spring.justmessenger.dto.person.edit.PersonUsernameChangeRqDTO;
import wf.spring.justmessenger.entity.person.Person;

public interface PersonEditService {
    void changeUsername(PersonUsernameChangeRqDTO personUsernameChangeRqDTO, Person principal);

    void changePassword(PersonPasswordChangeRqDTO personPasswordChangeRqDTO, Person principal);
}
