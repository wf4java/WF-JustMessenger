package wf.spring.justmessenger.service.chat.group_chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.Person;

public interface GroupChatAccessService {
    void sendMessageAccess(Person principal, ObjectId groupId);

    void sendAttachmentAccess(Person principal, ObjectId receiverId);

    void getAttachmentAccess(Person principal, ObjectId attachmentId, ObjectId groupId);

    void createAccess(Person principal);

    void setProfilePhotoAccess(Person principal, ObjectId groupId);

    void getAllParticipantAccess(Person principal, ObjectId groupId);

    void getParticipantAccess(Person principal, ObjectId groupId, ObjectId personId);

    void getProfilePhotoAccess(Person principal, ObjectId groupId);

    void deleteProfilePhotoAccess(Person principal, ObjectId groupId);

    void getMessagesAccess(Person principal, ObjectId groupId);

    void getMessageAccess(Person principal, ObjectId groupId, ObjectId messageId);

    void addParticipantAccess(Person principal, ObjectId groupId, ObjectId personId);

    void removeParticipantAccess(Person principal, ObjectId groupId, ObjectId personId);

    void leaveAccess(Person principal, ObjectId groupId);

    void changeNameAccess(Person principal, ObjectId groupId);

    void getGroupChatAccess(Person principal, ObjectId groupId);

    void getGroupChatDeleteAccess(Person principal, ObjectId groupId);
}
