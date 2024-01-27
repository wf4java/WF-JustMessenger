package wf.spring.justmessenger.service.chat.group_chat;

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
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoDeleteRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoGetRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.entity.chat.GroupChat;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.service.StorageService;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupChatProfilePhotoServiceImpl implements GroupChatProfilePhotoService {

    private final MongoTemplate mongoTemplate;
    private final StorageService storageService;
    private final GroupChatService groupChatService;
    private final GroupChatAccessService groupChatAccessService;
    private final MessengerMessagingTemplate messengerMessagingTemplate;


    @Override
    public void setProfilePhoto(ObjectId id, MultipartFile image, Person principal) {
        groupChatAccessService.setProfilePhotoAccess(principal, id);

        UUID uuid = storageService.save(image);

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("profilePhoto", uuid);

        mongoTemplate.updateFirst(query, update, GroupChat.class);
        messengerMessagingTemplate.convertAndSendToGroupChatProfilePhotoUpdate(id.toHexString());
    }

    @Override
    public Resource getProfilePhoto(GroupChatProfilePhotoGetRqDTO groupChatProfilePhotoGetRqDTO, Person principal) {
        groupChatAccessService.getProfilePhotoAccess(principal, groupChatProfilePhotoGetRqDTO.getChatId());

        GroupChat groupChat = groupChatService.getById(groupChatProfilePhotoGetRqDTO.getChatId());
        if(groupChat.getProfilePhoto() == null)
            return new InputStreamResource(new ByteArrayInputStream(new byte[0]));

        return new InputStreamResource(storageService.get(groupChat.getProfilePhoto()));
    }

    @Override
    public void deleteProfilePhoto(GroupChatProfilePhotoDeleteRqDTO groupChatProfilePhotoDeleteRqDTO, Person principal) {
        groupChatAccessService.deleteProfilePhotoAccess(principal, groupChatProfilePhotoDeleteRqDTO.getChatId());

        Query query = new Query(Criteria.where("_id").is(groupChatProfilePhotoDeleteRqDTO.getChatId()));
        Update update = new Update().unset("profilePhoto");

        mongoTemplate.updateFirst(query, update, GroupChat.class);
        messengerMessagingTemplate.convertAndSendToGroupChatProfilePhotoDelete(groupChatProfilePhotoDeleteRqDTO.getChatId().toHexString());
    }

}
