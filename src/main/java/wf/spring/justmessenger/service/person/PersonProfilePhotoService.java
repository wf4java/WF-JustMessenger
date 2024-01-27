package wf.spring.justmessenger.service.person;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.entity.person.Person;

public interface PersonProfilePhotoService {
    void setProfilePhoto(MultipartFile image, Person principal);

    void deleteProfilePhoto(Person principal);

    Resource getProfilePhoto(ObjectId personId, Person principal);
}
