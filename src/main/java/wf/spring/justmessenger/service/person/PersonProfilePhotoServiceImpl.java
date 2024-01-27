package wf.spring.justmessenger.service.person;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.service.StorageService;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonProfilePhotoServiceImpl implements PersonProfilePhotoService {

    private final PersonService personService;
    private final MongoTemplate mongoTemplate;
    private final StorageService storageService;
    private final MessengerMessagingTemplate messengerMessagingTemplate;

    @Override
    public void setProfilePhoto(MultipartFile image, Person principal) {
        UUID uuid = storageService.save(image);

        Query query = new Query(Criteria.where("_id").is(principal.getId()));
        Update update = new Update().set("profilePhoto", uuid);

        mongoTemplate.updateFirst(query, update, Person.class);

        messengerMessagingTemplate.convertAndSendToTopicOfUserProfilePhotoUpdate(principal.getId().toHexString());
    }

    @Override
    public void deleteProfilePhoto(Person principal) {
        Query query = new Query(Criteria.where("_id").is(principal.getId()));
        Update update = new Update().unset("profilePhoto");

        mongoTemplate.updateFirst(query, update, Person.class);

        messengerMessagingTemplate.convertAndSendToTopicOfUserProfilePhotoDelete(principal.getId().toHexString());
    }

    @Override
    public Resource getProfilePhoto(ObjectId personId, Person principal) {
        Person person = personService.getById(personId);
        if(person.getProfilePhoto() == null)
            return new InputStreamResource(new ByteArrayInputStream(new byte[0]));

        return new InputStreamResource(storageService.get(person.getProfilePhoto()));
    }

}
