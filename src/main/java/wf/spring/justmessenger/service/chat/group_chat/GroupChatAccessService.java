package wf.spring.justmessenger.service.chat.group_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.chat.GroupChat;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.AccessException;
import wf.spring.justmessenger.model.exception.ConflictException;
import wf.spring.justmessenger.service.chat.AttachmentService;
import wf.spring.justmessenger.service.chat.MessageService;


@Service
@RequiredArgsConstructor
public class GroupChatAccessService {


    @Lazy
    @Autowired
    private GroupChatParticipantService groupChatParticipantService;
    @Lazy
    @Autowired
    private GroupChatService groupChatService;
    private final MessageService messageService;
    private final AttachmentService attachmentService;





    public void sendMessageAccess(Person principal, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to send message to this GroupChat");
    }


    public void sendAttachmentAccess(Person principal, ObjectId receiverId) {
        sendMessageAccess(principal, receiverId);
    }


    public void getAttachmentAccess(Person principal, ObjectId attachmentId, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!attachmentService.existsByIdAndChatId(attachmentId, groupId))
            throw new AccessException("You do not have access to this attachment");

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get attachment from this GroupChat");
    }





    public void createAccess(Person principal) {
        // Check for access to create group
    }



    public void setProfilePhotoAccess(Person principal, ObjectId groupId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to set ProfilePhoto for this GroupChat");
    }


    public void getAllParticipantAccess(Person principal, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get all participant from this GroupChat");
    }


    public void getParticipantAccess(Person principal, ObjectId groupId, ObjectId personId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get participant from this GroupChat");
    }


    public void getProfilePhotoAccess(Person principal, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get ProfilePhoto for this GroupChat");
    }


    public void deleteProfilePhotoAccess(Person principal, ObjectId groupId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to delete ProfilePhoto for this GroupChat");
    }



    public void getMessagesAccess(Person principal, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get messages from this GroupChat");
    }


    public void getMessageAccess(Person principal, ObjectId groupId, ObjectId messageId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have access to get message from this GroupChat");

        if(!messageService.existsByIdAndChatId(messageId, groupId))
            throw new AccessException("You do not have access to this message");
    }


    public void addParticipantAccess(Person principal, ObjectId groupId, ObjectId personId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to add member to this GroupChat");

        if(groupChatParticipantService.existsByGroupIdAndPersonId(groupChat.getChatId(), personId))
            throw new ConflictException("This Person is already in this GroupChat");

        // Check for blocking
    }


    public void removeParticipantAccess(Person principal, ObjectId groupId, ObjectId personId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to remove member from this GroupChat");

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupChat.getChatId(), personId))
            throw new ConflictException("This Person is already not in this GroupChat");
    }



    public void leaveAccess(Person principal, ObjectId groupId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupChat.getChatId(), principal.getId()))
            throw new ConflictException("You already not in this GroupChat");

        if(groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("The owner of the GroupChat cannot shower while there are persons there");
    }


    public void changeNameAccess(Person principal, ObjectId groupId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to set ProfilePhoto for this GroupChat");
    }


    public void getGroupChatAccess(Person principal, ObjectId groupId) {
        groupChatService.exitsByIdOrThrow(groupId);

        if(!groupChatParticipantService.existsByGroupIdAndPersonId(groupId, principal.getId()))
            throw new AccessException("You do not have to get this GroupChat");
    }


    public void getGroupChatDeleteAccess(Person principal, ObjectId groupId) {
        GroupChat groupChat = groupChatService.getById(groupId);

        if(!groupChat.getOwnerId().equals(principal.getId()))
            throw new AccessException("You do not have access to add member to this GroupChat");
    }
}
