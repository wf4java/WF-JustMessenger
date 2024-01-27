package wf.spring.justmessenger.service.person;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import wf.spring.justmessenger.dto.person.status.PersonStatusGetRqDTO;
import wf.spring.justmessenger.dto.person.status.PersonStatusRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PersonStatusServiceImpl implements PersonStatusService {

    @Autowired
    @Lazy
    private PersonService personService;

    private final MongoTemplate mongoTemplate;
    private final MessengerMessagingTemplate messengerMessagingTemplate;
    private final Map<ObjectId, SessionsStatus> personStatusMap = new ConcurrentHashMap<>();




    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        Person principal = (Person) Objects.requireNonNull(event.getUser());
        String sessionId = StompHeaderAccessor.getSessionId(event.getMessage().getHeaders());
        connect(principal, sessionId);
    }



    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Person principal = (Person) Objects.requireNonNull(event.getUser());
        String sessionId = StompHeaderAccessor.getSessionId(event.getMessage().getHeaders());
        disconnect(principal, sessionId);
    }


    public void connect(Person person, String sessionId) {
        if(personStatusMap.containsKey(person.getId())) {
            SessionsStatus sessionsStatus = personStatusMap.get(person.getId());
            sessionsStatus.getSessions().add(sessionId);
        } else {
            personStatusMap.put(person.getId(), new SessionsStatus(sessionId, person.getSelectedStatus()));
        }

        if(person.getSelectedStatus() != Person.Status.INVISIBLE) sendUserStatus(person, person.getSelectedStatus());
    }



    public void disconnect(Person person, String sessionId) {
        SessionsStatus sessionsStatus = personStatusMap.get(person.getId());
        sessionsStatus.getSessions().remove(sessionId);

        if(sessionsStatus.getSessions().isEmpty()){
            personStatusMap.remove(person.getId());
            if(sessionsStatus.getStatus() != Person.Status.INVISIBLE) sendUserStatus(person, Person.Status.INVISIBLE);
        }
    }


    @Override
    public void setStatus(ObjectId id, Person.Status personStatus) {
        personService.exitsByIdOrThrow(id);

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("selectedStatus", personStatus);
        mongoTemplate.updateFirst(query, update, Person.class);

        if(!personStatusMap.containsKey(id)) return;
        SessionsStatus sessionsStatus = personStatusMap.get(id);
        sessionsStatus.setStatus(personStatus);
        sendUserStatus(id, personStatus);
    }


    @Override
    public void setStatus(Person person, Person.Status personStatus) {
        setStatus(person.getId(), personStatus);
    }


    @Override
    public Person.Status getStatus(ObjectId id) {
        SessionsStatus sessionsStatus = personStatusMap.get(id);
        if(sessionsStatus == null) return Person.Status.INVISIBLE;
        return sessionsStatus.getStatus();
    }

    @Override
    public Person.Status getStatus(Person person) {
        return getStatus(person.getId());
    }



    @Override
    public List<Person.Status> getStatuses(List<ObjectId> ids) {
        List<Person.Status> statuses = new ArrayList<>(ids.size());

        for(ObjectId id : ids)
            statuses.add(getStatus(id));

        return statuses;
    }



    private void sendUserStatus(Person person, Person.Status personStatus) {
        sendUserStatus(person.getId(), personStatus);
    }

    private void sendUserStatus(ObjectId id, Person.Status personStatus){
        messengerMessagingTemplate.convertAndSendToTopicOfUserStatus(id.toHexString(), new PersonStatusRsDTO(id, personStatus));
    }

    @Override
    public PersonStatusRsDTO get(PersonStatusGetRqDTO personStatusRqDTO, Person principal) {
        // Access check

        return new PersonStatusRsDTO(personStatusRqDTO.getPersonId(), getStatus(personStatusRqDTO.getPersonId()));
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class SessionsStatus {
        private final List<String> sessions = new ArrayList<>(1);
        private Person.Status status;

        public SessionsStatus(String sessionId, Person.Status status) {
            sessions.add(sessionId);
            this.status = status;
        }
    }


}
