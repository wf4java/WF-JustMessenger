package wf.spring.justmessenger.service.chat.single_chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.Person;

public interface SingleChatAccessService {
    void sendMessageAccess(Person principal, ObjectId receiverId);

    void sendAttachmentAccess(Person principal, ObjectId receiverId);

    void getAttachmentAccess(Person principal, ObjectId attachmentId, ObjectId chatId);

    void getMessagesAccess(Person principal, ObjectId chatId);

    void getMessageAccess(Person principal, ObjectId chatId, ObjectId messageId);

    void getSingleChatAccess(Person principal, ObjectId chatId);
}
