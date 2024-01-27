package wf.spring.justmessenger.service.person;

import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import wf.spring.justmessenger.dto.person.status.PersonStatusGetRqDTO;
import wf.spring.justmessenger.dto.person.status.PersonStatusRsDTO;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;

public interface PersonStatusService {




    void setStatus(ObjectId id, Person.Status personStatus);

    void setStatus(Person person, Person.Status personStatus);

    Person.Status getStatus(ObjectId id);

    Person.Status getStatus(Person person);

    List<Person.Status> getStatuses(List<ObjectId> ids);

    PersonStatusRsDTO get(PersonStatusGetRqDTO personStatusRqDTO, Person principal);
}
