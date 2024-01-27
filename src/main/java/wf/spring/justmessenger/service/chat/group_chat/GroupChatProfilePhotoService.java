package wf.spring.justmessenger.service.chat.group_chat;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoDeleteRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoGetRqDTO;
import wf.spring.justmessenger.entity.person.Person;

public interface GroupChatProfilePhotoService {
    void setProfilePhoto(ObjectId id, MultipartFile image, Person principal);

    Resource getProfilePhoto(GroupChatProfilePhotoGetRqDTO groupChatProfilePhotoGetRqDTO, Person principal);

    void deleteProfilePhoto(GroupChatProfilePhotoDeleteRqDTO groupChatProfilePhotoDeleteRqDTO, Person principal);
}
