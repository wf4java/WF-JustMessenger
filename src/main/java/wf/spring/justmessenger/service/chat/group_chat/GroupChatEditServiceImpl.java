package wf.spring.justmessenger.service.chat.group_chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.chat.group_chat.edit.GroupChatNameChangeRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.edit.GroupChatNameChangeRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.entity.chat.GroupChat;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;

@Service
@RequiredArgsConstructor
public class GroupChatEditServiceImpl implements GroupChatEditService {

    private final MongoTemplate mongoTemplate;
    private final GroupChatAccessService groupChatAccessService;
    private final MessengerMessagingTemplate messengerMessagingTemplate;


    @Override
    public void changeName(GroupChatNameChangeRqDTO groupChatNameChangeRqDTO, Person principal) {
        groupChatAccessService.changeNameAccess(principal, groupChatNameChangeRqDTO.getChatId());

        Query query = new Query(Criteria.where("_id").is(groupChatNameChangeRqDTO.getChatId()));
        Update update = new Update().set("name", groupChatNameChangeRqDTO.getName());
        mongoTemplate.updateFirst(query, update, GroupChat.class);

        messengerMessagingTemplate.convertAndSendToGroupChatNameUpdate(groupChatNameChangeRqDTO.getChatId().toHexString(),
                new GroupChatNameChangeRsDTO(groupChatNameChangeRqDTO.getChatId(), groupChatNameChangeRqDTO.getName()));
    }
}
