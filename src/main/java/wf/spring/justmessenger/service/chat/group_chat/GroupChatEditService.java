package wf.spring.justmessenger.service.chat.group_chat;

import wf.spring.justmessenger.dto.chat.group_chat.edit.GroupChatNameChangeRqDTO;
import wf.spring.justmessenger.entity.person.Person;

public interface GroupChatEditService {
    void changeName(GroupChatNameChangeRqDTO groupChatNameChangeRqDTO, Person principal);
}
